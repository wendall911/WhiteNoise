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

package test.config;

import net.neoforged.fml.common.Mod;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigLoader;
import technology.roughness.whitenoise.config.WhiteNoiseTestConfig;

@Mod("whitenoisetest")
public class WhiteNoiseNeoForgeTestMod {

    public WhiteNoiseNeoForgeTestMod() {
        WhiteNoiseConfig clientConfig = WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.CLIENT, WhiteNoiseTestConfig.CLIENT_SPEC,
                "whitenoisetest");
        WhiteNoiseConfig commonConfig = WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.COMMON, WhiteNoiseTestConfig.COMMON_SPEC,
            "whitenoisetest");
        WhiteNoiseConfig serverConfig = WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.SERVER, WhiteNoiseTestConfig.SERVER_SPEC,
            "whitenoisetest");

        clientConfig.addLoadListener((config, isReloading) -> WhiteNoiseTestConfig.init(isReloading));
        commonConfig.addStartupListener(config -> WhiteNoiseTestConfig.commonStartup());
        commonConfig.addStartupListener(config -> WhiteNoiseTestConfig.serverStartup());
    }

}
