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

package me.fallenbreath.fanetlib.impl.event;

import com.google.common.collect.Lists;
import me.fallenbreath.fanetlib.api.event.FanetlibServerEvents.PlayerDisconnectCallback;
import me.fallenbreath.fanetlib.api.event.FanetlibServerEvents.PlayerJoinCallback;
import me.fallenbreath.fanetlib.mixins.access.ServerPlayNetworkHandlerAccessor;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.List;

public class FanetlibServerEventsRegistry
{
	private static final FanetlibServerEventsRegistry INSTANCE = new FanetlibServerEventsRegistry();

	private final List<PlayerJoinCallback> playerJoinCallbacks = Lists.newArrayList();
	private final List<PlayerDisconnectCallback> playerDisconnectCallbacks = Lists.newArrayList();

	public static FanetlibServerEventsRegistry getInstance()
	{
		return INSTANCE;
	}

	public void registerPlayerJoinListener(PlayerJoinCallback callback)
	{
		this.playerJoinCallbacks.add(callback);
	}

	public void dispatchPlayerJoinEvent(ServerPlayNetworkHandler networkHandler)
	{
		for (PlayerJoinCallback callback : this.playerJoinCallbacks)
		{
			callback.onPlayerJoin(((ServerPlayNetworkHandlerAccessor)networkHandler).getServer(), networkHandler, networkHandler.player);
		}
	}

	public void registerPlayerDisconnectListener(PlayerDisconnectCallback callback)
	{
		this.playerDisconnectCallbacks.add(callback);
	}

	public void dispatchPlayerDisconnectEvent(ServerPlayNetworkHandler networkHandler)
	{
		for (PlayerDisconnectCallback callback : this.playerDisconnectCallbacks)
		{
			callback.onPlayerDisconnect(((ServerPlayNetworkHandlerAccessor)networkHandler).getServer(), networkHandler, networkHandler.player);
		}
	}
}
