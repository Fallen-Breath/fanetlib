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

package me.fallenbreath.fanetlib.mixins.register;

import com.google.common.collect.ImmutableMap;
import me.fallenbreath.fanetlib.impl.packet.FanetlibCustomPayload;
import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistrationCenterHelper;
import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

// used in mc [1.20.2, 1.20.5)
@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(CustomPayloadC2SPacket.class)
public abstract class CustomPayloadC2SPacketMixin
{
	@Mutable
	@Shadow
	@Final
	private static Map<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>> ID_TO_READER;

	static
	{
		FanetlibPacketRegistrationCenterHelper.collectC2S();
		var builder = ImmutableMap.<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>>builder().putAll(ID_TO_READER);
		FanetlibPacketRegistry.C2S_PLAY.getRegistry().forEach((id, entry) -> {
			builder.put(id.getIdentifier(), buf -> new FanetlibCustomPayload(id, entry.getCodec(), buf));
		});
		ID_TO_READER = builder.build();
	}
}
