package technology.roughness.whitenoise.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ResourceLocationHelper {

    public static Identifier loc(String modid, String path) {
        return Identifier.fromNamespaceAndPath(modid, path);
    }

    public static Identifier parse(String path) {
        return Identifier.parse(path);
    }

    public static Identifier tryParse(String path) {
        return Identifier.tryParse(path);
    }

    public static Identifier getItemStackId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem());
    }

    public static Identifier getBlockId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String getModId(ItemStack stack) {
        return getItemStackId(stack).getNamespace();
    }

    public static String getModId(Block block) {
        return getBlockId(block).getNamespace();
    }

}
