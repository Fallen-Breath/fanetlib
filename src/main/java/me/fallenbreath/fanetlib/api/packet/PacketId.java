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

import net.minecraft.util.Identifier;

import java.util.Objects;

public class PacketId<P>
{
	private final Identifier identifier;

	public PacketId(Identifier identifier)
	{
		this.identifier = identifier;
	}

	public static <P> PacketId<P> of(Identifier identifier)
	{
		return new PacketId<>(identifier);
	}

	public static <P> PacketId<P> of(String namespace, String path)
	{
		return of(
				//#if MC >= 12100
				//$$ Identifier.of
				//#else
				new Identifier
				//#endif
						(namespace, path)
		);
	}

	public Identifier getIdentifier()
	{
		return this.identifier;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PacketId<?> packetId = (PacketId<?>)o;
		return Objects.equals(identifier, packetId.identifier);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(identifier);
	}
}
