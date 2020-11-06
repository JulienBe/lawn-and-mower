package lam.validation;

import lam.data.Coordinate;
import lam.data.Lawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateValidationTest {

    @Test
    void nullTest() {
        assertThrows(IllegalArgumentException.class,  () -> {
            CoordinateValidation.isValid(null, new Lawn(1, 1));
        });
        assertThrows(IllegalArgumentException.class,  () -> {
            CoordinateValidation.isValid(new Coordinate(1, 1), null);
        });
    }

    @Test
    void outOfBounds() {
        assertFalse(CoordinateValidation.isValid(new Coordinate(-1, 3), new Lawn(5, 4)));
        assertFalse(CoordinateValidation.isValid(new Coordinate(1, 4),  new Lawn(5, 4)));
        assertFalse(CoordinateValidation.isValid(new Coordinate(4, 1),  new Lawn(4, 5)));
        assertFalse(CoordinateValidation.isValid(new Coordinate(3, -1), new Lawn(5, 4)));
    }

    @Test
    void inBounds() {
        assertTrue(CoordinateValidation.isValid(new Coordinate(0, 3), new Lawn(5, 4)));
        assertTrue(CoordinateValidation.isValid(new Coordinate(1, 4), new Lawn(5, 5)));
        assertTrue(CoordinateValidation.isValid(new Coordinate(4, 1), new Lawn(5, 5)));
        assertTrue(CoordinateValidation.isValid(new Coordinate(3, 0), new Lawn(5, 4)));
    }

}