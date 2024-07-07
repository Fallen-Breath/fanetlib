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
import io.netty.buffer.Unpooled;
import me.fallenbreath.fanetlib.api.PacketCodec;
import me.fallenbreath.fanetlib.api.PacketHandlerC2S;
import me.fallenbreath.fanetlib.api.PacketHandlerS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.Map;

public class FanetlibRegistry<Handler, McPacket>
{
	public static final FanetlibRegistry<PacketHandlerC2S<?>, CustomPayloadC2SPacket> C2S_PLAY = new FanetlibRegistry<>(Direction.C2S);
	public static final FanetlibRegistry<PacketHandlerS2C<?>, CustomPayloadS2CPacket> S2C_PLAY = new FanetlibRegistry<>(Direction.S2C);

	private final Direction direction;
	private final Map<Identifier, RegistryEntry<?, Handler>> registry = Maps.newHashMap();

	private FanetlibRegistry(Direction direction)
	{
		this.direction = direction;
	}

	public <P> void register(Identifier id, PacketCodec<P> codec, Handler handler)
	{
		if (this.registry.containsKey(id))
		{
			throw new IllegalArgumentException(String.format("Duplicate packet id: %s", id));
		}
		this.registry.put(id, new RegistryEntry<>(codec, handler));
	}

	public RegistryEntry<?, Handler> getEntry(Identifier id)
	{
		return this.registry.get(id);
	}

	@SuppressWarnings("unused")
	public Map<Identifier, RegistryEntry<?, Handler>> getRegistry()
	{
		return this.registry;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public <P> McPacket createPacket(Identifier id, P packet)
	{
		RegistryEntry<?, Handler> entry = this.getEntry(id);
		if (entry == null)
		{
			throw new IllegalArgumentException(String.format("Unknown packet id: %s", id));
		}

		PacketCodec codec = entry.getCodec();
		FanetlibCustomPayload<P> fcp = new FanetlibCustomPayload<P>(id, codec, packet);

		//#if MC < 12002
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		fcp.write(buf);
		//#endif

		if (this.direction == Direction.C2S)
		{
			EnvType envType = FabricLoader.getInstance().getEnvironmentType();
			if (envType != EnvType.CLIENT)
			{
				throw new RuntimeException("Cannot send C2S packet in non-client, current env: " + envType);
			}
			return (McPacket)new CustomPayloadC2SPacket(
					//#if MC >= 12002
					//$$ fcp
					//#else
					id, buf
					//#endif
			);
		}
		else
		{
			return (McPacket)new CustomPayloadS2CPacket(
					//#if MC >= 12002
					//$$ fcp
					//#else
					id, buf
					//#endif
			);
		}
	}

	private enum Direction
	{
		C2S,
		S2C
	}
}
