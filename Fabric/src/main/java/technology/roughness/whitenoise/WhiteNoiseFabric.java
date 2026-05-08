/*
 * Derived from Spectrelib
 * https://github.com/illusivesoulworks/spectrelib
 * Copyright (C) 2022 Illusive Soulworks
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; you
 * may only use version 2.1 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <https://www.gnu.org/licenses/>.
 */

package technology.roughness.whitenoise;

import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;

import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.config.WhiteNoiseConfigNetwork;
import technology.roughness.whitenoise.config.WhiteNoiseConfigPayload;

public class WhiteNoiseFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.clientboundPlay()
                .register(WhiteNoiseConfigPayload.TYPE, WhiteNoiseConfigPayload.STREAM_CODEC);
        ServerLifecycleEvents.SERVER_STARTING.register(WhiteNoiseConfigEvents::onLoadServer);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> WhiteNoiseConfigEvents.onUnloadServer());
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer serverPlayer = handler.getPlayer();
            List<WhiteNoiseConfigPayload> configData = WhiteNoiseConfigNetwork.getServerConfigSync();

            if (!configData.isEmpty()) {
                for (WhiteNoiseConfigPayload configDatum : configData) {
                    ServerPlayNetworking.send(serverPlayer, configDatum);
                }
            }
        });
    }

}

