package technology.roughness.whitenoise;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigLoader;
import technology.roughness.whitenoise.config.WhiteNoiseInitializer;
import technology.roughness.whitenoise.config.WhiteNoiseTestConfig;

public class WhiteNoiseTest implements WhiteNoiseInitializer {

    @Override
    public void onInitializeConfig() {
        WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.CLIENT, WhiteNoiseTestConfig.CLIENT_SPEC,
            "whitenoisetest");
        WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.COMMON, WhiteNoiseTestConfig.COMMON_SPEC,
            "whitenoisetest");
        WhiteNoiseConfigLoader.add(WhiteNoiseConfig.Type.SERVER, WhiteNoiseTestConfig.SERVER_SPEC,
            "whitenoisetest");
    }

}

