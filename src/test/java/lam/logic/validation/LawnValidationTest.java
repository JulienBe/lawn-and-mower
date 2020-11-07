package lam.logic.validation;

import lam.records.Lawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LawnValidationTest {

    @Test
    void nullTest() {
        assertThrows(IllegalArgumentException.class, () -> new LawnValidation().isValid(null));
    }

    @Test
    void tooSmall() {
        assertThrows(IllegalArgumentException.class,  () -> {
            new LawnValidation().isValid(new Lawn(0, 22));
        });
        assertThrows(IllegalArgumentException.class,  () -> {
            new LawnValidation().isValid(new Lawn(22, 0));
        });
        assertThrows(IllegalArgumentException.class,  () -> {
            new LawnValidation().isValid(new Lawn(Integer.MIN_VALUE, 1));
        });
    }

    @Test
    void validLawn() {
        new LawnValidation().isValid(new Lawn(1, 1));
        new LawnValidation().isValid(new Lawn(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

}