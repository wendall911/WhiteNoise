package technology.roughness.whitenoise.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for pretty-printing string representation of components.
 * Note: This is a simple implementation and does not cover all edge cases.
 * It is primarily designed for debugging purposes.
 */

public class PrettyPrinter {

    public static String format(String unformattedString) {
        StringBuilder prettyStringBuilder = new StringBuilder();
        int indentLevel = 0;
        boolean inQuote = false;

        Pattern optionalReferencePattern = Pattern.compile("Optional\\[Reference\\{.*?/(.*?)].*?}]");
        Matcher optionalReferenceMatcher = optionalReferencePattern.matcher(unformattedString);

        if (optionalReferenceMatcher.find()) {
            Pattern innerPattern = Pattern.compile("Optional\\[Reference\\{.*?}]");
            Matcher innerMatcher = innerPattern.matcher(unformattedString);

            unformattedString = innerMatcher.replaceAll(optionalReferenceMatcher.group(1));
        }
        else {
            unformattedString = unformattedString.replaceAll(", usingConvertsTo=Optional.empty", "");
            Pattern optionalPattern = Pattern.compile("Optional\\[(.*?)]");
            Matcher optionalMatcher = optionalPattern.matcher(unformattedString);

            if (optionalMatcher.find()) {
                Pattern innerPattern = Pattern.compile("Optional\\[.*?]");
                Matcher innerMatcher = innerPattern.matcher(unformattedString);

                unformattedString = innerMatcher.replaceAll(optionalMatcher.group(1));
            }
        }

        Pattern objectPattern = Pattern.compile("](.*?)}");
        Matcher objectMatcher = objectPattern.matcher(unformattedString);

        if (objectMatcher.find()) {
            return "";
        }

        unformattedString = unformattedString.replaceAll(", customColor=Optional.empty", "");
        unformattedString = unformattedString.replaceAll(", customEffects=\\[]", "");
        unformattedString = unformattedString.replaceAll(", effects=\\[]", "");
        unformattedString = unformattedString.replaceAll("FoodProperties", "");
        unformattedString = unformattedString.replaceAll("PotionContents", "");

        for (char ch : unformattedString.toCharArray()) {
            switch (ch) {
                case '"':
                    inQuote = !inQuote;
                    prettyStringBuilder.append(ch);
                    break;
                case '[':
                    prettyStringBuilder.append(ch);
                    indentLevel++;
                    appendIndentedNewLine(indentLevel, prettyStringBuilder);
                    break;
                case ']':
                    indentLevel--;
                    appendIndentedNewLine(indentLevel, prettyStringBuilder);
                    prettyStringBuilder.append(ch);
                    break;
                case ',':
                    prettyStringBuilder.append(ch);
                    if (!inQuote) {
                        appendIndentedNewLine(indentLevel, prettyStringBuilder);
                    }
                    break;
                default:
                    prettyStringBuilder.append(ch);
            }
        }
        return prettyStringBuilder.toString();
    }

    private static void appendIndentedNewLine(int indentLevel, StringBuilder stringBuilder) {
        stringBuilder.append("\n");
        stringBuilder.append("  " .repeat(Math.max(0, indentLevel)));
    }

}
