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

package me.fallenbreath.fanetlib.impl;

import me.fallenbreath.fanetlib.api.PacketCodec;
import net.minecraft.util.PacketByteBuf;

public class FanetlibPacketCodecImpl<P> implements PacketCodec<P>
{
	private final Encoder<P> encoder;
	private final Decoder<P> decoder;

	public FanetlibPacketCodecImpl(Encoder<P> encoder, Decoder<P> decoder)
	{
		this.encoder = encoder;
		this.decoder = decoder;
	}

	@Override
	public void encode(P packet, PacketByteBuf buf)
	{
		this.encoder.encode(packet, buf);
	}

	@Override
	public P decode(PacketByteBuf buf)
	{
		return this.decoder.decode(buf);
	}
}
