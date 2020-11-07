package lam.logic;

import lam.records.MowingSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InputParserTest {

    @Test
    void initInvalids() {
        new TestUtilities().getInvalids().forEach(it ->
                assertThrows(IllegalArgumentException.class, () -> InputParser.init(it)));
    }

    @Test
    void initValids() {
        String example = new TestUtilities().getValid("given_test");
        MowingSession session = InputParser.init(example);
        assertEquals(TestUtilities.getExampleSession(), session);
    }


}