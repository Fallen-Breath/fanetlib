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

package me.fallenbreath.fanetlib.api;

import me.fallenbreath.fanetlib.api.handler.C2SPacketHandler;
import me.fallenbreath.fanetlib.api.handler.S2CPacketHandler;
import me.fallenbreath.fanetlib.impl.FanetlibRegistry;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;

public abstract class FanetlibApi
{
	public static <P> void registerC2SPacket(Identifier id, PacketCodec<P> codec, C2SPacketHandler<P> handler)
	{
		FanetlibRegistry.C2S_PLAY.register(id, codec, handler);
	}

	public static <P> void registerS2CPacket(Identifier id, PacketCodec<P> codec, S2CPacketHandler<P> handler)
	{
		FanetlibRegistry.S2C_PLAY.register(id, codec, handler);
	}

	public static <P> Packet<?> createS2CPacket(Identifier id, P packet)
	{
		return FanetlibRegistry.S2C_PLAY.createPacket(id, packet);
	}

	public static <P> Packet<?> createC2SPacket(Identifier id, P packet)
	{
		return FanetlibRegistry.C2S_PLAY.createPacket(id, packet);
	}
}
