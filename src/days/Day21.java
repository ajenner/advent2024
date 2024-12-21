package days;

import common.Point;
import templates.DayTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 extends DayTemplate {
    HashSet<Button> numericKeypad;
    HashSet<Button> directionalKeypad;
    HashMap<State, Long> cache;

    private Point matchingButtonLocation(char c, HashSet<Button> keypad) {
        return keypad.stream().filter(button -> button.face() == c).findFirst().get().p();
    }

    public Set<String> permutations(String str) {
        return permutations("", str);
    }

    private Set<String> permutations(String prefix, String str) {
        HashSet<String> results = new HashSet<>();
        int n = str.length();
        if (n == 0) {
            results.add(prefix);
        } else {
            for (int i = 0; i < n; i++) {
                results.addAll(permutations(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n)));
            }
        }
        return results;
    }

    private boolean valid(Point currentCursor, String moves, HashSet<Button> keypad) {
        Point tempPosition = currentCursor;
        Point invalidPosition = matchingButtonLocation('X', keypad);
        for (char move : moves.toCharArray()) {
            tempPosition = switch (move) {
                case '^' -> tempPosition.moveUp();
                case '>' -> tempPosition.moveRight();
                case '<' -> tempPosition.moveLeft();
                case 'v' -> tempPosition.moveDown();
                default -> tempPosition;
            };
            if (tempPosition.equals(invalidPosition)) {
                return false;
            }
        }
        return true;
    }

    private Set<String> generatePossibleMoves(Point target, Point currentCursor, HashSet<Button> keypad) {
        var result = new StringBuilder();
        int cursorX = currentCursor.x();
        int cursorY = currentCursor.y();

        while ((cursorX != target.x())) {
            if (cursorX < target.x()) {
                cursorX++;
                result.append(">");
            } else {
                cursorX--;
                result.append("<");
            }
        }
        while (cursorY != target.y()) {
            if (cursorY < target.y()) {
                cursorY++;
                result.append("v");
            } else {
                cursorY--;
                result.append("^");
            }
        }
        return permutations(result.toString()).stream().map(path -> path + "A")
                .filter(path -> valid(currentCursor, path, keypad)).collect(Collectors.toSet());
    }

    private Long shortestPath(Set<String> paths) {
        long minLength = Long.MAX_VALUE;
        for (String path : paths) {
            minLength = Math.min(minLength, path.length());
        }
        return minLength;
    }

    private Long calculateComplexity(String input, int numberOfRobots, int currentDepth) {
        State currentState = new State(input, numberOfRobots, currentDepth);
        if (cache.get(currentState) != null) {
            return cache.get(currentState);
        }

        var length = 0L;
        Point current;
        HashSet<Button> keypad;

        if (currentDepth == 0) {
            keypad = numericKeypad;
        } else {
            keypad = directionalKeypad;
        }
        current = matchingButtonLocation('A', keypad);

        for (int i = 0; i < input.length(); i++) {
            var next = matchingButtonLocation(input.charAt(i), keypad);
            Set<String> paths = generatePossibleMoves(next, current, keypad);

            if (currentDepth >= numberOfRobots) {
                length += shortestPath(paths);
            } else {
                long minPath = Long.MAX_VALUE;
                for (String path : paths) {
                    minPath = Math.min(minPath, calculateComplexity(path, numberOfRobots, currentDepth + 1));
                }
                length += minPath;
            }
            current = next;
        }

        cache.put(currentState, length);
        return length;
    }

    private Long calculateComplexitySum(String input, int numberOfRobots) {
        var numericWeight = Integer.parseInt(input.split("\\D+")[0]);
        var complexity = calculateComplexity(input, numberOfRobots, 0);
        return numericWeight * complexity;
    }

    private void buildKeypads() {
        numericKeypad = new HashSet<>();
        numericKeypad.add(new Button(new Point(0, 0), '7'));
        numericKeypad.add(new Button(new Point(1, 0), '8'));
        numericKeypad.add(new Button(new Point(2, 0), '9'));
        numericKeypad.add(new Button(new Point(0, 1), '4'));
        numericKeypad.add(new Button(new Point(1, 1), '5'));
        numericKeypad.add(new Button(new Point(2, 1), '6'));
        numericKeypad.add(new Button(new Point(0, 2), '1'));
        numericKeypad.add(new Button(new Point(1, 2), '2'));
        numericKeypad.add(new Button(new Point(2, 2), '3'));
        numericKeypad.add(new Button(new Point(0, 3), 'X'));
        numericKeypad.add(new Button(new Point(1, 3), '0'));
        numericKeypad.add(new Button(new Point(2, 3), 'A'));

        directionalKeypad = new HashSet<>();
        directionalKeypad.add(new Button(new Point(0, 0), 'X'));
        directionalKeypad.add(new Button(new Point(1, 0), '^'));
        directionalKeypad.add(new Button(new Point(2, 0), 'A'));
        directionalKeypad.add(new Button(new Point(0, 1), '<'));
        directionalKeypad.add(new Button(new Point(1, 1), 'v'));
        directionalKeypad.add(new Button(new Point(2, 1), '>'));

        cache = new HashMap<>();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildKeypads();
        return String.valueOf(inputs.stream().mapToLong(input -> calculateComplexitySum(input, (part1) ? 2 : 25)).sum());
    }

    record Button(Point p, char face) { }

    record State(String input, int limit, int depth) { }
}

