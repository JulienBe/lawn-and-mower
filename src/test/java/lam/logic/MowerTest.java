package lam.logic;

import lam.enums.Direction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowerState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static lam.enums.Instruction.F;
import static lam.enums.Instruction.R;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MowerTest {

    @Test
    void inBounds() {
        {
            Mower m = new Mower(new Coordinate(1, 2), Direction.E, Arrays.asList(F, F, F), new Lawn(5, 5), 0);
            m.processFromLastState();
            assertEquals(new MowerState(Direction.E, new Coordinate(4, 2), 3, 0), m.lastState());
        }
        {                                                           // 1 3  1 4  1 4 E  2 4  3 4
            Mower m = new Mower(new Coordinate(1, 2), Direction.N, Arrays.asList(F, F, R, F, F), new Lawn(5, 5), 1);
            m.processFromLastState();
            assertEquals(new MowerState(Direction.E, new Coordinate(3, 4), 5, 1), m.lastState());
        }
    }

    @Test
    void outOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(1, 6), Direction.E, Collections.singletonList(F), new Lawn(6, 6), 0));
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(6, 1), Direction.E, Collections.singletonList(F), new Lawn(6, 6), 0));
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(3, -3), Direction.E, Collections.singletonList(F), new Lawn(6, 6), 0));
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(-3, 3), Direction.E, Collections.singletonList(F), new Lawn(6, 6), 0));
    }

}