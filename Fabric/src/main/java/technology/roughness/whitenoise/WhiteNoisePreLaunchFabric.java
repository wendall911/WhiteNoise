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

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import technology.roughness.whitenoise.config.WhiteNoiseConfigEvents;
import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;

public class WhiteNoisePreLaunchFabric implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            EntrypointUtils.invokeEntrypoints("whitenoise-config", WhiteNoiseConfigInitializer.class,
                    WhiteNoiseConfigInitializer::onInitializeConfig);
            WhiteNoiseConfigEvents.onLoadGlobal();
        }
    }

}

