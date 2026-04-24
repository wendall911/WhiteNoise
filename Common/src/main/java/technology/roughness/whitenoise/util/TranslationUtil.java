package technology.roughness.whitenoise.util;

import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import technology.roughness.whitenoise.WhiteNoise;
import technology.roughness.whitenoise.config.ConfigHandler;

public class TranslationUtil {

    private final Set<String> untranslatables = new HashSet<>();
    private final Set<Pair<String, String>> untranslatablesWithFallback = new HashSet<>();

    public MutableComponent getWithLiteralFallback(final String translationKey, final String fallback) {
        MutableComponent component = Component.empty();

        if (I18n.exists(translationKey)) {
            component.append(Component.translatable(translationKey));
        }
        else {
            untranslatablesWithFallback.add(Pair.of(translationKey, fallback));
            component.append(Component.literal(fallback));
        }

        return component;
    }

    public Component get(final String modTranslationKey, final String fallbackTranslationKey) {
        if (exists(modTranslationKey)) {
            return Component.translatable(modTranslationKey);
        }
        else {
            return translatable(fallbackTranslationKey);
        }
    }

    public Component translatable(final String translationKey, final MutableComponent replacements, @Nullable ChatFormatting... formatting) {
        if (exists(translationKey)) {
            return Component.translatable(translationKey, replacements).withStyle(formatting);
        }
        else {
            return Component.literal(translationKey).withStyle(formatting);
        }
    }

    public Component translatable(final String translationKey, final String replacements, @Nullable ChatFormatting... formatting) {
        if (exists(translationKey)) {
            return Component.translatable(translationKey, replacements).withStyle(formatting);
        }
        else {
            return Component.literal(translationKey).withStyle(formatting);
        }
    }

    public Component translatable(final String translationKey, @Nullable ChatFormatting... formatting) {
        if (exists(translationKey)) {
            return Component.translatable(translationKey).withStyle(formatting);
        }
        else {
            return Component.literal(translationKey).withStyle(formatting);
        }
    }

    public boolean exists(final String translationKey) {
        if (!I18n.exists(translationKey)) {
            untranslatables.add(translationKey);

            return false;
        }

        return true;
    }

    public void finish() {
        if (ConfigHandler.Client.logTranslatableWarnings() && (!untranslatables.isEmpty() || !untranslatablesWithFallback.isEmpty())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("""
                \nDev warning - Untranslated configuration keys encountered.
                Please translate your configuration keys so users can properly configure your mod.
            """);
            if (!untranslatables.isEmpty()) {
                stringBuilder.append("\nUntranslated keys:");
                for (String key : untranslatables) {
                    stringBuilder.append("\n  \"").append(key).append("\": \"\",");
                }
            }
            if (!untranslatablesWithFallback.isEmpty()) {
                stringBuilder.append("\nThe following keys have fallbacks. ")
                    .append("Please check if those are suitable, and translate them if they're not.");
                for (Pair<String, String> untranslatable : untranslatablesWithFallback) {
                    stringBuilder.append("\n  \"").append(untranslatable.getFirst())
                        .append("\": \"").append(untranslatable.getSecond()).append("\",");
                }
            }

            WhiteNoise.LOGGER.warn("{}", stringBuilder);
        }
        untranslatables.clear();
    }

    public interface TranslatableEnum {

        default Component getTranslatedName() {
            return Component.literal(((Enum<?>) this).name());
        }

    }

}
