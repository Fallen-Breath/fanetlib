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

/**
 * See <a href="https://wiki.vg/NBT">https://wiki.vg/NBT</a>
 * for the nbt changes between mc < 1.20.2 and mc >= 1.20.2
 */
public enum NbtFormat
{
	UNKNOWN,
	OLD,  // <  1.20.2
	NEW;  // >= 1.20.2

	public static final NbtFormat CURRENT =
			//#if MC >= 12002
			//$$ NEW;
			//#else
			OLD;
			//#endif
}
