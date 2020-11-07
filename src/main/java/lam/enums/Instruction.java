package lam.enums;

import lam.records.Coordinate;
import lam.records.MowerState;

import java.util.function.UnaryOperator;

public enum Instruction {

    L(state -> new MowerState(state.dir().turnCCW(), state.coord(), state.nextInstructionCpt() + 1, state.mowerIndex())),
    R(state -> new MowerState(state.dir().turnCW(),  state.coord(), state.nextInstructionCpt() + 1, state.mowerIndex())),
    F(state -> new MowerState(state.dir(),           new Coordinate(state.coord().x() + state.dir().xOffset, state.coord().y() + state.dir().yOffset), state.nextInstructionCpt() + 1, state.mowerIndex()));

    /**
     * A null to apply will crash it
     */
    public final UnaryOperator<MowerState> exec;

    Instruction(UnaryOperator<MowerState> exec) {
        this.exec = exec;
    }
}