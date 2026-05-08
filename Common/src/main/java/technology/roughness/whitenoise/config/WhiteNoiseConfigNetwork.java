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

package technology.roughness.whitenoise.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class WhiteNoiseConfigNetwork {

    public static List<WhiteNoiseConfigPayload> getServerConfigSync() {
        Map<String, byte[]> configData = WhiteNoiseConfigTracker.INSTANCE.getServerConfigSync();

        if (configData.isEmpty()) {
            return new ArrayList<>();
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeMap(configData, FriendlyByteBuf::writeUtf, (k, v) -> k.writeByteArray(v));
        return configData.entrySet().stream()
                .map(e -> new WhiteNoiseConfigPayload(e.getValue(), e.getKey()))
                .toList();
    }

    public static void acceptSyncedConfigs(byte[] data, String fileName) {
        if (!Minecraft.getInstance().isLocalServer()) {
            WhiteNoiseConfigTracker.INSTANCE.acceptSyncedConfigs(fileName, data);
        }
    }

}

