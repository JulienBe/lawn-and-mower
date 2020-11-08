package lam.logic;

import lam.enums.Direction;
import lam.enums.Instruction;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowingSession;
import org.jboss.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class SessionCreatorAutomata {

    protected static final Map<String, Direction> directionMapping = Map.of(
            "N", Direction.N,
            "E", Direction.E,
            "S", Direction.S,
            "W", Direction.W
    );
    protected static final Map<Character, Instruction> instructionMapping = Map.of(
            'L', Instruction.L,
            'R', Instruction.R,
            'F', Instruction.F
    );
    private static final Logger LOG = Logger.getLogger(SessionCreatorAutomata.class);
    protected Lawn lawn;
    protected Coordinate currentMowerCoord;
    protected Direction currentMowerDir;
    protected List<Mower> mowers = new ArrayList<>();
    private State state = State.LAWN;

    public @Nullable MowingSession processInput(String s) {
        try {
            s.lines().forEach(this::process);
            if (lawn != null && !mowers.isEmpty() && state == State.MOWER_POS_DIR) {
                return new MowingSession(lawn, mowers);
            }
        } catch (Exception e) {
            LOG.error("Could not create session for \n" + s + "\n at state " + state, e);
        }
        return null;
    }

    private void process(String line) {
        if (line == null)
            return;
        line = line.strip();
        state = State.values()[state.exec.apply(line.strip(), this)];
    }

    protected int parseInt(@NotNull String s) {
        return Integer.parseInt(s);
    }
}

enum State {
    LAWN((line, automata) -> {
        String lawnStr = InputManipulator.extractLawn(line);
        List<String> dimensions = InputManipulator.extractCoord(lawnStr);
        automata.lawn = new Lawn(parseInt(dimensions.get(0)) + 1, parseInt(dimensions.get(1)) + 1);
        return 1;
    }),
    MOWER_POS_DIR((line, automata) -> {
        String line1 = InputManipulator.extractMowerLine1(line);
        List<Integer> coord = InputManipulator.extractCoord(line1)
                .stream()
                .map(automata::parseInt)
                .collect(Collectors.toList());
        automata.currentMowerCoord = new Coordinate(coord.get(0), coord.get(1));
        automata.currentMowerDir = SessionCreatorAutomata.directionMapping.get(InputManipulator.extractDirection(line1));
        return 2;
    }),
    MOWER_INST((line, automata) -> {
        String line2 = InputManipulator.extractMowerLine2(line);
        List<Instruction> instructions = line2
                .chars()
                .mapToObj(value -> (char) value)
                .map(SessionCreatorAutomata.instructionMapping::get)
                .collect(Collectors.toList());
        automata.mowers.add(new Mower(automata.currentMowerCoord, automata.currentMowerDir, instructions, automata.lawn, automata.mowers.size()));
        return 1;
    });


    final BiFunction<String, SessionCreatorAutomata, Integer> exec;

    /**
     * @param exec return the index of the next state
     */
    State(BiFunction<String, SessionCreatorAutomata, Integer> exec) {
        this.exec = exec;
    }
}
