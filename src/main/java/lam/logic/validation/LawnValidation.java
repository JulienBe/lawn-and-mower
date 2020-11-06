package lam.logic.validation;

import lam.records.Lawn;
import org.jetbrains.annotations.NotNull;

public class LawnValidation {
    public boolean isValid(@NotNull Lawn lawn) {
        if (lawn.width() > 0 && lawn.height() > 0)
            return true;
        throw new IllegalArgumentException("width and height of the lawn should be >= 0. Lawn is " + lawn);
    }
}
