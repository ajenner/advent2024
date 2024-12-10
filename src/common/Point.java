package common;

import java.util.EnumSet;

public record Point(int x, int y) {

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT,
        UPRIGHT,
        UPLEFT,
        DOWNRIGHT,
        DOWNLEFT;

        public static final EnumSet<Direction> ORTHOGONALS = EnumSet.of(UP, RIGHT, DOWN, LEFT);
        public static final EnumSet<Direction> DIAGONALS = EnumSet.of(UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT);
    }

    public boolean inBounds (int maxWidth, int maxHeight) {
        return this.x >= 0 && this.x < maxWidth && this.y >= 0 && this.y < maxHeight;
    }

    public Point moveUp () {
        return new Point(this.x, this.y - 1);
    }

    public Point moveRight () {
        return new Point(this.x + 1, this.y);
    }

    public Point moveDown () {
        return new Point(this.x, this.y + 1);
    }

    public Point moveLeft () {
        return new Point(this.x - 1, this.y);
    }

    public Point moveUpRight () {
        return new Point(this.x + 1, this.y - 1);
    }

    public Point moveUpLeft () {
        return new Point(this.x - 1, this.y - 1);
    }

    public Point moveDownRight () {
        return new Point(this.x + 1, this.y + 1);
    }

    public Point moveDownLeft () {
        return new Point(this.x - 1, this.y + 1);
    }

    public Point moveDirection (Direction direction) {
        return switch (direction) {
            case UP -> moveUp();
            case RIGHT -> moveRight();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
            case UPRIGHT -> moveUpRight();
            case UPLEFT -> moveUpLeft();
            case DOWNRIGHT -> moveDownRight();
            case DOWNLEFT -> moveDownLeft();
        };
    }

    public Direction rotateDirectionLeft90Degrees(Direction direction) {
        return switch (direction) {
            case UP -> Direction.LEFT;
            case RIGHT -> Direction.UP;
            case DOWN -> Direction.RIGHT;
            case LEFT -> Direction.DOWN;
            case UPRIGHT -> Direction.UPLEFT;
            case UPLEFT -> Direction.DOWNLEFT;
            case DOWNRIGHT -> Direction.UPRIGHT;
            case DOWNLEFT -> Direction.DOWNRIGHT;
        };
    }

    public Direction rotateDirectionRight90Degrees(Direction direction) {
        return switch (direction) {
            case UP -> Direction.RIGHT;
            case RIGHT -> Direction.DOWN;
            case DOWN -> Direction.LEFT;
            case LEFT -> Direction.UP;
            case UPRIGHT -> Direction.DOWNRIGHT;
            case UPLEFT -> Direction.UPRIGHT;
            case DOWNRIGHT -> Direction.DOWNLEFT;
            case DOWNLEFT -> Direction.UPLEFT;
        };
    }

    public int manhattanDistance (Point p) {
        return Math.abs(this.x - p.x) + Math.abs(this.y - p.y);
    }

    public double slope (Point p) {
        if (this.x - p.x == 0) {
            return Integer.MAX_VALUE;
        }
        return (double) (this.y - p.y) / (this.x - p.x);
    }

}
