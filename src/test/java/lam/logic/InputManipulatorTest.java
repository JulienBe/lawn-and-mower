package lam.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void extractLawn() {
        assertEquals(lawns[0], InputManipulator.extractLawn(lawns[0] + "\n" + mowers[0]));
        assertEquals(lawns[1], InputManipulator.extractLawn(lawns[1] + "\n" + mowers[1] + "\n" + mowers[2]));
        assertEquals(lawns[2], InputManipulator.extractLawn(lawns[2] + "\n" + mowers[3] + "\n" + mowers[4] + "\n" + mowers[5]));
    }
}