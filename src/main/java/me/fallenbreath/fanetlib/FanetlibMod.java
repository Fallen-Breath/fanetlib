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

package me.fallenbreath.fanetlib;

import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistrationCenterHelper;
import net.fabricmc.api.ModInitializer;

//#if MC >= 11800
//$$ import com.mojang.logging.LogUtils;
//$$ import org.slf4j.Logger;
//#else
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//#endif

public class FanetlibMod implements ModInitializer
{
	public static final Logger LOGGER =
			//#if MC >= 11800
			//$$ LogUtils.getLogger();
			//#else
			LogManager.getLogger();
			//#endif

	@Override
	public void onInitialize()
	{
		// For MC < 1.20.2, or in case of those vanilla custom payload classes are not loaded
		FanetlibPacketRegistrationCenterHelper.collectAll();
	}
}
