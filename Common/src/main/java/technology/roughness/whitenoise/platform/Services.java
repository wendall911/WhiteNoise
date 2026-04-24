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

package technology.roughness.whitenoise.platform;

import technology.roughness.whitenoise.WhiteNoise;
import technology.roughness.whitenoise.platform.services.IClientPlatform;
import technology.roughness.whitenoise.platform.services.IConfigHelper;
import technology.roughness.whitenoise.platform.services.IPlatform;

public class Services extends ServicesBase {

    public static final IConfigHelper CONFIG = load(WhiteNoise.LOGGER, IConfigHelper.class);
    public static final IClientPlatform CLIENT_PLATFORM = load(WhiteNoise.LOGGER, IClientPlatform.class);
    public static final IPlatform WN_PLATFORM = load(WhiteNoise.LOGGER, IPlatform.class);
    public static final IPlatform PLATFORM = WN_PLATFORM;

}

