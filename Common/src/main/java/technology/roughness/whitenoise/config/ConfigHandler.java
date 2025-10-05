package technology.roughness.whitenoise.config;

import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {

    public static final WhiteNoiseConfigSpec CLIENT_SPEC;
    private static final Client CLIENT;

    static {
        Pair<Client, WhiteNoiseConfigSpec> clientSpecPair = new WhiteNoiseConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static final class Client {
        private final WhiteNoiseConfigSpec.BooleanValue showAdvancedTooltips;
        private final WhiteNoiseConfigSpec.BooleanValue logTranslatableWarnings;

        Client(WhiteNoiseConfigSpec.Builder builder) {
            showAdvancedTooltips = builder
                .comment(
                    "Show advanced tooltips. Requires advanced tooltips (F3+H)",
                    "Shows NBT data, tags and durability information."
                )
                .define("showAdvancedTooltips", false);
            logTranslatableWarnings = builder
                .comment(
                    "Log warnings for missing translation keys.",
                    "If enabled, missing translation keys will be logged to the console.",
                    "This can help identify missing translations in the mod."
                )
                .define("logTranslatableWarnings", true);
        }

        public static boolean showAdvancedTooltips() {
            return CLIENT.showAdvancedTooltips.get();
        }

        public static boolean logTranslatableWarnings() {
            return CLIENT.logTranslatableWarnings.get();
        }

    }

}
