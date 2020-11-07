package lam.records;

import lam.Mower;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MowingSession(@NotNull Lawn lawn, @NotNull List<Mower> mowers) {
}