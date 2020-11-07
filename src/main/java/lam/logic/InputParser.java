package lam.logic;

import lam.Mower;
import lam.enums.Direction;
import lam.enums.Instruction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowingSession;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputParser {

    private static final Map<String, Direction> directionMapping = Map.of(
            "N", Direction.N,
            "E", Direction.E,
            "S", Direction.S,
            "W", Direction.W
    );
    private static final Map<Character, Instruction> instructionMapping = Map.of(
            'L', Instruction.L,
            'R', Instruction.R,
            'F', Instruction.F
    );

    private InputParser() {}

    public static @NotNull MowingSession init(@NotNull String wholeInput) {
        wholeInput = InputManipulator.validateInput(wholeInput);
        Lawn lawn = convertLawnString(wholeInput);
        List<Mower> mowers = convertMowerStrings(wholeInput, lawn);
        return new MowingSession(lawn, mowers);
    }

    private static @NotNull List<Mower> convertMowerStrings(@NotNull String input, @NotNull Lawn lawn) {
        // order need to be kept for output
        return InputManipulator.extractMowers(input)
                .stream()
                .map(s -> parseMowerLine(lawn, s))
                .collect(Collectors.toList());
    }

    @NotNull
    private static Mower parseMowerLine(@NotNull Lawn lawn, String s) {
        String[] lines = InputManipulator.splitPerNewLine(s);

        List<Integer> coord = InputManipulator.extractCoord(lines[0])
                .stream()
                .map(InputParser::parseInt)
                .collect(Collectors.toList());
        Direction dir = directionMapping.get(InputManipulator.extractDirection(lines[0]));
        List<Instruction> instructions = InputManipulator.extractInstructions(lines[1])
                .chars()
                .mapToObj(value -> (char) value)
                .map(instructionMapping::get)
                .collect(Collectors.toList());

        return new Mower(new Coordinate(coord.get(0), coord.get(1)), dir, instructions, lawn);
    }

    private static @NotNull Lawn convertLawnString(@NotNull String input) {
        String lawnStr = InputManipulator.extractLawn(input);
        List<String> dimensions = InputManipulator.extractCoord(lawnStr);
        return new Lawn(parseInt(dimensions.get(0)), parseInt(dimensions.get(1)));
    }

    private static int parseInt(@NotNull String s) {
        return Integer.parseInt(s);
    }
}