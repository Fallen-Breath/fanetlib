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

package me.fallenbreath.fanetlib.mixins.register;

import me.fallenbreath.fanetlib.impl.packet.FanetlibCustomPayload;
import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistrationCenterHelper;
import me.fallenbreath.fanetlib.impl.packet.FanetlibPacketRegistry;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// used in mc >= 1.20.5
@Mixin(CustomPayloadS2CPacket.class)
public abstract class CustomPayloadS2CPacketMixin
{
	@SuppressWarnings({"rawtypes", "unchecked"})
	@ModifyArg(
			method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/packet/CustomPayload;createCodec(Lnet/minecraft/network/packet/CustomPayload$CodecFactory;Ljava/util/List;)Lnet/minecraft/network/codec/PacketCodec;"
			)
	)
	private static List<?> registerFanetlibPayload_s2c(List<CustomPayload.Type<?, ?>> types)
	{
		FanetlibPacketRegistrationCenterHelper.collectS2C();
		var newTypes = new ArrayList<>(types);
		FanetlibPacketRegistry.S2C_PLAY.getRegistry().forEach((id, entry) -> {
			newTypes.add(new CustomPayload.Type<>(
					new CustomPayload.Id<FanetlibCustomPayload<?>>(id.getIdentifier()),
					CustomPayload.codecOf(
							FanetlibCustomPayload::write,
							buf -> new FanetlibCustomPayload(id, entry.getCodec(), buf)
					)
			));
		});
		return Collections.unmodifiableList(newTypes);
	}
}
