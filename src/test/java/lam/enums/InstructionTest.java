package lam.enums;

import lam.records.Coordinate;
import lam.records.MowerState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionTest {

    @Test
    void turnDoesntMove() {
        final Coordinate initialCoord = new Coordinate(4, 5);
        final MowerState initialState = new MowerState(Direction.E, initialCoord);

        assertEquals(initialCoord, Instruction.L.exec.apply(initialState).coord());
        assertEquals(initialCoord, Instruction.R.exec.apply(initialState).coord());
    }

    @Test
    void turnDoesTurn() {
        final Coordinate initialCoord = new Coordinate(77, 88);
        final MowerState initialState = new MowerState(Direction.N, initialCoord);

        assertEquals(Direction.W, Instruction.L.exec.apply(initialState).dir());
        assertEquals(Direction.E, Instruction.R.exec.apply(initialState).dir());
    }

    @Test
    void moveDoesNotTurn() {
        final Coordinate initialCoord = new Coordinate(21, 83);
        final MowerState initialState = new MowerState(Direction.S, initialCoord);

        assertEquals(Direction.S, Instruction.F.exec.apply(initialState).dir());
    }

    @Test
    void moveDoesMove() {
        final Coordinate initialCoord = new Coordinate(21, 83);
        final MowerState initialState = new MowerState(Direction.S, initialCoord);

        final MowerState movedSouth = Instruction.F.exec.apply(initialState);
        assertEquals(new Coordinate(21, 82), movedSouth.coord());
        final MowerState movedEast = Instruction.F.exec.apply(Instruction.L.exec.apply(movedSouth));
        assertEquals(new Coordinate(22, 82), movedEast.coord());
        final MowerState movedEastAgain = Instruction.F.exec.apply(movedEast);
        assertEquals(new Coordinate(23, 82), movedEastAgain.coord());

        assertEquals(new MowerState(Direction.W, new Coordinate(5, 40)), Instruction.F.exec.apply(new MowerState(Direction.W, new Coordinate(6, 40))));
    }

}