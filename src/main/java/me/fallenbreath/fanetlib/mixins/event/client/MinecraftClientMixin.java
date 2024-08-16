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

package me.fallenbreath.fanetlib.mixins.event.client;

import me.fallenbreath.fanetlib.impl.event.FanetlibClientEventsRegistry;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@Inject(
			//#if MC >= 12005
			//$$ method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V",
			//#else
			method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V",
			//#endif
			at = @At("TAIL")
	)
	private void onClientDisconnectHook(CallbackInfo ci)
	{
		FanetlibClientEventsRegistry.getInstance().dispatchDisconnectEvent((MinecraftClient)(Object)this);
	}
}
