package technology.roughness.whitenoise.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class ToolTipEventListener {

    public static void init() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            AdvancedTooltipEventHandler.onItemTooltip(stack, lines);
        });
    }

}
