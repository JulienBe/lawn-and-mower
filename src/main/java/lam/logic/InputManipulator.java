package lam.logic;

import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputManipulator {

    private static final Logger LOG = Logger.getLogger(InputManipulator.class);
    private static final String LAWN_REGEX = "[0-9]+ [0-9]+";
    private static final String MOWER_REGEX = "[0-9]+ [0-9]+ [N|E|S|W]";
    private static final String INSTRUCTIONS_REGEX = "[L|R|F]+";
    private static final String NEW_LINE_REGEX = "\\R";
    private static final Pattern validInput = Pattern.compile(LAWN_REGEX + "(" + NEW_LINE_REGEX + MOWER_REGEX + NEW_LINE_REGEX + INSTRUCTIONS_REGEX + ")+");
    private static final Pattern lawnSplitter = Pattern.compile(LAWN_REGEX + NEW_LINE_REGEX);
    private static final Pattern mowerSplitter = Pattern.compile(MOWER_REGEX + NEW_LINE_REGEX + INSTRUCTIONS_REGEX);

    private InputManipulator() {}

    public static String extractLawn(@NotNull String s) {
        s = cleanInput(s);
        final Matcher matcher = lawnSplitter.matcher(s);
        if (matcher.find())
            return matcher.group(0).split(NEW_LINE_REGEX)[0];
        throw illegalString(s, "Lawn could not be extract according to pattern " + lawnSplitter.pattern() + "\tConsider checking with https://www.freeformatter.com/java-regex-tester.html");
    }

    public static List<String> extractMowers(@NotNull String s) {
        s = cleanInput(s);
        final Matcher matcher = mowerSplitter.matcher(s);
        List<String> mowers = new ArrayList<>();
        while (matcher.find())
            mowers.add(matcher.group());
        if (!mowers.isEmpty())
            return mowers;
        throw illegalString(s, "Mower could not be extract according to pattern " + mowerSplitter.pattern() + "\tConsider checking with https://www.freeformatter.com/java-regex-tester.html");
    }

    public static boolean isValidInput(@NotNull String s) {
        if (s.isEmpty())
            throw illegalString(s, "Empty String");
        return validInput.matcher(cleanInput(s)).matches();
    }

    /**
     * Just to be a bit kind
     */
    private static String cleanInput(String s) {
        return s.strip().toUpperCase();
    }

    private static IllegalArgumentException illegalString(String s, String error) {
        String errorMsg = "Trying to create an invalid simulation. \n " + error + "\ngot : \n" + s;
        LOG.error(errorMsg);
        return new IllegalArgumentException(errorMsg);
    }

}
