package lam.logic;

import lam.Mower;
import lam.enums.Direction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowingSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static lam.enums.Instruction.*;

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
        Lawn lawn = new Lawn(5, 5);
        Mower mower1 = new Mower(new Coordinate(1, 2), Direction.N, Arrays.asList(L,F,L,F,L,F,L,F,F), lawn);
        Mower mower2 = new Mower(new Coordinate(3, 3), Direction.E, Arrays.asList(F,F,R,F,F,R,F,R,R,F), lawn);
        assertEquals(new MowingSession(lawn, Arrays.asList(mower1, mower2)), session);
    }


}