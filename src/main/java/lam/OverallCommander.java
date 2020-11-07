package lam;

import lam.records.MowerState;
import lam.records.MowingSession;

import java.util.List;
import java.util.stream.Collectors;

public class OverallCommander {
    private final MowingSession session;

    public OverallCommander(MowingSession session) {
        this.session = session;
    }

    public void runFrom(int from) {
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
