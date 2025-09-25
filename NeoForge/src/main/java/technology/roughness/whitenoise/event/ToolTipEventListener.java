package technology.roughness.whitenoise.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEventListener {

    @SubscribeEvent
    public static void onToolTipEvent(ItemTooltipEvent event) {
        AdvancedTooltipEventHandler.onItemTooltip(event.getItemStack(), event.getToolTip());
    }

}
