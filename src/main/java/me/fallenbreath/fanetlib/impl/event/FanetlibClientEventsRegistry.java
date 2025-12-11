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
import me.fallenbreath.fanetlib.api.event.FanetlibClientEvents.DisconnectCallback;
import me.fallenbreath.fanetlib.api.event.FanetlibClientEvents.GameJoinCallback;
import me.fallenbreath.fanetlib.api.event.FanetlibClientEvents.PlayerRespawnCallback;
import me.fallenbreath.fanetlib.mixins.access.ClientPlayNetworkHandlerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.List;

public class FanetlibClientEventsRegistry
{
	private static final FanetlibClientEventsRegistry INSTANCE = new FanetlibClientEventsRegistry();

	private final List<GameJoinCallback> gameJoinCallbacks = Lists.newArrayList();
	private final List<PlayerRespawnCallback> playerRespawnCallbacks = Lists.newArrayList();
	private final List<DisconnectCallback> disconnectCallbacks = Lists.newArrayList();

	public static FanetlibClientEventsRegistry getInstance()
	{
		return INSTANCE;
	}

	public void registerGameJoinListener(GameJoinCallback callback)
	{
		this.gameJoinCallbacks.add(callback);
	}

	public void dispatchGameJoinEvent(ClientPacketListener networkHandler)
	{
		for (GameJoinCallback callback : this.gameJoinCallbacks)
		{
			callback.onGameJoin(((ClientPlayNetworkHandlerAccessor)networkHandler).getClient(), networkHandler);
		}
	}

	public void registerPlayerRespawnListener(PlayerRespawnCallback callback)
	{
		this.playerRespawnCallbacks.add(callback);
	}

	public void dispatchPlayerRespawnEvent(ClientPacketListener networkHandler)
	{
		for (PlayerRespawnCallback callback : this.playerRespawnCallbacks)
		{
			callback.onPlayerRespawn(((ClientPlayNetworkHandlerAccessor)networkHandler).getClient(), networkHandler);
		}
	}

	public void registerDisconnectListener(DisconnectCallback callback)
	{
		this.disconnectCallbacks.add(callback);
	}

	public void dispatchDisconnectEvent(Minecraft client)
	{
		for (DisconnectCallback callback : this.disconnectCallbacks)
		{
			callback.onDisconnect(client);
		}
	}
}
