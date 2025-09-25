package technology.roughness.whitenoise.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResourceLocationHelper {

    public static ResourceLocation loc(String modid, String path) {
        return ResourceLocation.fromNamespaceAndPath(modid, path);
    }

    public static ResourceLocation parse(String path) {
        return ResourceLocation.parse(path);
    }

    public static ResourceLocation tryParse(String path) {
        return ResourceLocation.tryParse(path);
    }

    public static ResourceLocation getItemStackId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    public static String getModId(ItemStack stack) {
        return getItemStackId(stack).getNamespace();
    }

}
