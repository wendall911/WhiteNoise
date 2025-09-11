package technology.roughness.whitenoise;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigLoader;
import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;
import technology.roughness.whitenoise.config.WhiteNoiseTestConfig;

public class WhiteNoiseTest implements WhiteNoiseConfigInitializer {

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

