package technology.roughness.whitenoise.common;

import java.util.Map;
import java.util.Set;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;

/*
 * Mod container for White Noise configurations. Mocking Neoforge's ModContainer for simplicity.
 */
public record WhiteNoiseModContainer(String modId, String displayName, Map<WhiteNoiseConfig.Type, Set<WhiteNoiseConfig>> configs) {

    public Set<WhiteNoiseConfig> getConfigSet(WhiteNoiseConfig.Type type) {
        Set<WhiteNoiseConfig> config = this.configs.get(type);

        return config == null ? Set.of() : config;
    }

}
