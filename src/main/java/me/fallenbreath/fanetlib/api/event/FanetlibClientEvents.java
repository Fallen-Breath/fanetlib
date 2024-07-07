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

package me.fallenbreath.fanetlib.api.event;

import me.fallenbreath.fanetlib.impl.event.FanetlibClientEventsRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class FanetlibClientEvents
{
	/**
	 * Hook at the end of {@link net.minecraft.client.network.ClientPlayNetworkHandler#onGameJoin}
	 */
	public static void registerGameJoinListener(GameJoinCallback callback)
	{
		FanetlibClientEventsRegistry.getInstance().registerGameJoinListener(callback);
	}

	/**
	 * Hook at the end of {@link net.minecraft.client.network.ClientPlayNetworkHandler#onPlayerRespawn}
	 */
	public static void registerPlayerRespawnListener(PlayerRespawnCallback callback)
	{
		FanetlibClientEventsRegistry.getInstance().registerPlayerRespawnListener(callback);
	}

	@FunctionalInterface
	public interface GameJoinCallback
	{
		void onGameJoin(MinecraftClient client, ClientPlayNetworkHandler networkHandler);
	}

	@FunctionalInterface
	public interface PlayerRespawnCallback
	{
		void onPlayerRespawn(MinecraftClient client, ClientPlayNetworkHandler networkHandler);
	}
}
