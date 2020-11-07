package lam.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    String[] invalidStrings = {
            """
            
            """,

            """
            mass
            effect            
            """,

            """
            2 1
            8 3 W
            mef
            """,

            """
            0 2
            0 1 E,
            """,

            """
            3 3
            1 1 S
             
            """,

            """
            -1 10
            0 5 N
            L
            """,

            """
            5 5
            1 2 N
            LFLFLFLFF
            3 3 E
            
            """
    };
    String[] validStrings = {
            """
            2 1
            8 3 W
            L
            """,

            """
            1 2
            0 1 E
            L
            """,

            """
            3 3
            1 1 S
            LFRLFLRFLRLFRLRLFLFLRFLRLFLRLFLRLFLRLFLRLF
            """,

            """
            20 10
            0 5 N
            L
            1 5 N
            LRFLFRLFR
            1 1 S
            LFRLFLRFLRLFRLRLFLFLRFLRLFLRLFLRLFLRLFLRLF
            """,

            """
            5 5
            1 2 N
            LFLFLFLFF
            3 3 E
            FFRFFRFRRF
            """
    };

    @Test
    void invalidString() {
        assertThrows(IllegalArgumentException.class, () -> InputValidator.isValidInput(""));
        Arrays.stream(invalidStrings).parallel().forEach(s ->
                assertFalse(InputValidator.isValidInput(s))
        );
    }

    @Test
    void isValidInput() {
        Arrays.stream(validStrings).parallel().forEach(s ->
            assertTrue(InputValidator.isValidInput(s))
        );
    }
}