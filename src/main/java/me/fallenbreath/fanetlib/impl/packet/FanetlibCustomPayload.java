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
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

//#if MC >= 12002
//$$ import net.minecraft.network.packet.CustomPayload;
//#endif

public class FanetlibCustomPayload<P> implements
		//#if MC >= 12002
		//$$ CustomPayload
		//#else
		FakeMcCustomPayload
		//#endif
{
	private final PacketId<P> id;
	private final PacketCodec<P> codec;
	private final P userPacket;

	public FanetlibCustomPayload(PacketId<P> id, PacketCodec<P> codec, P userPacket)
	{
		this.id = id;
		this.codec = codec;
		this.userPacket = userPacket;
	}

	public FanetlibCustomPayload(PacketId<P> id, PacketCodec<P> codec, PacketByteBuf buf)
	{
		this(id, codec, codec.decode(buf));
	}

	public P getUserPacket()
	{
		return this.userPacket;
	}

	//#if MC < 12005
	@Override
	//#endif
	public void write(PacketByteBuf buf)
	{
		this.codec.encode(this.userPacket, buf);
	}

	//#if MC < 12005
	@Override
	//#endif
	public Identifier id()
	{
		return this.id.getIdentifier();
	}

	//#if MC >= 12005
	//$$ @Override
	//$$ public Id<? extends CustomPayload> getId()
	//$$ {
	//$$ 	return new CustomPayload.Id<>(this.id.getIdentifier());
	//$$ }
	//#endif
}
