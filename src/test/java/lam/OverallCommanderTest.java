package lam;

import lam.enums.Direction;
import lam.logic.TestUtilities;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowingSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static lam.enums.Instruction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OverallCommanderTest {

    @Test
    void givenExample() {
        OverallCommander commander = new OverallCommander(TestUtilities.getExampleSession());
        commander.exec();
        var results = commander.results();
        assertEquals(new Coordinate(1, 3), results.get(0).coord());
        assertEquals(new Coordinate(5, 1), results.get(1).coord());
        assertEquals(Direction.N, results.get(0).dir());
        assertEquals(Direction.E, results.get(1).dir());
    }

    @Test
    void bumpyRide() {
        Lawn lawn = new Lawn(4, 5);
        Mower mower1 = new Mower(new Coordinate(0, 4), Direction.S, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 0);
        Mower mower2 = new Mower(new Coordinate(0, 0), Direction.N, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 1);
        Mower mower3 = new Mower(new Coordinate(1, 4), Direction.S, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 2);
        Mower mower4 = new Mower(new Coordinate(1, 0), Direction.N, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 3);
        MowingSession session = new MowingSession(lawn, Arrays.asList(mower1, mower2, mower3, mower4));
        OverallCommander commander = new OverallCommander(session);
        commander.exec();
        var results = commander.results();
        assertEquals(new Coordinate(0, 3), results.get(mower1.index).coord());
        assertEquals(new Coordinate(0, 1), results.get(mower2.index).coord());
        assertEquals(new Coordinate(1, 3), results.get(mower3.index).coord());
        assertEquals(new Coordinate(1, 1), results.get(mower4.index).coord());
    }

}