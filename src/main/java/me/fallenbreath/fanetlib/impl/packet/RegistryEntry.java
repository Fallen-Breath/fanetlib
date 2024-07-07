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

package me.fallenbreath.fanetlib.impl.packet;

import me.fallenbreath.fanetlib.api.packet.PacketCodec;

public class RegistryEntry<P, H>
{
	private final PacketCodec<P> codec;
	private final H handler;

	RegistryEntry(PacketCodec<P> codec, H handler)
	{
		this.codec = codec;
		this.handler = handler;
	}

	public PacketCodec<P> getCodec()
	{
		return this.codec;
	}

	public H getHandler()
	{
		return this.handler;
	}
}
