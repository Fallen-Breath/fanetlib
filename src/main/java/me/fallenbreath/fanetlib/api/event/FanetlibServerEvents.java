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

import me.fallenbreath.fanetlib.impl.event.FanetlibServerEventsRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FanetlibServerEvents
{
	/**
	 * Hook at the end of {@link net.minecraft.server.PlayerManager#onPlayerConnect}
	 */
	public static void registerPlayerJoinListener(PlayerJoinCallback callback)
	{
		FanetlibServerEventsRegistry.getInstance().registerPlayerJoinListener(callback);
	}

	/**
	 * Hook at the end of {@link net.minecraft.server.network.ServerGamePacketListenerImpl#onDisconnected}
	 */
	public static void registerPlayerDisconnectListener(PlayerDisconnectCallback callback)
	{
		FanetlibServerEventsRegistry.getInstance().registerPlayerDisconnectListener(callback);
	}

	@FunctionalInterface
	public interface PlayerJoinCallback
	{
		void onPlayerJoin(MinecraftServer server, ServerGamePacketListenerImpl networkHandler, ServerPlayer player);
	}

	@FunctionalInterface
	public interface PlayerDisconnectCallback
	{
		void onPlayerDisconnect(MinecraftServer server, ServerGamePacketListenerImpl networkHandler, ServerPlayer player);
	}
}
