package lam.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void turnsFromNorth() {
        assertEquals(Direction.W, Direction.N.turnCCW());
        assertEquals(Direction.E, Direction.N.turnCW());
    }

    @Test
    void turnsFromSouth() {
        assertEquals(Direction.W, Direction.S.turnCW());
        assertEquals(Direction.E, Direction.S.turnCCW());
    }

    @Test
    void turnsFromEast() {
        assertEquals(Direction.S, Direction.E.turnCW());
        assertEquals(Direction.N, Direction.E.turnCCW());
    }

    @Test
    void turnsFromWest() {
        assertEquals(Direction.S, Direction.W.turnCCW());
        assertEquals(Direction.N, Direction.W.turnCW());
    }

    @Test
    void turnALotCW() {
        var previousDir = Direction.N;
        for (int i = 0; i < Direction.values().length * 11; i++) {
            previousDir = previousDir.turnCW();
        }
        assertEquals(Direction.N, previousDir);
    }

    @Test
    void turnALotCCW() {
        var previousDir = Direction.S;
        for (int i = 0; i < Direction.values().length * 11; i++) {
            previousDir = previousDir.turnCCW();
        }
        assertEquals(Direction.S, previousDir);
    }

    @Test
    void turnThenTurnBack() {
        for (Direction d : Direction.values()) {
            assertEquals(d, d.turnCCW().turnCW());
            assertEquals(d, d.turnCW().turnCW().turnCCW().turnCCW());
        }
    }

}