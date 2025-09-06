package technology.roughness.whitenoise.util;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {

    @SuppressWarnings("removal")
    public static ResourceLocation loc(String modid, String path) {
        return new ResourceLocation(modid, path);
    }

}

