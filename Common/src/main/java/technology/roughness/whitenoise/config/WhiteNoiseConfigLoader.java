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

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class WhiteNoiseConfigLoader {

    public static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");

    public static WhiteNoiseConfig add(WhiteNoiseConfig.Type type, WhiteNoiseConfigSpec spec,
            String modId, String fileName) {
        WhiteNoiseConfig config = new WhiteNoiseConfig(type, spec, modId, fileName);

        WhiteNoiseConfigTracker.INSTANCE.track(config);

        return config;
    }

    public static WhiteNoiseConfig add(WhiteNoiseConfig.Type type, WhiteNoiseConfigSpec spec,
            String modId) {
        WhiteNoiseConfig config = new WhiteNoiseConfig(type, spec, modId);

        WhiteNoiseConfigTracker.INSTANCE.track(config);

        return config;
    }

}

