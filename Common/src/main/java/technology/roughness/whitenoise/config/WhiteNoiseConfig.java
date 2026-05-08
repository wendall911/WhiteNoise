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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jspecify.annotations.NonNull;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;

public class WhiteNoiseConfig {

    private final Type type;
    private final WhiteNoiseConfigSpec spec;
    private final String fileName;
    private final String modId;
    private final EnumMap<InstanceType, CommentedConfig> configData =
            new EnumMap<>(InstanceType.class);
    private final List<BiConsumer<WhiteNoiseConfig, Boolean>> loadListeners = new ArrayList<>();
    private final List<Consumer<WhiteNoiseConfig>> startupListeners = new ArrayList<>();
    private boolean sync = true;

    public WhiteNoiseConfig(Type type, WhiteNoiseConfigSpec spec, String modId, String fileName) {
        this.type = type;
        this.spec = spec;
        this.modId = modId;
        this.fileName = fileName;
    }

    public WhiteNoiseConfig(Type type, WhiteNoiseConfigSpec spec, String modId) {
        this(type, spec, modId, getDefaultFileName(type, modId));
    }

    private static String getDefaultFileName(Type type, String modId) {
        return String.format(Locale.ROOT, "%s-%s.toml", modId, type.suffix());
    }

    public Type getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public WhiteNoiseConfigSpec getSpec() {
        return spec;
    }

    public String getModId() {
        return modId;
    }

    public CommentedConfig getConfigData(InstanceType type) {
        return this.configData.get(type);
    }

    public CommentedConfig getActiveConfigData() {
        if (this.configData.containsKey(InstanceType.SERVER)) {
            return this.getConfigData(InstanceType.SERVER);
        }

        return this.getConfigData(InstanceType.GLOBAL);
    }

    public void setConfigData(InstanceType type, @NonNull final CommentedConfig configData, boolean create) {
        this.configData.put(type, configData);
        this.getSpec().setConfigData(configData, create);
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean sync() {
        return this.sync;
    }

    public void clearServerConfigData() {
        this.configData.remove(InstanceType.SERVER);
        this.getSpec().setConfigData(this.getConfigData(InstanceType.GLOBAL), false);
    }

    public void save(InstanceType type) {
        if (this.configData.containsKey(type)) {
            CommentedConfig config = this.getConfigData(type);

            if (config instanceof CommentedFileConfig fileConfig) {
                fileConfig.save();
            }
        }
    }

    public Path getFullPath() {
        return ((CommentedFileConfig) this.getActiveConfigData()).getNioPath();
    }

    public void addLoadListener(BiConsumer<WhiteNoiseConfig, Boolean> listener) {
        this.loadListeners.add(listener);
    }

    public void addStartupListener(Consumer<WhiteNoiseConfig> listener) {
        this.startupListeners.add(listener);
    }

    public void fireStartupEvent() {
        for (Consumer<WhiteNoiseConfig> listener : this.startupListeners) {
            listener.accept(this);
        }
    }

    public void fireLoad(boolean isReloading) {
        for (BiConsumer<WhiteNoiseConfig, Boolean> loadListener : this.loadListeners) {
            loadListener.accept(this, isReloading);
        }
    }

    public enum Type {
        COMMON,
        CLIENT,
        SERVER,
        STARTUP;

        public String suffix() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public enum InstanceType {
        GLOBAL,
        SERVER;

        public String id() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

}

