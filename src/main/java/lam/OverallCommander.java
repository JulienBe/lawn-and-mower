package lam;

import lam.records.MowerState;
import lam.records.MowingSession;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OverallCommander {
    private final MowingSession session;

    public OverallCommander(MowingSession session) {
        this.session = session;
    }

    public void exec() {
        List<MowerState> collisions;
        do {
            // TODO: don't start from scratch either
            runFrom(0);
            List<List<MowerState>> paths = session
                    .mowers()
                    .parallelStream()
                    .map(Mower::getStates)
                    .collect(Collectors.toList());
            int longest = paths
                    .parallelStream()
                    .mapToInt(List::size)
                    .max()
                    .getAsInt();
            collisions = detectEarliestCollision(paths, longest);
            collisions
                    .parallelStream()
                    .forEach(mowerState -> {
                        handleCollisionState(mowerState, session.mowers());
                    });
        } while (!collisions.isEmpty());
    }

    private void handleCollisionState(MowerState mowerState, List<Mower> mowers) {
        var collidingMower = mowers.get(mowerState.mowerIndex());
        collidingMower.removeInstruction(mowerState.nextInstructionCpt() - 1);
    }

    // TODO: don't start from scratch each time
    private List<MowerState> detectEarliestCollision(List<List<MowerState>> paths, int longest) {
        for (int turn = 0; turn < longest; turn++) {
            int currentTurn = turn;
            List<MowerState> turnsToCompare = paths
                    .parallelStream()
                    .filter(p -> p.size() > currentTurn)
                    .map(p -> p.get(currentTurn))
                    .collect(Collectors.toList());

            if (turnsToCompare.size() <= 1)
                break;

            var collisionList = isThereACollision(turnsToCompare);
            if (!collisionList.isEmpty())
                return collisionList;
        }
        return Collections.emptyList();
    }

    private List<MowerState> isThereACollision(List<MowerState> turns) {
        return turns
                .parallelStream()
                .collect(Collectors.groupingBy(MowerState::coord))
                .values().parallelStream()
                .filter(listAtThisCoord -> listAtThisCoord.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private void runFrom(int from) {
        session.mowers()
                .parallelStream()
                .forEach(m -> m.processFrom(from));
    }
    public List<MowerState> results() {
        return session.mowers()
                .stream()
                .map(Mower::lastState)
                .collect(Collectors.toList());
    }
}