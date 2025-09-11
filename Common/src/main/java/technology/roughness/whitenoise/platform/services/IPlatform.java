package technology.roughness.whitenoise.platform.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public interface IPlatform {

    ResourceLocation getResourceLocation(Item item);

    boolean isModLoaded(String name);

    boolean isPhysicalClient();

    boolean isFakePlayer(Player player);

}

