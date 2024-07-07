/*
 * This file is part of the fanetlib project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
 *
 * fanetlib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fanetlib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fanetlib.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.fallenbreath.fanetlib.mixins.hook;

import me.fallenbreath.fanetlib.api.packet.PacketHandlerC2S;
import me.fallenbreath.fanetlib.impl.packet.FanetlibCustomPayload;
import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistry;
import me.fallenbreath.fanetlib.impl.packet.PacketHandlerContextImpl;
import me.fallenbreath.fanetlib.impl.packet.RegistryEntry;
import me.fallenbreath.fanetlib.mixins.access.CustomPayloadS2CPacketAccessor;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if 12002 <= MC && MC < 12005
//$$ import net.minecraft.server.network.ServerCommonNetworkHandler;
//#endif

@Mixin(
		//#if 12002 <= MC && MC < 12005
		//$$ ServerCommonNetworkHandler.class
		//#else
		ServerPlayNetworkHandler.class
		//#endif
)
public abstract class ServerPlayNetworkHandlerMixin
{
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	private void handleCustomPayloadC2SPacket(CustomPayloadC2SPacket packet, CallbackInfo ci)
	{
		//#if MC >= 12005
		//$$ Identifier identifier = packet.payload().getId().id();
		//#elseif MC >= 12002
		//$$ Identifier identifier = packet.payload().id();
		//#else
		Identifier identifier = ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		//#endif

		RegistryEntry<?, PacketHandlerC2S<?>> entry = FanetlibPacketRegistry.C2S_PLAY.getEntry(identifier);
		if (entry == null)
		{
			return;
		}

		//#if MC >= 12002
		//$$ if (packet.payload() instanceof FanetlibCustomPayload fcp && (Object)this instanceof ServerPlayNetworkHandler self)
		//$$ {
		//$$ 	handleCustomPayload(fcp, (PacketHandlerC2S)entry.getHandler(), self);
		//$$ 	ci.cancel();
		//$$ }
		//#else
		PacketByteBuf packetByteBuf = ((CustomPayloadS2CPacketAccessor)packet).getData();
		try
		{
			FanetlibCustomPayload<?> payload = new FanetlibCustomPayload<>(identifier, entry.getCodec(), packetByteBuf);
			handleCustomPayload(payload, (PacketHandlerC2S)entry.getHandler(), (ServerPlayNetworkHandler)(Object)this);
			ci.cancel();
		}
		finally
		{
			// Fix https://bugs.mojang.com/browse/MC-121884, for XXXX packets
			packetByteBuf.release();
		}
		//#endif
	}

	@Unique
	private static <P> void handleCustomPayload(FanetlibCustomPayload<P> payload, PacketHandlerC2S<P> handler, ServerPlayNetworkHandler self)
	{
		handler.handle(payload.getUserPacket(), new PacketHandlerContextImpl.C2S(self));
	}
}
