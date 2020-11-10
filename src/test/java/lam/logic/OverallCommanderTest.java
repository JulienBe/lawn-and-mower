package lam.logic;

import lam.enums.Direction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowingSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static lam.enums.Instruction.F;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OverallCommanderTest {

    @Test
    void crosspath() {
        var fileContent = new TestUtilities().getValid("crosspath");
        var result = new OverallCommander(new SessionCreatorAutomata().processInput(fileContent)).exec();
        assertEquals("1 0 N", result.get(0));
        assertEquals("1 2 S", result.get(1));
        assertEquals("2 1 W", result.get(2));
        assertEquals("1 1 E", result.get(3));
        assertEquals("0 1 N", result.get(4));
        assertEquals("2 2 S", result.get(5));
    }
    @Test
    void crosspath2f() {
        var fileContent = new TestUtilities().getValid("crosspath_2f");
        var result = new OverallCommander(new SessionCreatorAutomata().processInput(fileContent)).exec();
        assertEquals("1 0 N", result.get(0));
        assertEquals("1 2 S", result.get(1));
        assertEquals("2 1 W", result.get(2));
        assertEquals("1 1 E", result.get(3));
        assertEquals("0 2 N", result.get(4));
        assertEquals("2 2 S", result.get(5));
    }

    /**
     * // 0
     * 3 2 W
     * F
     * 3 1 N
     * FLFF
     *
     * // 1
     * 2 2 W
     *
     * 3 2 N
     * LFF
     *
     * // 2
     * 2 2 W
     * 3 2 W
     * FF
     *
     * // 3
     * 2 2 W
     * 2 2 W -> 3 2 W
     */
    @Test
    void swap() {
        var fileContent = new TestUtilities().getValid("swap");
        var result = new OverallCommander(new SessionCreatorAutomata().processInput(fileContent)).exec();
        assertEquals("2 2 W", result.get(0));
        assertEquals("3 2 W", result.get(1));
    }

    /**
     * 3 3
     * 3 1 N
     * FLF
     * 3 0 E
     * FFFLFFLFF
     *
     * 3 2 N
     * LF
     * 3 0 N
     * FFLFF
     *
     * 3 2 W
     * F
     * 3 1 N
     * FLFF
     *
     * 2 2 W
     * 3 2 N
     * LFF
     *
     * 2 2 W
     * 3 2 W
     */
    @Test
    void noActualMoves() {
        var fileContent = new TestUtilities().getValid("instructions_invalid");
        var result = new OverallCommander(new SessionCreatorAutomata().processInput(fileContent)).exec();
        assertEquals("2 2 W", result.get(0));
        assertEquals("3 2 W", result.get(1));
    }

    @Test
    void bumperMowers() {
        var fileContent = new TestUtilities().getValid("bumper_mowers");
        var result = new OverallCommander(new SessionCreatorAutomata().processInput(fileContent)).exec();
        assertEquals("0 2 S", result.get(0));
        assertEquals("0 4 E", result.get(1));
        assertEquals("5 5 N", result.get(2));
        assertEquals("4 4 W", result.get(3));
    }

    @Test
    void obstacle() {
        var fileContent = new TestUtilities().getValid("obstacle");
        var result = new OverallCommander(new SessionCreatorAutomata().processInput(fileContent)).exec();
        assertEquals("5 5 N", result.get(0));
        assertEquals("4 4 W", result.get(1));
    }

    @Test
    void givenExample() {
        MowingSession session = TestUtilities.getExampleSession();
        List<String> results = getMowerStates(session.lawn(), session.mowers().get(0), session.mowers().get(1));
        assertEquals("1 3 N", results.get(0));
        assertEquals("5 1 E", results.get(1));
    }

    @Test
    void oneInstruction() {
        Lawn lawn = new Lawn(4, 5);
        Mower mower1 = new Mower(new Coordinate(2, 4), Direction.S, Arrays.asList(F), lawn, 0);
        Mower mower2 = new Mower(new Coordinate(2, 0), Direction.N, Arrays.asList(F), lawn, 1);
        List<String> results = getMowerStates(lawn, mower1, mower2);
        assertEquals("2 3 S", results.get(mower1.index));
        assertEquals("2 1 N", results.get(mower2.index));
    }

    @Test
    void equalBump() {
        Lawn lawn = new Lawn(4, 5);
        Mower mower1 = new Mower(new Coordinate(0, 4), Direction.S, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 0);
        Mower mower2 = new Mower(new Coordinate(0, 0), Direction.N, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 1);
        List<String> results = getMowerStates(lawn, mower1, mower2);
        assertEquals("0 3 S", results.get(mower1.index));
        assertEquals("0 1 N", results.get(mower2.index));
    }

    @Test
    void oneDiffBump() {
        Lawn lawn = new Lawn(4, 5);
        Mower mower1 = new Mower(new Coordinate(0, 4), Direction.S, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 0);
        Mower mower2 = new Mower(new Coordinate(0, 0), Direction.N, Arrays.asList(F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F,F), lawn, 1);
        List<String> results = getMowerStates(lawn, mower1, mower2);
        assertEquals("0 2 S", results.get(mower1.index));
        assertEquals("0 1 N", results.get(mower2.index));
    }

    /**
     * 0 4 S
     * FFFF
     * 0 0 N
     * FF
     *
     * 0 3 S
     * FFF
     * 0 1 N
     * F
     *
     * 0 3 S
     * FF
     * 0 1 N
     *
     * 0 2 S
     * F
     * 0 1 N
     *
     * 0 2 S
     * 0 1 N
     */
    @Test
    void twoDiffBump() {
        Lawn lawn = new Lawn(4, 5);
        // can go freely to 0 3. bump until it has two instructions left. Should get it to 0 2, then bump back to 0 2 again
        Mower mower1 = new Mower(new Coordinate(0, 4), Direction.S, Arrays.asList(F,F,F,F), lawn, 0);
        // can go freely to 0 1. always bump
        Mower mower2 = new Mower(new Coordinate(0, 0), Direction.N, Arrays.asList(F,F), lawn, 1);
        List<String> results = getMowerStates(lawn, mower1, mower2);
        assertEquals("0 2 S", results.get(mower1.index));
        assertEquals("0 1 N", results.get(mower2.index));
    }
    @Test
    void fourDiffBump() {
        Lawn lawn = new Lawn(4, 5);
        // can go freely to 0 3. bump until it has two instructions left. Should get it to 0 2, then bump back to 0 2 again
        Mower mower1 = new Mower(new Coordinate(0, 4), Direction.S, Arrays.asList(F,F,F,F,F,F), lawn, 0);
        // can go freely to 0 1. always bump
        Mower mower2 = new Mower(new Coordinate(0, 0), Direction.N, Arrays.asList(F,F), lawn, 1);
        List<String> results = getMowerStates(lawn, mower1, mower2);
        assertEquals("0 2 S", results.get(mower1.index));
        assertEquals("0 1 N", results.get(mower2.index));
    }

    private List<String> getMowerStates(Lawn lawn, Mower mower1, Mower mower2) {
        MowingSession session = new MowingSession(lawn, Arrays.asList(mower1, mower2));
        return new OverallCommander(session).exec();
    }

}