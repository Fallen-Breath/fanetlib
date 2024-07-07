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

package me.fallenbreath.fanetlib.api.packet;

import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistry;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;

public abstract class FanetlibPackets
{
	public static <P> void registerC2S(Identifier id, PacketCodec<P> codec, PacketHandlerC2S<P> handler)
	{
		FanetlibPacketRegistry.C2S_PLAY.register(id, codec, handler);
	}

	public static <P> void registerS2C(Identifier id, PacketCodec<P> codec, PacketHandlerS2C<P> handler)
	{
		FanetlibPacketRegistry.S2C_PLAY.register(id, codec, handler);
	}

	public static <P> void registerDual(Identifier id, PacketCodec<P> codec, PacketHandlerC2S<P> c2sHandler, PacketHandlerS2C<P> s2cHandler)
	{
		registerC2S(id, codec, c2sHandler);
		registerS2C(id, codec, s2cHandler);
	}

	public static <P> CustomPayloadS2CPacket createS2C(Identifier id, P packet)
	{
		return FanetlibPacketRegistry.S2C_PLAY.createPacket(id, packet);
	}

	public static <P> CustomPayloadC2SPacket createC2S(Identifier id, P packet)
	{
		return FanetlibPacketRegistry.C2S_PLAY.createPacket(id, packet);
	}
}
