package lam.logic.validation;

import lam.records.Coordinate;
import lam.records.Lawn;

public class CoordinateValidation {

    private CoordinateValidation() {}

    public static boolean isValid(Coordinate coordinate, Lawn lawn) {
        if (coordinate == null)
            throw new IllegalArgumentException("Coordinate given was null");
        if (lawn == null)
            throw new IllegalArgumentException("Lawn given was null");

        return new LawnValidation().isValid(lawn) &&
                coordinate.x() >= 0 &&
                coordinate.y() >= 0 &&
                coordinate.x() < lawn.width() &&
                coordinate.y() < lawn.height();
    }

}
