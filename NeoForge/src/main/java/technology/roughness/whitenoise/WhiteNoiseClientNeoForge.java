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

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import technology.roughness.whitenoise.common.WhiteNoiseModContainer;
import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.config.WhiteNoiseConfigTracker;
import technology.roughness.whitenoise.config.client.screen.ConfigurationScreen;
import technology.roughness.whitenoise.event.ToolTipEventListener;

public class WhiteNoiseClientNeoForge {

    public static void setup() {
        NeoForge.EVENT_BUS.addListener(WhiteNoiseClientNeoForge::onPlayerLoggedOut);
        NeoForge.EVENT_BUS.register(ToolTipEventListener.class);

        ModList.get().forEachModContainer((modId, modContainer) -> {
            Map<String, Map<WhiteNoiseConfig.Type, Set<WhiteNoiseConfig>>> configs =
                    WhiteNoiseConfigTracker.INSTANCE.getConfigsByMod();
            Map<WhiteNoiseConfig.Type, Set<WhiteNoiseConfig>> modConfigs = configs.get(modId);

            WhiteNoiseModContainer wnModContainer = new WhiteNoiseModContainer(
                modId,
                modContainer.getModInfo().getDisplayName(),
                modConfigs
            );

            if (modConfigs != null && !modConfigs.isEmpty()) {
                int count = modConfigs.values().stream().mapToInt(Set::size).sum();
                WhiteNoise.LOGGER.info("Registering config screens for mod {} with {} config(s)", modId, count);
                modContainer.registerExtensionPoint(IConfigScreenFactory.class, new IConfigScreenFactory() {
                    @NotNull
                    @Override
                    public Screen createScreen(@NotNull ModContainer modContainer1, @NotNull Screen screen) {
                        return new ConfigurationScreen(wnModContainer, screen);
                    }
                });
            }
        });
    }

    private static void onPlayerLoggedOut(final ClientPlayerNetworkEvent.LoggingOut evt) {
        if (!Minecraft.getInstance().isLocalServer()) {
            WhiteNoiseConfigEvents.onUnloadServer();
        }
    }

}
