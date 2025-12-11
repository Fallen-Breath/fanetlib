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

import me.fallenbreath.fanetlib.api.packet.PacketHandlerS2C;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import me.fallenbreath.fanetlib.impl.packet.FanetlibCustomPayload;
import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistry;
import me.fallenbreath.fanetlib.impl.packet.PacketHandlerContextImpl;
import me.fallenbreath.fanetlib.impl.packet.RegistryEntry;
import me.fallenbreath.fanetlib.mixins.access.CustomPayloadS2CPacketAccessor;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12002
//$$ import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//#endif

@Mixin(
		//#if MC >= 12002
		//$$ ClientCommonPacketListenerImpl.class
		//#else
		ClientPacketListener.class
		//#endif
)
public abstract class ClientPlayNetworkHandlerMixin
{
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Inject(
			//#if MC >= 12002
			//$$ method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V",
			//#else
			method = "onCustomPayload",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	private void handleCustomPayloadS2CPacket(ClientboundCustomPayloadPacket packet, CallbackInfo ci)
	{
		//#if MC >= 12005
		//$$ ResourceLocation identifier = packet.payload().type().id();
		//#elseif MC >= 12002
		//$$ ResourceLocation identifier = packet.payload().id();
		//#else
		ResourceLocation identifier = ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		//#endif

		PacketId packetId = new PacketId(identifier);
		RegistryEntry<?, PacketHandlerS2C<?>> entry = FanetlibPacketRegistry.S2C_PLAY.getEntry(packetId);
		if (entry == null)
		{
			return;
		}

		//#if MC >= 12002
		//$$ if (packet.payload() instanceof FanetlibCustomPayload fcp && (Object)this instanceof ClientPacketListener self)
		//$$ {
		//$$ 	handleCustomPayload((PacketHandlerS2C)entry.getHandler(), fcp, self);
		//$$ 	ci.cancel();
		//$$ }
		//#else
		FriendlyByteBuf buf = ((CustomPayloadS2CPacketAccessor)packet).getData();
		try
		{
			FanetlibCustomPayload<?> payload = new FanetlibCustomPayload<>(packetId, entry.getCodec(), buf);
			handleCustomPayload((PacketHandlerS2C)entry.getHandler(), payload, (ClientPacketListener)(Object)this);
			ci.cancel();
		}
		finally
		{
			// Fix https://bugs.mojang.com/browse/MC-121884, for fanetlib packets
			buf.release();
		}
		//#endif
	}

	@Unique
	private static <P> void handleCustomPayload(PacketHandlerS2C<P> handler, FanetlibCustomPayload<P> payload, ClientPacketListener self)
	{
		handler.handle(payload.getUserPacket(), new PacketHandlerContextImpl.S2C(self));
	}
}
