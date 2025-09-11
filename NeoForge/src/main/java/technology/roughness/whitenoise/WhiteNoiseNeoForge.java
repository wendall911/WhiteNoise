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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.config.WhiteNoiseConfigNetwork;
import technology.roughness.whitenoise.config.WhiteNoiseConfigPayload;
import technology.roughness.whitenoise.network.WhiteNoiseClientPayloadHandler;

@Mod(WhiteNoise.MODID)
public class WhiteNoiseNeoForge {

    public WhiteNoiseNeoForge(IEventBus eventBus) {
        NeoForge.EVENT_BUS.addListener(this::onServerAboutToStart);
        NeoForge.EVENT_BUS.addListener(this::onServerStopped);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        eventBus.addListener(this::loadConfigs);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::registerPayloadHandler);
    }

    private void registerPayloadHandler(final RegisterPayloadHandlersEvent evt) {
        evt.registrar(WhiteNoise.MODID)
            .playToClient(WhiteNoiseConfigPayload.TYPE, WhiteNoiseConfigPayload.STREAM_CODEC,
                WhiteNoiseClientPayloadHandler.getInstance()::handleData);
    }

    private void loadConfigs(final NewRegistryEvent evt) {
        WhiteNoiseConfigEvents.onLoadGlobal();
    }

    private void clientSetup(final FMLClientSetupEvent evt) {
        WhiteNoiseClientNeoForge.setup();
    }

    private void onServerAboutToStart(final ServerAboutToStartEvent evt) {
        WhiteNoiseConfigEvents.onLoadServer(evt.getServer());
    }

    private void onServerStopped(final ServerStoppedEvent evt) {
        WhiteNoiseConfigEvents.onUnloadServer();
    }

    private void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent evt) {
        Player player = evt.getEntity();

        if (player instanceof ServerPlayer serverPlayer) {
            List<WhiteNoiseConfigPayload> configData = WhiteNoiseConfigNetwork.getConfigSync();

            if (!configData.isEmpty()) {
                for (WhiteNoiseConfigPayload configDatum : configData) {
                    PacketDistributor.sendToPlayer(serverPlayer,
                        new WhiteNoiseConfigPayload(configDatum.contents, configDatum.fileName));
                }
            }
        }
    }

}
