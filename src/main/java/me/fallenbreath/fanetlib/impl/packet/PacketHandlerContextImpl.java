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

import me.fallenbreath.fanetlib.api.packet.PacketHandlerC2S;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerS2C;
import me.fallenbreath.fanetlib.mixins.access.ClientPlayNetworkHandlerAccessor;
import me.fallenbreath.fanetlib.mixins.access.ServerPlayNetworkHandlerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;

public class PacketHandlerContextImpl
{
	public static class S2C implements PacketHandlerS2C.Context
	{
		private final ClientPacketListener networkHandler;
		private final Minecraft client;
		private final LocalPlayer player;

		public S2C(ClientPacketListener networkHandler)
		{
			this.networkHandler = networkHandler;
			this.client = ((ClientPlayNetworkHandlerAccessor)networkHandler).getMinecraft();
			this.player = this.client.player;
		}

		@Override
		public Minecraft getClient()
		{
			return this.client;
		}

		@Override
		public ClientPacketListener getNetworkHandler()
		{
			return this.networkHandler;
		}

		@Override
		@Nullable
		public LocalPlayer getPlayer()
		{
			return this.player;
		}
	}

	public static class C2S implements PacketHandlerC2S.Context
	{
		private final ServerGamePacketListenerImpl networkHandler;
		private final MinecraftServer server;
		private final ServerPlayer player;

		public C2S(ServerGamePacketListenerImpl networkHandler)
		{
			this.networkHandler = networkHandler;
			this.server = ((ServerPlayNetworkHandlerAccessor)networkHandler).getServer();
			this.player = this.networkHandler.player;
		}

		@Override
		public MinecraftServer getServer()
		{
			return this.server;
		}

		@Override
		public ServerGamePacketListenerImpl getNetworkHandler()
		{
			return this.networkHandler;
		}

		@Override
		public ServerPlayer getPlayer()
		{
			return this.player;
		}

		public void runSynced(Runnable runnable)
		{
			this.server.execute(runnable);
		}
	}
}
