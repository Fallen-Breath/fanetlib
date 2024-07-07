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

package me.fallenbreath.fanetlib.api.nbt;

import me.fallenbreath.fanetlib.impl.NetworkNbtUtilsImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import org.jetbrains.annotations.NotNull;

public class NetworkNbtUtils
{
	/**
	 * The next element inside the buffer should be a serialized nbt
	 */
	public static NbtFormat guessNbtFormat(PacketByteBuf buf)
	{
		return NetworkNbtUtilsImpl.guessNbtFormat(buf);
	}

	/**
	 * Read an NBT from a {@link PacketByteBuf} with best-effort nbt format detection
	 * <p>
	 * Compatible with both mc >= 1.20.2 and mc < 1.20.2 formats
	 */
	@NotNull
	public static CompoundTag readNbtAuto(PacketByteBuf buf)
	{
		return NetworkNbtUtilsImpl.readNbtAuto(buf);
	}

	/**
	 * Write nbt with current serialization style appended
	 */
	public static void writeNbtWithFormat(PacketByteBuf buf, CompoundTag nbt)
	{
		NetworkNbtUtilsImpl.writeNbtWithFormat(buf, nbt);
	}

	/**
	 * Read nbt with current serialization style appended
	 */
	@NotNull
	public static CompoundTag readNbtWithFormat(PacketByteBuf buf)
	{
		return NetworkNbtUtilsImpl.readNbtWithFormat(buf);
	}
}
