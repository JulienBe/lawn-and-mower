package lam.records;

import lam.enums.Direction;
import org.jetbrains.annotations.NotNull;

public final record MowerState(@NotNull Direction dir, @NotNull Coordinate coord, int nextInstructionCpt, int mowerIndex) {
}
