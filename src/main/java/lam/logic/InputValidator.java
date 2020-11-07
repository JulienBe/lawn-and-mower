package lam.logic;

import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Logger LOG = Logger.getLogger(InputValidator.class);
    private static final String LAWN_REGEX = "[0-9]+ [0-9]+";
    private static final String MOWER_REGEX = "[0-9]+ [0-9]+ [N|E|S|W]";
    private static final String INSTRUCTIONS_REGEX = "[L|R|F]+";
    private static final String NEW_LINE_REGEX = "\\R";
    private static final Pattern validInput = Pattern.compile(LAWN_REGEX + "(" + NEW_LINE_REGEX + MOWER_REGEX + NEW_LINE_REGEX + INSTRUCTIONS_REGEX + ")+");

    private InputValidator() {}

    public static boolean isValidInput(@NotNull String s) {
        if (s.isEmpty())
            illegalString(s, "Empty String");
        return validInput.matcher(cleanInput(s)).matches();
    }

    private static String cleanInput(String s) {
        return s.strip().toUpperCase();
    }

    private static void illegalString(String s, String error) {
        String errorMsg = "Trying to create an invalid simulation. \n " + error + "\ngot : " + s;
        LOG.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
    }

}
