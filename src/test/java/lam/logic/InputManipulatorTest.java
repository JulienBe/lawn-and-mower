package lam.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputManipulatorTest {

    String[] lawns = {"2 1", "5 5", "66 30"};
    String[] mowers = {
            """
            8 3 W
            L""",
            """
            1 2 N
            LFLFLFLFF""",
            """
            3 3 E
            FFRFFRFRRF""",
            """
            0 1 E
            L""",
            """
            1 1 S
            LFRLFLRFLRLFRLRLFLFLRFLRLFLRLFLRLFLRLFLRLF""",
            """
            2 0 W
            LFRLFRLFLFLRFLRLFLRLFLRLFLRLFLRLF""",
    };


    @Test
    void invalidString() {
        new TestUtilities().getInvalids().parallelStream().forEach(s ->
                assertThrows(IllegalArgumentException.class, () -> InputManipulator.validateInput(s))
        );
    }

    @Test
    void isValidInput() {
        InputManipulator.validateInput(lawns[0] + "\n" + mowers[0]);
        InputManipulator.validateInput(lawns[1] + "\n" + mowers[1] + "\n" + mowers[2]);
        InputManipulator.validateInput(lawns[2] + "\n" + mowers[3] + "\n" + mowers[4] + "\n" + mowers[5]);
    }

    @Test
    void extractLawn() {
        assertEquals(lawns[0], InputManipulator.extractLawn(lawns[0] + "\n" + mowers[0]));
        assertEquals(lawns[1], InputManipulator.extractLawn(lawns[1] + "\n" + mowers[1] + "\n" + mowers[2]));
        assertEquals(lawns[2], InputManipulator.extractLawn(lawns[2] + "\n" + mowers[3] + "\n" + mowers[4] + "\n" + mowers[5]));
    }

    @Test
    void extractMowers() {
        for (int i = 0; i < mowers.length; i++) {
            assertEquals(Arrays.asList(mowers[i]), InputManipulator.extractMowers(lawns[0] + "\n" + mowers[i]));
            assertEquals(
                    Arrays.asList(mowers[i], mowers[(i + 1) % mowers.length]),
                    InputManipulator.extractMowers(lawns[0] + "\n" + mowers[i] + "\n" + mowers[(i + 1) % mowers.length]));
        }
    }
}