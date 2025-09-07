package technology.roughness.whitenoise.platform;

import java.util.ServiceLoader;

import org.slf4j.Logger;

public class ServicesBase {

    public static <T> T load(Logger logger, Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(
                        () -> new NullPointerException("Failed to load service for " + clazz.getName()));
        logger.debug("Loaded {} for service {}", loadedService, clazz);

        return loadedService;
    }

}
