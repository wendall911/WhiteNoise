package technology.roughness.whitenoise.event;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.slf4j.helpers.MessageFormatter;

import technology.roughness.whitenoise.config.ConfigHandler;
import technology.roughness.whitenoise.util.PrettyPrinter;

public class AdvancedTooltipEventHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> toolTip) {
        if (itemStack != null && !itemStack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            if (ConfigHandler.Client.showAdvancedTooltips() && mc.options.advancedItemTooltips) {
                if (itemStack.getMaxDamage() != 0 && itemStack.getDamageValue() == 0) {
                    toolTip.add(
                        1,
                        Component.translatable("tooltip.whitenoise.durability", itemStack.getMaxDamage())
                            .withStyle(ChatFormatting.GRAY)
                    );
                }

                if (mc.hasControlDown()) {
                    for (TypedDataComponent<?> typedDataComponent : itemStack.getComponents()) {
                        Object value = typedDataComponent.value();
                        String unformatted = MessageFormatter.format("{}", value).getMessage();
                        boolean showComponent = true;

                        switch (value) {
                            case ItemLore(List<Component> lines, List<Component> styledLines) ->
                                    showComponent = !lines.isEmpty() || !styledLines.isEmpty();
                            case ItemAttributeModifiers(List<ItemAttributeModifiers.Entry> modifiers) ->
                                    showComponent = !modifiers.isEmpty();
                            case ItemEnchantments itemEnchantments ->
                                    showComponent = !itemEnchantments.keySet().isEmpty();
                            case ItemContainerContents ignored -> showComponent = false;
                            case TooltipDisplay ignored -> showComponent = false;
                            default -> {}
                        }

                        if (showComponent) {
                            String prettyString = PrettyPrinter.format(unformatted);

                            if (!prettyString.isEmpty()) {
                                String componentString = String.format("%s: %s", typedDataComponent.type(), prettyString);
                                List<String> lines = Arrays.stream(componentString.split("\\n")).toList();

                                for (String tagString : lines) {
                                    toolTip.add(
                                        Component.literal(tagString).withStyle(ChatFormatting.GRAY)
                                    );
                                }
                            }
                        }
                    }
                }
                else {
                    toolTip.add(
                        Component.translatable(
                            "tooltip.whitenoise.ctrl",
                                Component.translatable("tooltip.whitenoise.ctrl_literal")
                                    .withStyle(ChatFormatting.YELLOW)
                            ).withStyle(ChatFormatting.GRAY)
                    );
                }

                Collection<ResourceLocation> tags = getTags(itemStack.getTags());

                if (mc.hasShiftDown()) {
                    Block block = Block.byItem(itemStack.getItem());

                    if (!tags.isEmpty()) {
                        toolTip.add(
                            Component.translatable("tooltip.whitenoise.item_tags")
                                .withStyle(ChatFormatting.DARK_GRAY)
                        );
                        tags.forEach((tag) -> toolTip.add(
                            Component.literal("  #" + tag.toString()).withStyle(ChatFormatting.GRAY)));
                    }

                    if (block != Blocks.AIR) {
                        tags = getTags(block.defaultBlockState().getTags());

                        if (!tags.isEmpty()) {
                            toolTip.add(
                                Component.translatable("tooltip.whitenoise.block_tags")
                                    .withStyle(ChatFormatting.DARK_GRAY)
                            );
                            tags.forEach((tag) -> toolTip.add(
                                Component.literal("  #" + tag).withStyle(ChatFormatting.GRAY)));
                        }
                    }
                }
                else {
                    if (tags.isEmpty()) {
                        Block block = Block.byItem(itemStack.getItem());

                        if (block != Blocks.AIR) {
                            tags = getTags(block.defaultBlockState().getTags());
                        }
                    }

                    if (!tags.isEmpty()) {
                        toolTip.add(
                            Component.translatable(
                                "tooltip.whitenoise.shift",
                                Component.translatable("tooltip.whitenoise.shift_literal")
                                    .withStyle(ChatFormatting.YELLOW)
                            ).withStyle(ChatFormatting.GRAY)
                        );
                    }
                }
            }

        }
    }

    public static <T> Collection<ResourceLocation> getTags(Stream<TagKey<T>> tags) {
        return tags.map(TagKey::location).collect(Collectors.toUnmodifiableSet());
    }

}
