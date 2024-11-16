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

/**
 * In MC >= 1.20.2, the current fanetlib design can only submit packet registrations to Minecraft
 * during the initialization of vanilla classes CustomPayloadC2SPacket and CustomPayloadS2CPacket.
 * This is the only chance, so we need to ensure that all packet registrations are collected
 * before submitting them to Minecraft
 * <p>
 * A solution is to mixin into the following methods, utilizing them as the opportunity for registering your packets.
 * These methods will be called by fanetlib before related packets are about to be submitted to Minecraft.
 * <p>
 * Example mixin:
 * <pre>
 * {@code
 * @Mixin(FanetlibPacketRegistrationCenter.class)
 * public abstract class FanetlibPacketRegistrationCenterMixin
 * {
 *     @Inject(method = "common", at = @At("HEAD"), remap = false)
 *     private static void register(CallbackInfo ci)
 *     {
 *         MyMod.registerMyPacketsToFanetlib();
 *     }
 * }
 * }
 * </pre>
 */
public class FanetlibPacketRegistrationCenter
{
	/**
	 * Fanetlib ensures that it will be called before submitting C2S or S2C packet registrations to Minecraft
	 */
	public static void common()
	{
	}

	/**
	 * Fanetlib ensures that it will be called before submitting C2S packet registrations to Minecraft
	 */
	public static void c2s()
	{
	}

	/**
	 * Fanetlib ensures that it will be called before submitting S2C packet registrations to Minecraft
	 */
	public static void s2c()
	{
	}
}
