package lam;

import lam.enums.Direction;
import lam.enums.Instruction;
import lam.logic.validation.CoordinateValidation;
import lam.records.Coordinate;
import lam.records.Lawn;
import lam.records.MowerState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.jboss.logging.Logger;

public class Mower {

    private static final Logger LOG = Logger.getLogger(Mower.class);

    private final List<Instruction> instructions = new LinkedList<>();
    private final List<MowerState> states = new LinkedList<>();
    private final Lawn lawn;

    public Mower(@NotNull Coordinate initialCoordinate, @NotNull Direction dir, @NotNull List<Instruction> instructions, @NotNull Lawn lawn) {
        this.instructions.addAll(instructions);
        this.lawn = lawn;
        states.add(new MowerState(dir, initialCoordinate, 0));
    }

    /**
     * The mower will go to the position it was on this turn.
     * It will reprocess every instructions after those that led it there.
     * @param turnFrom
     */
    public synchronized void processFrom(int turnFrom) {
        if (turnFrom < 0)
            throw new IllegalArgumentException("Turn should not be < 0");
        if (turnFrom >= states.size()) {
            LOG.warn("your are trying to process turns that are still to happen... Great Scott!");
            return;
        }

        MowerState nextState = states.get(turnFrom);
        states.removeAll(new LinkedList<>(states.subList(turnFrom, states.size()))); // remove from turn and add it later. So if we remove everything the initial state is kept
        states.add(nextState);

        while (nextState.nextInstructionCpt() < instructions.size()) {
            var instruction = instructions.get(nextState.nextInstructionCpt());
            var stateCandidate = instruction.exec.apply(states.get(states.size() - 1));
            if (CoordinateValidation.isValid(stateCandidate.coord(), lawn))
                nextState = stateCandidate;
            else
                nextState = new MowerState(nextState.dir(), nextState.coord(), nextState.nextInstructionCpt() + 1);
            states.add(nextState);
        }
    }

    @Unmodifiable
    public List<MowerState> getStates() {
        // what if it gets modified when processFrom is called...
        return Collections.unmodifiableList(states); // ho gosh that accessibility management...
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mower)) return false;
        Mower mower = (Mower) o;
        return instructions.equals(mower.instructions) &&
                lawn.equals(mower.lawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructions, lawn);
    }
}
