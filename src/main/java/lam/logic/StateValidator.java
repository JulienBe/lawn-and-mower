package lam.logic;

import lam.logic.validation.CoordinateValidation;
import lam.records.FinishStatusAndState;
import lam.records.Lawn;
import lam.records.MowerState;

import java.util.List;
import java.util.stream.Collectors;

public class StateValidator {

    private StateValidator() {}

    public static List<FinishStatusAndState> getAllCollisions(List<FinishStatusAndState> turns) {
        return turns
                .parallelStream()
                .collect(Collectors.groupingBy(it -> it.state().coord()))
                .values().parallelStream()
                .filter(listAtThisCoord -> listAtThisCoord.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
    public static boolean isOOB(MowerState state, Lawn lawn) {
        return !CoordinateValidation.isValid(state.coord(), lawn);
    }

    public static List<FinishStatusAndState> getAllOOB(List<FinishStatusAndState> turnsToCompare, Lawn lawn) {
        return turnsToCompare
                .parallelStream()
                .filter(it -> StateValidator.isOOB(it.state(), lawn))
                .collect(Collectors.toList());
    }
}
