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

        Client(WhiteNoiseConfigSpec.Builder builder) {
            showAdvancedTooltips = builder
                .comment(
                    "Show advanced tooltips. Requires advanced tooltips (F3+H)",
                    "Shows NBT data, tags and durability information."
                )
                .define("showAdvancedTooltips", false);
        }

        public static boolean showAdvancedTooltips() {
            return CLIENT.showAdvancedTooltips.get();
        }

    }

}
