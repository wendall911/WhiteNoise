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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.electronwill.nightconfig.toml.TomlFormat;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.MinecraftServer;

import org.apache.commons.io.FilenameUtils;

import technology.roughness.whitenoise.WhiteNoise;
import technology.roughness.whitenoise.platform.Services;

import static technology.roughness.whitenoise.config.WhiteNoiseConfigLoader.CONFIG;

public class WhiteNoiseConfigTracker {

    public static final WhiteNoiseConfigTracker INSTANCE = new WhiteNoiseConfigTracker();

    private final ConcurrentHashMap<String, WhiteNoiseConfig> files = new ConcurrentHashMap<>();
    private final EnumMap<WhiteNoiseConfig.Type, Set<WhiteNoiseConfig>> configsByType =
            new EnumMap<>(WhiteNoiseConfig.Type.class);
    private final ConcurrentHashMap<String, Map<WhiteNoiseConfig.Type, Set<WhiteNoiseConfig>>>
            configsByMod = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> defaultConfigs = new ConcurrentHashMap<>();

    private WhiteNoiseConfigTracker() {
        for (WhiteNoiseConfig.Type value : WhiteNoiseConfig.Type.values()) {
            this.configsByType.put(value, Collections.synchronizedSet(new LinkedHashSet<>()));
        }
    }

    void track(final WhiteNoiseConfig config) {
        if (config.getType() == WhiteNoiseConfig.Type.CLIENT && Services.CONFIG.isDedicatedServer()) {
            return;
        }
        if (this.files.containsKey(config.getFileName())) {
            WhiteNoise.LOGGER.error(CONFIG, "Detected config file conflict {} between {} and {}",
                    config.getFileName(), this.files.get(config.getFileName()).getModId(), config.getModId());
            throw new RuntimeException("Conflicting config files");
        }
        this.files.put(config.getFileName(), config);
        this.configsByType.get(config.getType()).add(config);
        this.configsByMod.computeIfAbsent(config.getModId(),
                (k) -> new EnumMap<>(WhiteNoiseConfig.Type.class)).computeIfAbsent(config.getType(),
                (k) -> Collections.synchronizedSet(new LinkedHashSet<>())).add(config);
        WhiteNoise.LOGGER.debug(CONFIG, "Config file {} for {} added to tracking",
                config.getFileName(), config.getModId());
    }

    void loadDefaultConfigs() {
        WhiteNoise.LOGGER.debug(CONFIG, "Loading default configs");
        this.files.values().forEach(config -> {
            WhiteNoise.LOGGER.trace(CONFIG, "Loading config file type {} at {} for {}",
                    config.getType(), config.getFileName(), config.getModId());
            boolean alreadyExists =
                    Files.exists(Services.CONFIG.getDefaultConfigPath().resolve(config.getFileName()));
            final CommentedFileConfig configData =
                    read(Services.CONFIG.getDefaultConfigPath()).apply(config);
            WhiteNoiseConfig.InstanceType type = WhiteNoiseConfig.InstanceType.DEFAULT;
            config.setConfigData(type, configData, !alreadyExists);
            config.fireLoad();
            config.save(type);
        });
    }

