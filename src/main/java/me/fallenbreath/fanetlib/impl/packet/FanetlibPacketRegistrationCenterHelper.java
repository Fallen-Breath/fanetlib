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

import me.fallenbreath.fanetlib.api.packet.FanetlibPacketRegistrationCenter;
import me.fallenbreath.fanetlib.impl.utils.CallOnce;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings({"Convert2MethodRef", "WriteOnlyObject"})
public class FanetlibPacketRegistrationCenterHelper
{
	private static final CallOnce commonInvoker = new CallOnce(() -> FanetlibPacketRegistrationCenter.common());
	private static final CallOnce c2sInvoker = new CallOnce(() -> FanetlibPacketRegistrationCenter.c2s());
	private static final CallOnce s2cInvoker = new CallOnce(() -> FanetlibPacketRegistrationCenter.s2c());
	private static final AtomicBoolean hasInvokedS2C = new AtomicBoolean(false);
	private static final AtomicBoolean hasInvokedC2S = new AtomicBoolean(false);

	public static void invokeC2S()
	{
		commonInvoker.run();
		c2sInvoker.run();
		hasInvokedC2S.set(true);
	}

	public static void invokeS2C()
	{
		commonInvoker.run();
		s2cInvoker.run();
		hasInvokedS2C.set(true);
	}

	public static boolean isTooLateForC2SRegister()
	{
		//#if MC >= 12002
		//$$ return hasInvokedC2S.get();
		//#else
		return false;
		//#endif
	}

	public static boolean isTooLateForS2CRegister()
	{
		//#if MC >= 12002
		//$$ return hasInvokedS2C.get();
		//#else
		return false;
		//#endif
	}
}
