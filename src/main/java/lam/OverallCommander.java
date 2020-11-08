package lam;

import lam.records.MowerState;
import lam.records.MowingSession;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OverallCommander {
    private final MowingSession session;

    public OverallCommander(MowingSession session) {
        this.session = session;
    }

    public List<String> exec() {
        List<MowerState> allCollidingStates;
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
            allCollidingStates = detectEarliestCollision(allMowersPaths, longestPathLength);

            allCollidingStates
                    .parallelStream()
                    .forEach(mowerState -> handleCollisionState(mowerState, session.mowers()));
        } while (!allCollidingStates.isEmpty());
        return session.mowers()
                .stream()
                .map(Mower::lastState)
                .map(state -> state.coord().x() + " " + state.coord().y() + " " + state.dir())
                .collect(Collectors.toList());
    }

    private void handleCollisionState(MowerState mowerState, List<Mower> mowers) {
        var collidingMower = mowers.get(mowerState.mowerIndex());
        collidingMower.skipInstruction(mowerState);
    }

    // TODO: don't start from scratch each time
    private List<MowerState> detectEarliestCollision(List<List<MowerState>> paths, int longest) {
        for (int turn = 0; turn < longest; turn++) {
            List<MowerState> turnsToCompare = paths
                    .parallelStream()
                    .map(getCorrespondingState(turn))
                    .collect(Collectors.toList());

            if (turnsToCompare.size() <= 1)
                break;

            var collisionList = new CollisionDetector().isThereACollision(turnsToCompare);
            if (!collisionList.isEmpty())
                return collisionList;
        }
        return Collections.emptyList();
    }

    private @NotNull Function<List<MowerState>, MowerState> getCorrespondingState(int currentTurn) {
        return p -> p.get(Math.min(currentTurn, p.size() - 1));
    }
}