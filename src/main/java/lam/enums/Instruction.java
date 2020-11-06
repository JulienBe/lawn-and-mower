package lam.enums;

import lam.records.Coordinate;
import lam.records.MowerState;

import java.util.function.Function;

public enum Instruction {

    L(state -> new MowerState(state.dir().turnCCW(), state.coord())),
    R(state -> new MowerState(state.dir().turnCW(),  state.coord())),
    F(state -> new MowerState(state.dir(),           new Coordinate(state.coord().x() + state.dir().xOffset, state.coord().y() + state.dir().yOffset)));

    /**
     * A null to apply will crash it
     */
    public final Function<MowerState, MowerState> exec;

    Instruction(Function<MowerState, MowerState> exec) {
        this.exec = exec;
    }
}