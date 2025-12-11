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

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerC2S;
import me.fallenbreath.fanetlib.api.packet.PacketHandlerS2C;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

import java.util.Map;

public class FanetlibPacketRegistry<Handler, McPacket>
{
	public static final FanetlibPacketRegistry<PacketHandlerC2S<?>, ServerboundCustomPayloadPacket> C2S_PLAY = new FanetlibPacketRegistry<>(Direction.C2S);
	public static final FanetlibPacketRegistry<PacketHandlerS2C<?>, ClientboundCustomPayloadPacket> S2C_PLAY = new FanetlibPacketRegistry<>(Direction.S2C);

	private final Direction direction;
	private final Map<PacketId<?>, RegistryEntry<?, Handler>> registry = Maps.newHashMap();

	private FanetlibPacketRegistry(Direction direction)
	{
		this.direction = direction;
	}

	public <P> void register(PacketId<P> id, PacketCodec<P> codec, Handler handler)
	{
		if (this.registry.containsKey(id))
		{
			throw new IllegalArgumentException(String.format("Duplicate packet id: %s", id));
		}
		this.registry.put(id, new RegistryEntry<>(codec, handler));
	}

	@SuppressWarnings("unchecked")
	public <P> RegistryEntry<P, Handler> getEntry(PacketId<P> id)
	{
		return (RegistryEntry<P, Handler>)this.registry.get(id);
	}

	@SuppressWarnings("unused")
	public Map<PacketId<?>, RegistryEntry<?, Handler>> getRegistry()
	{
		return this.registry;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public <P> McPacket createPacket(PacketId<P> id, P packet)
	{
		RegistryEntry<?, Handler> entry = this.getEntry(id);
		if (entry == null)
		{
			throw new IllegalArgumentException(String.format("Unknown packet id: %s", id));
		}

		PacketCodec codec = entry.getCodec();
		FanetlibCustomPayload<P> fcp = new FanetlibCustomPayload<P>(id, codec, packet);

		//#if MC < 12002
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		fcp.write(buf);
		//#endif

		if (this.direction == Direction.C2S)
		{
			EnvType envType = FabricLoader.getInstance().getEnvironmentType();
			if (envType != EnvType.CLIENT)
			{
				throw new RuntimeException("Cannot send C2S packet in non-client, current env: " + envType);
			}
			return (McPacket)new ServerboundCustomPayloadPacket(
					//#if MC >= 12002
					//$$ fcp
					//#else
					id.getIdentifier(), buf
					//#endif
			);
		}
		else
		{
			return (McPacket)new ClientboundCustomPayloadPacket(
					//#if MC >= 12002
					//$$ fcp
					//#else
					id.getIdentifier(), buf
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
