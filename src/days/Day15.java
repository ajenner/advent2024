package days;

import common.Point;
import templates.DayTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Day15 extends DayTemplate {
    static HashSet<Point> walls;
    Robot robot;
    static HashSet<Box> boxes;
    ArrayList<Point.Direction> instructions;
    static int maxX = 0;
    static int maxY = 0;

    private void buildMapLinePart1(ArrayList<String> inputs, int y) {
        for (int x = 0; x < inputs.get(y).length(); x++) {
            if (maxX == 0) {
                maxX = inputs.get(y).length();
            }
            var currentChar = inputs.get(y).charAt(x);
            if (currentChar == '#') {
                walls.add(new Point(x, y));
            }
            if (currentChar == 'O') {
                var box = new Box(new HashSet<>());
                box.points.add(new Point(x, y));
                boxes.add(box);
            }
            if (currentChar == '@') {
                robot = new Robot(new Point(x, y));
            }
        }

    }

    private void buildMapLinePart2(ArrayList<String> inputs, int y, int twiceX) {
        for (int x = 0; x < inputs.get(y).length(); x++) {
            if (maxX == 0) {
                maxX = inputs.get(y).length() * 2;
            }
            var currentChar = inputs.get(y).charAt(x);
            if (currentChar == '#') {
                walls.add(new Point(twiceX, y));
                walls.add(new Point(twiceX + 1, y));
            }
            if (currentChar == 'O') {
                var box = new Box(new HashSet<>());
                box.points.add(new Point(twiceX, y));
                box.points.add(new Point(twiceX + 1, y));
                boxes.add(box);
            }
            if (currentChar == '@') {
                robot = new Robot(new Point(twiceX, y));
            }
            twiceX += 2;
        }
    }

    private void buildObjects(ArrayList<String> inputs, boolean part1) {
        maxX = 0;
        maxY = 0;
        walls = new HashSet<>();
        boxes = new HashSet<>();
        instructions = new ArrayList<>();
        for (int y = 0; y < inputs.size(); y++) {
            if (inputs.get(y).contains("#")) {
                int twiceX = 0;
                if (part1) {
                    buildMapLinePart1(inputs, y);
                } else {
                    buildMapLinePart2(inputs, y, twiceX);
                }
            }
            if (inputs.get(y).contains("<") || inputs.get(y).contains("v") || inputs.get(y).contains(">") || inputs.get(y).contains("^")) {
                if (maxY == 0) {
                    maxY = y - 1;
                }
                for (int x = 0; x < inputs.get(y).length(); x++) {
                    var currentChar = inputs.get(y).charAt(x);
                    if (currentChar == '<') {
                        instructions.add(Point.Direction.LEFT);
                    } else if (currentChar == '^') {
                        instructions.add(Point.Direction.UP);
                    } else if (currentChar == '>') {
                        instructions.add(Point.Direction.RIGHT);
                    } else if (currentChar == 'v') {
                        instructions.add(Point.Direction.DOWN);
                    }
                }
            }
        }
    }

    private void moveRobot() {
        for (Point.Direction instruction : instructions) {
            robot = robot.moveDirection(instruction);
        }
    }

    private Long calculateGPS() {
        return boxes.stream().mapToLong(Box::calculateGPS).sum();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildObjects(inputs, part1);
        moveRobot();
        return calculateGPS().toString();
    }

    record Robot(Point point) {
        public Robot moveDirection(Point.Direction direction) {
            var nextPoint = point.moveDirection(direction);
            if (nextPoint.inBounds(maxX, maxY) && !walls.contains(nextPoint)) {
                if (boxes.stream().anyMatch(box -> box.containsPoint(nextPoint))) {
                    var matchingBox = boxes.stream().filter(box -> box.containsPoint(nextPoint)).findFirst();
                    if (matchingBox.isPresent()) {
                        var nextBox = matchingBox.get();
                        if (!nextBox.canMoveBox(direction)) {
                            return this;
                        }
                        nextBox.moveBox(direction);
                    }
                }
                return new Robot(nextPoint);
            }
            return this;
        }
    }

    record Box(HashSet<Point> points) {

        public long calculateGPS() {
            Point minXPoint = null;
            for (Point p : points) {
                if (minXPoint == null) {
                    minXPoint = p;
                } else if (minXPoint.x() > p.x()) {
                    minXPoint = p;
                }
            }
            return 100L * minXPoint.y() + minXPoint.x();
        }

        public boolean containsPoint(Point p) {
            return points.contains(p);
        }

        public boolean leftPoint(Point point) {
            if (points.contains(point)) {
                for (Point p : points) {
                    if (p.equals(point)) {
                        continue;
                    }
                    return point.x() < p.x();
                }
            }
            return false;
        }

        public boolean rightPoint(Point point) {
            if (points.contains(point)) {
                for (Point p : points) {
                    if (p.equals(point)) {
                        continue;
                    }
                    return point.x() > p.x();
                }
            }
            return false;
        }

        boolean canMoveBox(Point.Direction direction) {
            for (Point p : points) {
                var nextPoint = p.moveDirection(direction);
                if (!nextPoint.inBounds(maxX, maxY) || walls.contains(nextPoint)) {
                    return false;
                }
                var matchingBox = boxes.stream().filter(box -> box.containsPoint(nextPoint) && !box.equals(this)).findFirst();
                if (matchingBox.isPresent()) {
                    var nextBox = matchingBox.get();
                    boolean valid = nextBox.canMoveBox(direction);
                    if (!valid) {
                        return false;
                    }
                }
            }
            return true;
        }

        void moveBox(Point.Direction direction) {
            boxes.remove(this);
            var thisNewBox = new Box(new HashSet<>());
            for (Point p : points) {
                var nextPoint = p.moveDirection(direction);
                thisNewBox.points.add(nextPoint);
                var matchingBox = boxes.stream().filter(box -> box.containsPoint(nextPoint) && !box.equals(this)).findFirst();
                if (matchingBox.isPresent()) {
                    var nextBox = matchingBox.get();
                    nextBox.moveBox(direction);
                }
            }
            boxes.add(thisNewBox);
        }
    }

    private void printMap(Point.Direction direction) {
        System.out.println(direction);
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                var point = new Point(x, y);
                if (Objects.equals(robot.point, point)) {
                    System.out.print("@");
                } else if (walls.contains(point)) {
                    System.out.print("#");
                } else if (boxes.stream().anyMatch(box -> box.leftPoint(point))) {
                    System.out.print("[");
                } else if (boxes.stream().anyMatch(box -> box.rightPoint(point))) {
                    System.out.print("]");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
