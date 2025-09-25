package technology.roughness.whitenoise.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResourceLocationHelper {

    @SuppressWarnings("removal")
    public static ResourceLocation loc(String modid, String path) {
        return new ResourceLocation(modid, path);
    }

    @SuppressWarnings("removal")
    public static ResourceLocation mcLoc(String path) {
        return new ResourceLocation(path);
    }

    public static ResourceLocation getItemStackId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    public static String getModId(ItemStack stack) {
        return getItemStackId(stack).getNamespace();
    }

}

