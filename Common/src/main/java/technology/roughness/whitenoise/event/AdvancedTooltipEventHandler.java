package technology.roughness.whitenoise.event;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import technology.roughness.whitenoise.config.ConfigHandler;

public class AdvancedTooltipEventHandler {

    public static void onItemTooltip(ItemStack itemStack, List<Component> toolTip) {
        if (itemStack != null && !itemStack.isEmpty()) {
            boolean hasTag = itemStack.hasTag();
            CompoundTag itemStackTag = itemStack.getTag();

            if (ConfigHandler.Client.showAdvancedTooltips() && Minecraft.getInstance().options.advancedItemTooltips) {
                if (hasTag) {
                    if (itemStack.getMaxDamage() != 0 && itemStack.getDamageValue() == 0) {
                        toolTip.add(
                            1,
                            new TranslatableComponent("tooltip.whitenoise.durability", itemStack.getMaxDamage())
                                .withStyle(ChatFormatting.GRAY)
                        );
                    }

                    if (Screen.hasControlDown()) {
                        toolTip.add(
                            new TranslatableComponent("tooltip.whitenoise.nbt_length", getNBTSize(itemStackTag))
                                .withStyle(ChatFormatting.GRAY)
                        );

                        if (itemStackTag != null) {
                            String tagString = itemStackTag.toString();
                            int length = 200;

                            if (tagString.length() > length) {
                                toolTip.add(
                                    new TextComponent(tagString.substring(0, length))
                                        .withStyle(ChatFormatting.GRAY)
                                );
                                toolTip.add(
                                    new TranslatableComponent("tooltip.whitenoise.more", tagString.length() - length)
                                        .withStyle(ChatFormatting.DARK_GRAY)
                                );
                            }
                            else {
                                toolTip.add(new TextComponent(tagString).withStyle(ChatFormatting.GRAY));
                            }
                        }
                    }
                    else {
                        toolTip.add(
                            new TranslatableComponent(
                                "tooltip.whitenoise.ctrl",
                                    new TranslatableComponent("tooltip.whitenoise.ctrl_literal")
                                        .withStyle(ChatFormatting.YELLOW)
                                ).withStyle(ChatFormatting.GRAY)
                        );
                    }
                }

                Collection<ResourceLocation> tags = getTags(itemStack.getTags());

                if (Screen.hasShiftDown()) {
                    Block block = Block.byItem(itemStack.getItem());

                    if (!tags.isEmpty()) {
                        toolTip.add(
                            new TranslatableComponent("tooltip.whitenoise.item_tags")
                                .withStyle(ChatFormatting.DARK_GRAY)
                        );
                        tags.forEach((tag) -> toolTip.add(
                            new TextComponent("  #" + tag.toString()).withStyle(ChatFormatting.GRAY)));
                    }

                    if (block != Blocks.AIR) {
                        tags = getTags(block.defaultBlockState().getTags());

                        if (!tags.isEmpty()) {
                            toolTip.add(
                                new TranslatableComponent("tooltip.whitenoise.block_tags")
                                    .withStyle(ChatFormatting.DARK_GRAY)
                            );
                            tags.forEach((tag) -> toolTip.add(
                                new TextComponent("  #" + tag).withStyle(ChatFormatting.GRAY)));
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
                            new TranslatableComponent(
                                "tooltip.whitenoise.shift",
                                new TranslatableComponent("tooltip.whitenoise.shift_literal")
                                    .withStyle(ChatFormatting.YELLOW)
                            ).withStyle(ChatFormatting.GRAY)
                        );
                    }
                }
            }

        }
    }

    private static int getNBTSize(@Nullable CompoundTag nbt) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

        buffer.writeNbt(nbt);
        buffer.release();

        return buffer.writerIndex();
    }

    public static <T> Collection<ResourceLocation> getTags(Stream<TagKey<T>> tags) {
        return tags.map(TagKey::location).collect(Collectors.toUnmodifiableSet());
    }

}
