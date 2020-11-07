package lam;

import lam.enums.Direction;
import lam.logic.TestUtilities;
import lam.records.Coordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OverallCommanderTest {

    @Test
    void givenExample() {
        OverallCommander commander = new OverallCommander(TestUtilities.getExampleSession());
        commander.runFrom(0);
        var results = commander.results();
        assertEquals(new Coordinate(1, 3), results.get(0).coord());
        assertEquals(new Coordinate(5, 1), results.get(1).coord());
        assertEquals(Direction.N, results.get(0).dir());
        assertEquals(Direction.E, results.get(1).dir());
    }

}