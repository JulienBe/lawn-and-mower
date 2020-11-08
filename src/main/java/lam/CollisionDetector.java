package lam;

import lam.records.MowerState;

import java.util.List;
import java.util.stream.Collectors;

public class CollisionDetector {
    public List<MowerState> isThereACollision(List<MowerState> turns) {
        return turns
                .parallelStream()
                .collect(Collectors.groupingBy(MowerState::coord))
                .values().parallelStream()
                .filter(listAtThisCoord -> listAtThisCoord.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
