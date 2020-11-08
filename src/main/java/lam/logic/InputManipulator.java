package lam.logic;

import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InputManipulator {

    private static final Logger LOG = Logger.getLogger(InputManipulator.class);
    private static final String SEPARATOR = " ";
    private static final String COORD_REGEX = "[0-9]+";
    private static final String COORDS_REGEX = "^" + COORD_REGEX + SEPARATOR + COORD_REGEX;
    private static final String DIR_REGEX = "[N|E|S|W]";
    private static final String MOWER_REGEX = COORDS_REGEX + SEPARATOR + DIR_REGEX;
    private static final String INSTRUCTIONS_REGEX = "^[L|R|F]+$";
    private static final String NEW_LINE_REGEX = "\\R";
    private static final Pattern lawnPattern = Pattern.compile(COORDS_REGEX);
    private static final Pattern mowerLine1Pattern = Pattern.compile(MOWER_REGEX);
    private static final Pattern mowerLine2Pattern = Pattern.compile(INSTRUCTIONS_REGEX);
    private static final Pattern mowerPattern = Pattern.compile(MOWER_REGEX + NEW_LINE_REGEX + INSTRUCTIONS_REGEX, Pattern.MULTILINE);
    private static final Pattern coordPattern = Pattern.compile(COORD_REGEX, Pattern.MULTILINE);
    private static final Pattern dirPattern = Pattern.compile(DIR_REGEX, Pattern.MULTILINE);

    private InputManipulator() {}

    public static @NotNull String extractMowerLine1(@NotNull String s) {
        return getFirstOnList(extractAllMatching(s, mowerLine1Pattern));
    }
    public static @NotNull String extractMowerLine2(@NotNull String s) {
        return getFirstOnList(extractAllMatching(s, mowerLine2Pattern));
    }
    public static @NotNull String extractDirection(@NotNull String s) {
        return getFirstOnList(extractAllMatching(s, dirPattern));
    }
    public static @NotNull String extractLawn(@NotNull String s) {
        return getFirstOnList(extractAllMatching(s, lawnPattern));
    }
    public static @NotNull List<String> extractCoord(@NotNull String s) {
        return extractAllMatching(s, coordPattern);
    }

    private static @NotNull String getFirstOnList(@NotNull List<String> list) {
        return cleanInput(list.get(0));
    }

    private static @NotNull List<String> extractAllMatching(@NotNull String s, @NotNull Pattern pattern) {
        s = cleanInput(s);
        final List<String> occurrences = pattern.matcher(s)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
        if (!occurrences.isEmpty())
            return occurrences;
        throw illegalString(s, "Extraction failed" + patternErrorStr(mowerPattern));
    }

    /**
     * Just to be a bit kind
     */
    private static @NotNull String cleanInput(String s) {
        return s.strip().toUpperCase();
    }

    private static @NotNull IllegalArgumentException illegalString(String s, String error) {
        String errorMsg = "Trying to create an invalid simulation. \n " + error + "\ngot : \n" + s + "end of what we got";
        LOG.error(errorMsg);
        return new IllegalArgumentException(errorMsg);
    }

    private static @NotNull String patternErrorStr(Pattern pattern) {
        return " according to pattern " + pattern.pattern() + "\tConsider checking with https://www.freeformatter.com/java-regex-tester.html";
    }
}
