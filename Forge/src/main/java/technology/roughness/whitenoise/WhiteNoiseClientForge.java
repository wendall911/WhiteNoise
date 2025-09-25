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

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.event.ToolTipEventListener;

public class WhiteNoiseClientForge {

    public static void setup() {
        MinecraftForge.EVENT_BUS.addListener(WhiteNoiseClientForge::onPlayerLoggedOut);
        MinecraftForge.EVENT_BUS.register(ToolTipEventListener.class);
    }

    private static void onPlayerLoggedOut(final ClientPlayerNetworkEvent.LoggedOutEvent evt) {
        if (!Minecraft.getInstance().isLocalServer()) {
            WhiteNoiseConfigEvents.onUnloadServer();
        }
    }

}
