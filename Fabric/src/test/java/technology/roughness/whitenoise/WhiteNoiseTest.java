package technology.roughness.whitenoise;

import technology.roughness.whitenoise.config.WhiteNoiseConfig;
import technology.roughness.whitenoise.config.WhiteNoiseConfigLoader;
import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;
import technology.roughness.whitenoise.config.WhiteNoiseTestConfig;

public class WhiteNoiseTest implements WhiteNoiseConfigInitializer {

    @Override
    public void onInitializeConfig() {
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

