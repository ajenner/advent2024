package days;

import common.Point;
import templates.DayTemplate;
import java.util.ArrayList;
import java.util.HashSet;

public class Day10 extends DayTemplate {

    public Integer part1(ArrayList<String> inputs) {
        int count = 0;
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                if (inputs.get(y).charAt(x) == '0') {
                    count += calculateTrailheadScore(inputs, new Point(x, y), '0', new HashSet<>());
                }
            }
        }
        return count;
    }

    public Integer part2(ArrayList<String> inputs) {
        int count = 0;
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                if (inputs.get(y).charAt(x) == '0') {
                    count += calculateTrailheadRating(inputs, new Point(x, y), '0', new HashSet<>());
                }
            }
        }
        return count;
    }

    private int calculateTrailheadScore(ArrayList<String> inputs, Point currentPoint, char currentChar, HashSet<Point> visited) {
        visited.add(currentPoint);
        if (currentChar == '9') {
            return 1;
        }

        int score = 0;
        Point nextPoint = currentPoint.moveUp();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            score += calculateTrailheadScore(inputs, nextPoint, (char) (currentChar + 1), visited);
        }

        nextPoint = currentPoint.moveRight();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            score += calculateTrailheadScore(inputs, nextPoint, (char) (currentChar + 1), visited);
        }

        nextPoint = currentPoint.moveDown();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            score += calculateTrailheadScore(inputs, nextPoint, (char) (currentChar + 1), visited);
        }

        nextPoint = currentPoint.moveLeft();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            score += calculateTrailheadScore(inputs, nextPoint, (char) (currentChar + 1), visited);
        }
        return score;
    }

    private int calculateTrailheadRating(ArrayList<String> inputs, Point currentPoint, char currentChar, HashSet<Point> visited) {
        if (currentChar == '9') {
            return 1;
        }

        int rating = 0;
        Point nextPoint = currentPoint.moveUp();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            visited.add(nextPoint);
            rating += calculateTrailheadRating(inputs, nextPoint, (char) (currentChar + 1), visited);
            visited.remove(nextPoint);
        }

        nextPoint = currentPoint.moveRight();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            visited.add(nextPoint);
            rating += calculateTrailheadRating(inputs, nextPoint, (char) (currentChar + 1), visited);
            visited.remove(nextPoint);
        }

        nextPoint = currentPoint.moveDown();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            visited.add(nextPoint);
            rating += calculateTrailheadRating(inputs, nextPoint, (char) (currentChar + 1), visited);
            visited.remove(nextPoint);
        }

        nextPoint = currentPoint.moveLeft();
        if (validStep(nextPoint, inputs, currentChar, visited)) {
            visited.add(nextPoint);
            rating += calculateTrailheadRating(inputs, nextPoint, (char) (currentChar + 1), visited);
            visited.remove(nextPoint);
        }
        return rating;
    }

    private boolean validStep (Point nextPoint, ArrayList<String> inputs, char currentChar, HashSet<Point> visited) {
        return nextPoint.inBounds(inputs.getFirst().length(), inputs.size())
                && !visited.contains(nextPoint)
                && inputs.get(nextPoint.y()).charAt(nextPoint.x()) == currentChar + 1;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}
