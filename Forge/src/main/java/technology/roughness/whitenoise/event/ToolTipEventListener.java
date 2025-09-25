package technology.roughness.whitenoise.event;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolTipEventListener {

    @SubscribeEvent
    public static void onToolTipEvent(ItemTooltipEvent event) {
        AdvancedTooltipEventHandler.onItemTooltip(event.getItemStack(), event.getToolTip());
    }

}
