package lam.logic.validation;

import lam.records.Coordinate;
import lam.records.Lawn;
import org.jetbrains.annotations.NotNull;

public class CoordinateValidation {

    private CoordinateValidation() {}

    public static boolean isValid(@NotNull Coordinate coordinate, @NotNull Lawn lawn) {
        return new LawnValidation().isValid(lawn) &&
                coordinate.x() >= 0 &&
                coordinate.y() >= 0 &&
                coordinate.x() < lawn.width() &&
                coordinate.y() < lawn.height();
    }

}
