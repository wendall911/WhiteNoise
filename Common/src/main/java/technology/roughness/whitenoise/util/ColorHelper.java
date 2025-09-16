package technology.roughness.whitenoise.util;

/*
 * Helper class for color operations.
 * This class provides methods to convert between different color formats.
 */

import java.awt.Color;

public class ColorHelper {

    /**
     * Decodes a hex color string (e.g. "#RRGGBB" or "RRGGBB") to AWT Color.
     * The alpha channel is set to 255 (opaque).
     *
     * @param hexString The hex color string to decode.
     * @return AWT Color object representing the color.
     */
    public static Color decodeHex(String hexString) {
        return Color.decode(hexString);
    }

    public static int hexToRGBA(String hex) {
        Color color = decodeHex(hex);

        return color.getRGB();
    }

    public static String RGBAtoHex(int rgba) {
        Color color = new Color(rgba, true);

        return "#" + Integer.toHexString(color.getRGB() & 0xffffff);
    }

    public enum Colors {

        BLACK("#000000"),
        WHITE("#FFFFFF"),
        DARK_BLUE("#0000AA"),
        DARK_GREEN("#00AA00"),
        DARK_AQUA("#00AAAA"),
        DARK_RED("#AA0000"),
        DARK_PURPLE("#AA00AA"),
        GOLD("#FFAA00"),
        GRAY("#AAAAAA"),
        DARK_GRAY("#555555"),
        BLUE("#5555FF"),
        GREEN("#55FF55"),
        AQUA("#55FFFF"),
        RED("#FF5555"),
        LIGHT_PURPLE("#FF55FF"),
        YELLOW("#FFFF55"),
        OFFWHITE("#E0E0E0"),
        ERROR_RED("#FF0000");

        private final String hex;

        Colors(String hex) {
            this.hex = hex;
        }

        public String getHex() {
            return hex;
        }

        public int toRGBA() {
            return ColorHelper.hexToRGBA(this.hex);
        }

    }

}
