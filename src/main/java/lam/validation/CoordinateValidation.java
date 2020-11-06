package lam.validation;

import lam.data.Coordinate;
import lam.data.Lawn;
import org.jetbrains.annotations.NotNull;

public class CoordinateValidation {

    public static boolean isValid(@NotNull Coordinate coordinate, @NotNull Lawn lawn) {
        if (new LawnValidation().isValid(lawn) &&
                coordinate.x() >= 0 &&
                coordinate.y() >= 0 &&
                coordinate.x() < lawn.width() &&
                coordinate.y() < lawn.height())
            return true;
        return false;
    }

}
