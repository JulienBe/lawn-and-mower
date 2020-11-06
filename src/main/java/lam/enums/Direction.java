package lam.enums;

public enum Direction {

    N(0, 1), E(1, 0), S(0, -1), W(-1, 0);

    final int xOffset;
    final int yOffset;

    Direction(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public Direction turnCW() {
        return turn(1);
    }
    public Direction turnCCW() {
        return turn(-1);
    }
    private Direction turn(int dir) {
        assert dir != 0;
        // % takes care of > length. Adding the length take care of < 0
        return Direction.values()[(Direction.values().length + ordinal() + (dir > 0 ? 1 : -1)) % Direction.values().length];
    }
}