    void loadLocalConfigs() {
        Path path = Services.CONFIG.getLocalConfigPath();

        if (!Files.isDirectory(path)) {
            try {
                Files.createDirectory(path);
            }
            catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    WhiteNoise.LOGGER.error(WhiteNoiseConfigLoader.CONFIG,
                            "Failed to create {} directory due to an intervening file", path);
                }
                else {
                    WhiteNoise.LOGGER.error(WhiteNoiseConfigLoader.CONFIG,
                            "Failed to create {} directory due to an unknown error", path, e);
                }

                throw new RuntimeException("Failed to create directory", e);
            }
        }
        else {
            WhiteNoise.LOGGER.debug(WhiteNoiseConfigLoader.CONFIG, "Found existing directory : {}", path);
        }

        WhiteNoiseConfig.InstanceType type = WhiteNoiseConfig.InstanceType.LOCAL;
        WhiteNoise.LOGGER.debug(CONFIG, "Loading {} configs from {}", type.id(), path);

        this.files.values().forEach(config -> {
            Path configPath = path.resolve(config.getFileName());

            if (Files.exists(configPath)) {
                WhiteNoise.LOGGER.trace(CONFIG, "Loading config file type {} at {} from {} for {}",
                        config.getType(), config.getFileName(), path, config.getModId());
                final CommentedFileConfig configData = read(path).apply(config);
                config.setConfigData(type, configData, false);
                config.fireLoad();
                config.save(type);
            }
        });
    }

    void loadServerConfigs(MinecraftServer server) {
        Path configDir = Services.CONFIG.getServerConfigPath(server);
        WhiteNoiseConfig.InstanceType type = WhiteNoiseConfig.InstanceType.SERVER;

        WhiteNoise.LOGGER.debug(CONFIG, "Loading {} configs from {}", type.id(), configDir);

        this.files.values().forEach(config -> {
            Path dir = null;
            String name = config.getFileName();
            Path configPath = configDir.resolve(name);

            if (Files.exists(configPath)) {
                dir = configDir;
            }
            else {
                Path localDir = Services.CONFIG.getLocalConfigPath();
                Path localPath = localDir.resolve(name);

                if (Files.exists(localPath)) {
                    dir = localDir;
                }
                else {
                    Path defaultDir = Services.CONFIG.getDefaultConfigPath();
                    Path defaultPath = defaultDir.resolve(name);

                    if (Files.exists(defaultPath)) {
                        dir = defaultDir;
                    }
                }
            }

            if (dir != null) {
                WhiteNoise.LOGGER.trace(CONFIG, "Loading config file type {} at {} from {} for {}",
                        config.getType(), config.getFileName(), dir, config.getModId());
                final CommentedFileConfig configData = read(dir).apply(config);

                config.setConfigData(type, configData, false);
                config.fireLoad();
                config.save(type);
            }
        });
    }

    void unloadServerConfigs() {
        WhiteNoise.LOGGER.debug(CONFIG, "Unloading server configs");

        this.files.values().forEach(config -> {
            WhiteNoise.LOGGER.trace(CONFIG, "Unloading config file type {} at {}", config.getType(),
                    config.getFileName());
            config.save(WhiteNoiseConfig.InstanceType.SERVER);
            config.clearServerConfigData();
        });
    }

    static void tryConfigFileLoad(FileConfig configData) {
        try {
            configData.load();
        }
        catch (ParsingException e) {
            try {
                Path path = configData.getNioPath();

                WhiteNoise.LOGGER.warn(CONFIG,
                        "Configuration file {} could not be parsed. Creating backup file and correcting", path);

                if (Files.exists(path)) {
                    createBackup(path);
                }
                Files.delete(path);
                configData.load();
                return;
            }
            catch (Throwable t) {
                e.addSuppressed(t);
            }

            throw e;
        }
    }

    private static void createBackup(Path commentedFileConfig) {
        Path path = commentedFileConfig.getParent();
        String name = commentedFileConfig.getFileName().toString();
        String fileName = FilenameUtils.removeExtension(name);
        String extension = FilenameUtils.getExtension(name) + ".bak";
        Path backup = path.resolve(fileName + "." + extension);

        try {
            Files.deleteIfExists(backup);
            Files.copy(commentedFileConfig, backup);
        }
        catch (IOException exception) {
            WhiteNoise.LOGGER.warn(CONFIG, "Failed to create backup file for {}", commentedFileConfig,
                    exception);
        }
    }

    private void tryDefaultConfigLoad(WhiteNoiseConfig modConfig) {
        String fileName = modConfig.getFileName();
        Path path = Services.CONFIG.getBackwardsCompatiblePath().resolve(fileName);

        if (Files.exists(path)) {
            try (CommentedFileConfig config = CommentedFileConfig.of(path)) {
                config.load();
                Map<String, Object> values = config.valueMap();

                if (values != null && !values.isEmpty()) {
                    this.defaultConfigs.put(fileName.intern(), ImmutableMap.copyOf(values));
                }

                WhiteNoise.LOGGER.info(CONFIG, "Loaded default config values from file at path {}",
                        path);
            }
            catch (Exception e) {
                WhiteNoise.LOGGER.error(CONFIG,
                        "Error loading default config values from file at path {}", path);
                e.printStackTrace();
            }
        }
    }

    Function<WhiteNoiseConfig, CommentedFileConfig> read(Path basePath) {
        return (config) -> {
            final Path configPath = basePath.resolve(config.getFileName());
            final CommentedFileConfig configData =
                    CommentedFileConfig.builder(configPath, TomlFormat.instance()).sync().
                            preserveInsertionOrder().
                            autosave().
                            onFileNotFound(this::setupConfigFile).
                            writingMode(WritingMode.REPLACE).
                            build();

            WhiteNoise.LOGGER.debug(CONFIG, "Built TOML config for {}", configPath);

            try {
                tryConfigFileLoad(configData);
            }
            catch (ParsingException ex) {
                WhiteNoise.LOGGER.error(CONFIG, "Error loading TOML config for {}", configPath);
                throw new ConfigLoadingException(config, ex);
            }

            this.tryDefaultConfigLoad(config);
            WhiteNoise.LOGGER.debug(CONFIG, "Loaded TOML config file {}", configPath);

            return configData;
        };
    }

    private boolean setupConfigFile(final Path path, final ConfigFormat<?> conf) throws IOException {
        Files.createDirectories(path.getParent());
        Path p = Services.CONFIG.getBackwardsCompatiblePath().resolve(path.getFileName());

        if (Files.exists(p)) {
            WhiteNoise.LOGGER.info(CONFIG, "Loading default config file from path {}", p);
            Files.copy(p, path);
        }
        else {
            Files.createFile(path);
            conf.initEmptyFile(path);
        }

        return true;
    }

    public Map<String, Map<String, Object>> getDefaultConfigs() {
        return ImmutableMap.copyOf(this.defaultConfigs);
    }

    public void acceptSyncedConfigs(String fileName, byte[] data) {
        WhiteNoiseConfig configFile = this.files.get(fileName);

        if (configFile != null) {
            final CommentedConfig configData =
                    TomlFormat.instance().createParser().parse(new ByteArrayInputStream(data));
            configFile.setConfigData(WhiteNoiseConfig.InstanceType.SERVER, configData, false);
            configFile.fireReload();
        }
    }

    public Map<String, byte[]> getConfigSync() {
        return this.configsByType.get(WhiteNoiseConfig.Type.SERVER).stream().collect(
                Collectors.toMap(WhiteNoiseConfig::getFileName, file -> {
                    try {
                        return Files.readAllBytes(file.getFullPath());
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
    }

    private static class ConfigLoadingException extends RuntimeException {
        public ConfigLoadingException(WhiteNoiseConfig config, Exception cause) {
            super("Failed loading config file " + config.getFileName() + " of type " + config.getType() +
                    " for " + config.getModId(), cause);
        }
    }

}

