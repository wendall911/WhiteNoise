package technology.roughness.whitenoise.platform.services;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public interface IPlatform {

    Identifier getResourceLocation(Item item);

    boolean isModLoaded(String name);

    boolean isPhysicalClient();

    boolean isFakePlayer(Player player);

}

