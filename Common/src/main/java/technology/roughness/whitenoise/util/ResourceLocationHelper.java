package technology.roughness.whitenoise.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

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

    public static ResourceLocation getBlockId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String getModId(ItemStack stack) {
        return getItemStackId(stack).getNamespace();
    }

    public static String getModId(Block block) {
        return getBlockId(block).getNamespace();
    }

}

