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

import java.io.File;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import net.minecraft.client.main.GameConfig;

import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;
import technology.roughness.whitenoise.config.WhiteNoiseConfigNetwork;
import technology.roughness.whitenoise.config.WhiteNoiseConfigPayload;
import technology.roughness.whitenoise.platform.FabricConfigHelper;

public class WhiteNoiseClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (!client.isLocalServer()) {
                WhiteNoiseConfigEvents.onUnloadServer();
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(WhiteNoiseConfigPayload.TYPE,
            (payload, context) -> {
                byte[] contents = payload.contents;
                String fileName = payload.fileName;
                context.client()
                    .execute(() -> WhiteNoiseConfigNetwork.acceptSyncedConfigs(contents, fileName));
            });
    }

    public static void prepareConfigs(GameConfig gameConfig) {
        File file = gameConfig.location.gameDirectory;
        FabricConfigHelper.gameDir = file.toPath();

        EntrypointUtils.invokeEntrypoints("whitenoise-config", WhiteNoiseConfigInitializer.class,
            WhiteNoiseConfigInitializer::onInitializeConfig);
        WhiteNoiseConfigEvents.onLoadGlobal();
    }

}

