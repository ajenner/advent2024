package common;

import days.Day10;

public record Point(int x, int y) {

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
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

    public Point moveDirection(Direction direction) {
        return switch (direction) {
            case UP -> moveUp();
            case RIGHT -> moveRight();
            case DOWN -> moveDown();
            case LEFT -> moveLeft();
        };
    }

}
