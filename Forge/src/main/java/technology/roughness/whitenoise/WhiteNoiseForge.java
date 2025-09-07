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

import io.netty.buffer.Unpooled;

import java.util.List;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.NewRegistryEvent;

import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.config.WhiteNoiseConfigNetwork;
import technology.roughness.whitenoise.network.ConfigSyncPacket;
import technology.roughness.whitenoise.network.WhiteNoiseForgePacketHandler;

@Mod(WhiteNoise.MODID)
public class WhiteNoiseForge {

    public WhiteNoiseForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopped);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        eventBus.addListener(this::loadConfigs);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);
    }

    private void loadConfigs(final NewRegistryEvent evt) {
        WhiteNoiseConfigEvents.onLoadDefaultAndLocal();
    }

    private void setup(final FMLCommonSetupEvent evt) {
        WhiteNoiseForgePacketHandler.setup();
    }

    private void clientSetup(final FMLClientSetupEvent evt) {
        WhiteNoiseClientForge.setup();
    }

    private void onServerAboutToStart(final ServerAboutToStartEvent evt) {
        WhiteNoiseConfigEvents.onLoadServer(evt.getServer());
    }

    private void onServerStopped(final ServerStoppedEvent evt) {
        WhiteNoiseConfigEvents.onUnloadServer();
    }

    private void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent evt) {
        if (evt.getEntity() instanceof ServerPlayer serverPlayer) {
            List<FriendlyByteBuf> configData = WhiteNoiseConfigNetwork.getConfigSync();

            if (!configData.isEmpty()) {
                for (FriendlyByteBuf configDatum : configData) {
                    WhiteNoiseForgePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new ConfigSyncPacket(configDatum));
                }
                WhiteNoiseForgePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new ConfigSyncPacket(new FriendlyByteBuf(Unpooled.buffer())));
            }
        }
    }

}

