package technology.roughness.whitenoise.util;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {

    public static ResourceLocation loc(String modid, String path) {
        return ResourceLocation.fromNamespaceAndPath(modid, path);
    }

    public static ResourceLocation mcLoc(String path) {
        return ResourceLocation.tryParse(path);
    }

}

