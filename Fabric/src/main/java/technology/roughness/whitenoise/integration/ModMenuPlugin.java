package technology.roughness.whitenoise.integration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.fabricmc.loader.api.FabricLoader;

import technology.roughness.whitenoise.common.WhiteNoiseModContainer;
import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigTracker;
import technology.roughness.whitenoise.config.client.screen.ConfigurationScreen;
import technology.roughness.whitenoise.WhiteNoise;

public class ModMenuPlugin implements ModMenuApi {

    private static final Set<String> LOGGED = new HashSet<>();

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, Map<WhiteNoiseConfig.Type, Set<WhiteNoiseConfig>>> configs =
                WhiteNoiseConfigTracker.INSTANCE.getConfigsByMod();
        Map<String, ConfigScreenFactory<?>> result = new HashMap<>();

        configs.forEach((key, modConfigs) -> {
            FabricLoader.getInstance().getModContainer(key).ifPresent(modContainer -> {
                int count = modConfigs.values().stream().mapToInt(Set::size).sum();

                if (!LOGGED.contains(key)) {
                    WhiteNoise.LOGGER.info("Registering config screens for mod {} with {} config(s)", key,
                            count);
                    LOGGED.add(key);
                }
                String displayName = modContainer.getMetadata().getName();

                WhiteNoiseModContainer wnModContainer = new WhiteNoiseModContainer(
                    key,
                    displayName,
                    modConfigs
                );
                result.put(key, screen -> new ConfigurationScreen(wnModContainer, screen));
            });
        });

        return result;

    }

}
