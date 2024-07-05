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

package me.fallenbreath.fanetlib.impl;

import com.google.common.collect.Maps;
import me.fallenbreath.fanetlib.api.PacketCodec;
import me.fallenbreath.fanetlib.api.handler.C2SPacketHandler;
import me.fallenbreath.fanetlib.api.handler.S2CPacketHandler;
import net.minecraft.util.Identifier;

import java.util.Map;

public class FanetlibRegistry<Handler>
{
	public static final FanetlibRegistry<C2SPacketHandler<?>> C2S_PLAY = new FanetlibRegistry<>();
	public static final FanetlibRegistry<S2CPacketHandler<?>> S2C_PLAY = new FanetlibRegistry<>();

	private final Map<Identifier, RegistryEntry<?, Handler>> registry = Maps.newHashMap();

	public <P> void register(Identifier id, PacketCodec<P> codec, Handler handler)
	{
		this.registry.put(id, new RegistryEntry<>(codec, handler));
	}

	public RegistryEntry<?, Handler> getEntry(Identifier id)
	{
		return this.registry.get(id);
	}

	public Map<Identifier, RegistryEntry<?, Handler>> getRegistry()
	{
		return this.registry;
	}
}
