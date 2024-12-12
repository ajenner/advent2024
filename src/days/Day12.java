package days;

import common.Point;
import templates.DayTemplate;

import java.util.ArrayList;
import java.util.HashSet;

public class Day12 extends DayTemplate {

    HashSet<Region> regions;
    HashSet<Point> visited;

    private void buildRegions(ArrayList<String> inputs) {
        regions = new HashSet<>();
        visited = new HashSet<>();
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                var nextPoint = new Point(x, y);
                if (!visited.contains(nextPoint)) {
                    var currentChar = inputs.get(y).charAt(x);
                    var region = new Region(new HashSet<>(), currentChar);
                    regions.add(region);
                    populateRegion(region, currentChar, nextPoint, inputs);
                }
            }
        }
    }

    private void populateRegion(Region currentRegion, char currentChar, Point currentPoint, ArrayList<String> inputs) {
        currentRegion.points.add(currentPoint);
        visited.add(currentPoint);
        for (Point.Direction dir : Point.Direction.ORTHOGONALS) {
            var nextPoint = validateNextPoint(currentPoint.moveDirection(dir), currentChar, inputs);
            if (nextPoint != null && !currentRegion.points.contains(nextPoint)) {
                populateRegion(currentRegion, currentChar, nextPoint, inputs);
            }
        }
    }

    private Point validateNextPoint(Point nextPoint, char currentChar, ArrayList<String> inputs) {
        if (nextPoint.inBounds(inputs.getFirst().length(), inputs.size())) {
            var nextChar = inputs.get(nextPoint.y()).charAt(nextPoint.x());
            if (currentChar == nextChar) {
                return new Point(nextPoint.x(), nextPoint.y());
            }
        }
        return null;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildRegions(inputs);
        return (part1) ? String.valueOf(regions.stream().mapToInt(region -> region.points.size() * region.sumOfBorders()).sum())
                : String.valueOf(regions.stream().mapToInt(region -> region.points.size() * region.sumOfSides()).sum());
    }

    record Region(HashSet<Point> points, char letter) {
        public int sumOfBorders() {
            return points.stream().mapToInt(point -> {
                var perimeter = 0;
                if (!points.contains(point.moveUp())) {
                    perimeter++;
                }
                if (!points.contains(point.moveRight())) {
                    perimeter++;
                }
                if (!points.contains(point.moveDown())) {
                    perimeter++;
                }
                if (!points.contains(point.moveLeft())) {
                    perimeter++;
                }
                return perimeter;
            }).sum();
        }

        public int sumOfSides() {
            return points.stream().mapToInt(point -> {
                int corners = 0;
                var upNeighbour = point.moveUp();
                var upRightNeighbour = point.moveUpRight();
                var rightNeighbour = point.moveRight();
                var downRightNeighbour = point.moveDownRight();
                var downNeighbour = point.moveDown();
                var downLeftNeighbour = point.moveDownLeft();
                var leftNeighbour = point.moveLeft();
                var upLeftNeighbour = point.moveUpLeft();

                if (!points.contains(leftNeighbour) && !points.contains(upNeighbour)
                        || !points.contains(upLeftNeighbour) && points.contains(leftNeighbour) && points.contains(upNeighbour)) {
                    corners++;
                }

                if (!points.contains(rightNeighbour) && !points.contains(upNeighbour)
                        || !points.contains(upRightNeighbour) && points.contains(rightNeighbour) && points.contains(upNeighbour)) {
                    corners++;
                }

                if (!points.contains(rightNeighbour) && !points.contains(downNeighbour)
                        || !points.contains(downRightNeighbour) && points.contains(rightNeighbour) && points.contains(downNeighbour)) {
                    corners++;
                }

                if (!points.contains(leftNeighbour) && !points.contains(downNeighbour)
                        || !points.contains(downLeftNeighbour) && points.contains(leftNeighbour) && points.contains(downNeighbour)) {
                    corners++;
                }

                return corners;
            }).sum();
        }
    }

}

