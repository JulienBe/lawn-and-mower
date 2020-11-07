package lam;

import lam.enums.Direction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowerState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static lam.enums.Instruction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MowerTest {

    @Test
    void inBounds() {
        {
            Mower m = new Mower(new Coordinate(1, 2), Direction.E, Arrays.asList(F, F, F), new Lawn(5, 5));
            for (int i = 0; i < 13; i++) {
                m.processFrom(i);
                var states = m.getStates();
                assertEquals(new MowerState(Direction.E, new Coordinate(4, 2), 3), states.get(states.size() - 1));
            }
        }
        {                                                           // 1 3  1 4  1 4 E  2 4  3 4
            Mower m = new Mower(new Coordinate(1, 2), Direction.N, Arrays.asList(F, F, R, F, F), new Lawn(5, 5));
            for (int i = 0; i < 13; i++) {
                m.processFrom(i);
                var states = m.getStates();
                assertEquals(new MowerState(Direction.E, new Coordinate(3, 4), 5), states.get(states.size() - 1));
            }
        }
    }

    @Test
    void outOfBounds() {
        // where no mower has gone before !
        {
            Mower m = new Mower(new Coordinate(1, 2), Direction.E, Arrays.asList(F, F, F, F, F, F), new Lawn(5, 5));
            m.processFrom(0);
            var states = m.getStates();
            assertEquals(new MowerState(Direction.E, new Coordinate(4, 2), 6), states.get(states.size() - 1));
        }
        {
            //1 1 S  1 0 S  1 0 S  1 0 W  0 0 W  0 0 W  0 0 N  0 1 N  0 1 E  1 1 E
            Mower m = new Mower(new Coordinate(1, 2), Direction.S, Arrays.asList(F, F, F, R, F, F, R, F, R, F),
                    new Lawn(6, 5));
            for (int i = 0; i < 13; i++) {
                m.processFrom(i);
                var states = m.getStates();
                assertEquals(new MowerState(Direction.E, new Coordinate(1, 1), 10), states.get(states.size() - 1));
            }
        }
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(1, 6), Direction.E, Collections.singletonList(F), new Lawn(6, 6)));
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(6, 1), Direction.E, Collections.singletonList(F), new Lawn(6, 6)));
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(3, -3), Direction.E, Collections.singletonList(F), new Lawn(6, 6)));
        assertThrows(IllegalArgumentException.class, () -> new Mower(new Coordinate(-3, 3), Direction.E, Collections.singletonList(F), new Lawn(6, 6)));
    }

}