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

import io.netty.buffer.Unpooled;
import me.fallenbreath.fanetlib.FanetlibMod;
import me.fallenbreath.fanetlib.api.nbt.NbtFormat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class NbtUtilsImpl
{
	private static final int TAG_ID_COMPOUND = 0x0A;

	// Notes: reader index untouched
	public static NbtFormat guessNbtFormat(PacketByteBuf buf)
	{
		int n = buf.readableBytes();

		int prevReaderIndex = buf.readerIndex();
		try
		{
			if (n < 2)
			{
				return NbtFormat.UNKNOWN;
			}

			byte typeId = buf.readByte();
			if (typeId != TAG_ID_COMPOUND)
			{
				return NbtFormat.UNKNOWN;
			}

			if (n == 2)
			{
				if (buf.readByte() == 0)
				{
					// >=1.20.2, empty nbt
					return NbtFormat.NEW;
				}
				return NbtFormat.UNKNOWN;
			}
			else  // n > 2
			{
				byte[] bytes = new byte[2];
				buf.readBytes(bytes);

				// Double 0x00 for the empty root tag name
				if (bytes[0] == 0 && bytes[1] == 0)
				{
					return NbtFormat.OLD;
				}
				// A valid nbt type id
				else if (0 <= bytes[0] && bytes[0] < 13)
				{
					return NbtFormat.NEW;
				}
			}
		}
		finally
		{
			buf.readerIndex(prevReaderIndex);
		}

		return NbtFormat.UNKNOWN;
	}

	public static CompoundTag readNbtAuto(PacketByteBuf buf)
	{
		NbtFormat nbtFormat = guessNbtFormat(buf);
		return Objects.requireNonNull(readNbtImpl(nbtFormat, buf));
	}

	public static void writeNbtWithFormat(PacketByteBuf buf, CompoundTag nbt)
	{
		buf.writeVarInt(NbtFormat.CURRENT.ordinal());
		//#if MC >= 12002
		//$$ buf.writeNbt(nbt);
		//#else
		buf.writeCompoundTag(nbt);
		//#endif
	}

	public static CompoundTag readNbtWithFormat(PacketByteBuf buf)
	{
		int styleId = buf.readVarInt();
		NbtFormat nbtFormat = NbtFormat.values()[styleId];
		return Objects.requireNonNull(readNbtImpl(nbtFormat, buf));
	}

	@Nullable
	private static CompoundTag readNbtImpl(NbtFormat bufNbtFormat, PacketByteBuf buf)
	{
		if (bufNbtFormat == NbtFormat.UNKNOWN)
		{
			FanetlibMod.LOGGER.debug("NbtUtilsImpl.readNbtImpl() called with unknown NbtFormat");
		}

		if (NbtFormat.CURRENT == NbtFormat.OLD && bufNbtFormat == NbtFormat.NEW)
		{
			// I'm < mc1.20.2 (OLD), trying to read a nbt in NEW style

			//#if MC < 12002
			int prevReaderIndex = buf.readerIndex();
			PacketByteBuf tweakedBuf = new PacketByteBuf(Unpooled.buffer());
			tweakedBuf.writeByte(buf.readByte());  // 0x0A, tag type
			tweakedBuf.writeByte(0).writeByte(0);  // 2* 0x00
			tweakedBuf.writeBytes(buf);
			buf.readerIndex(prevReaderIndex);

			CompoundTag nbt = tweakedBuf.readCompoundTag();

			int n = tweakedBuf.readerIndex();
			buf.readBytes(Math.max(0, n - 2));

			return nbt;
			//#endif
		}
		else if (NbtFormat.CURRENT == NbtFormat.NEW && bufNbtFormat == NbtFormat.OLD)
		{
			// I'm >= mc1.20.2 (NEW), trying to read a nbt in OLD style

			int prevReaderIndex = buf.readerIndex();
			PacketByteBuf tweakedBuf = new PacketByteBuf(Unpooled.buffer());
			tweakedBuf.writeByte(buf.readByte());  // 0x0A, tag type
			buf.readBytes(2);  // consume the 2* 0x00
			tweakedBuf.writeBytes(buf);
			buf.readerIndex(prevReaderIndex);

			CompoundTag nbt = tweakedBuf.readCompoundTag();

			int n = tweakedBuf.readerIndex();
			buf.readBytes(Math.max(0, n > 1 ? n + 2 : n));
			return nbt;
		}

		// direct read
		return buf.readCompoundTag();
	}
}
