package lam.logic;

import lam.records.FinishStatusAndState;
import lam.records.MowerState;
import lam.records.MowingSession;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OverallCommander {
    private static final Logger LOG = Logger.getLogger(OverallCommander.class);
    private final MowingSession session;

    public OverallCommander(MowingSession session) {
        this.session = session;
    }

    public List<String> exec() {
        long startTime = System.currentTimeMillis();
        Rollback rollback;
        int bumps = -1;

        do {
            session.mowers()
                    .parallelStream()
                    .forEach(Mower::processFromLastState);

            List<List<MowerState>> allMowersPaths = session
                    .mowers()
                    .parallelStream()
                    .map(Mower::getStates)
                    .collect(Collectors.toList());

            int longestPathLength = allMowersPaths
                    .parallelStream()
                    .mapToInt(List::size)
                    .max()
                    .getAsInt();
            rollback = detectEarliestInvalidState(allMowersPaths, longestPathLength);

            rollback.invalidStates()
                    .parallelStream()
                    .forEach(it -> handleInvalidState(it.state(), session.mowers()));

            rollback.valid()
                    .parallelStream()
                    .forEach(it -> rollbackTo(it.state(), session.mowers()));
            bumps++;
        } while (!rollback.invalidStates().isEmpty());

        var lastStatesAsString = session.mowers()
                .stream()
                .map(Mower::lastState)
                .map(state -> state.coord().x() + " " + state.coord().y() + " " + state.dir())
                .collect(Collectors.toList());

        LOG.info("Execution time: " + (System.currentTimeMillis() - startTime) + " with " + bumps + " bumps");
        return lastStatesAsString;
    }

    private void handleInvalidState(MowerState state, List<Mower> mowers) {
        var collidingMower = mowers.get(state.mowerIndex());
        collidingMower.skipInstruction(state);
    }
    private void rollbackTo(MowerState state, List<Mower> mowers) {
        var collidingMower = mowers.get(state.mowerIndex());
        collidingMower.rollbackTo(state);
    }

    // TODO: don't start from scratch each time
    private Rollback detectEarliestInvalidState(List<List<MowerState>> paths, int longest) {
        for (int turn = 0; turn < longest; turn++) {
            int finalTurn = turn;
            List<FinishStatusAndState> turnsToCompare = paths
                    .parallelStream()
                    .map(p -> new FinishStatusAndState(isPastItsLastTurn(finalTurn, p), p.get(Math.min(finalTurn, p.size() - 1))))
                    .collect(Collectors.toList());

            if (turnsToCompare.size() <= 1)
                break;

            List<FinishStatusAndState> invalids = new ArrayList<>();
            invalids.addAll(StateValidator.getAllCollisions(turnsToCompare
                    .parallelStream()
                    .collect(Collectors.toList())));

            invalids.addAll(StateValidator.getAllOOB(turnsToCompare
                    .parallelStream()
                    .collect(Collectors.toList()), session.lawn()));

            invalids = invalids.parallelStream()
                    .filter(it -> !it.finished())
                    .collect(Collectors.toList());
            if (!invalids.isEmpty()) {
                turnsToCompare.removeAll(invalids);
                return new Rollback(invalids, turnsToCompare);
            }
        }
        return new Rollback(Collections.emptyList(), Collections.emptyList());
    }

    private boolean isPastItsLastTurn(int finalTurn, List<MowerState> p) {
        return finalTurn > p.size();
    }

}

record Rollback(List<FinishStatusAndState> invalidStates, List<FinishStatusAndState> valid){}