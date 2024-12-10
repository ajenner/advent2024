package days;

import common.Point;
import templates.DayTemplate;

import java.util.ArrayList;
import java.util.HashSet;

public class Day10 extends DayTemplate {

    private int calculateTrailheadScore(ArrayList<String> inputs, Point currentPoint, char currentChar, HashSet<Point> visited, boolean part1) {
        if (part1) {
            visited.add(currentPoint);
        }

        if (currentChar == '9') {
            return 1;
        }

        int score = 0;
        Point nextPoint;
        for (Point.Direction dir : Point.Direction.ORTHOGONALS) {
            nextPoint = currentPoint.moveDirection(dir);
            if (validStep(nextPoint, inputs, currentChar, visited)) {
                if (!part1) {
                    visited.add(nextPoint);
                }
                score += calculateTrailheadScore(inputs, nextPoint, (char) (currentChar + 1), visited, part1);
                if (!part1) {
                    visited.remove(nextPoint);
                }
            }
        }
        return score;
    }

    private boolean validStep(Point nextPoint, ArrayList<String> inputs, char currentChar, HashSet<Point> visited) {
        return nextPoint.inBounds(inputs.getFirst().length(), inputs.size())
                && !visited.contains(nextPoint)
                && inputs.get(nextPoint.y()).charAt(nextPoint.x()) == currentChar + 1;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        int count = 0;
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                if (inputs.get(y).charAt(x) == '0') {
                    count += calculateTrailheadScore(inputs, new Point(x, y), '0', new HashSet<>(), part1);
                }
            }
        }
        return Integer.toString(count);
    }
}
