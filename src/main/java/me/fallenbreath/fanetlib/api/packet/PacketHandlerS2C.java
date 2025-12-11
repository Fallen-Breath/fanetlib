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

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PacketHandlerS2C<P>
{
	void handle(P packet, Context context);

	static <P> PacketHandlerS2C<P> dummy()
	{
		return (packet, context) -> {};
	}

	interface Context
	{
		Minecraft getClient();

		ClientPacketListener getNetworkHandler();

		@Nullable
		LocalPlayer getPlayer();
	}
}
