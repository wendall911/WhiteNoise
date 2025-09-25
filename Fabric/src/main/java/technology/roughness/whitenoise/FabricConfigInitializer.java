package technology.roughness.whitenoise;

import technology.roughness.whitenoise.config.WhiteNoiseConfigInitializer;

public class FabricConfigInitializer implements WhiteNoiseConfigInitializer {

    @Override
    public void onInitialize() {
        WhiteNoise.initConfig();
    }

}
