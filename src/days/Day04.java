package days;

import templates.DayTemplate;

import java.util.*;

public class Day04 extends DayTemplate {

    static Map<Character, Character> order = Map.of('X', 'M',
            'M', 'A',
            'A', 'S',
            'S', '$');
    static Set<Character> charsToMatch = Set.of('S', 'M');


    public static Integer part1(ArrayList<String> inputs) {
        int result = 0;

        for (int j = 0; j < inputs.size(); j++) {
            for (int i = 0; i < inputs.get(j).length(); i++) {
                if (inputs.get(j).charAt(i) == 'X') {
                    result += search(inputs, 'X', 0, i, j - 1);
                    result += search(inputs, 'X', 1, i + 1, j - 1);
                    result += search(inputs, 'X', 2, i + 1, j);
                    result += search(inputs, 'X', 3, i + 1, j + 1);
                    result += search(inputs, 'X', 4, i, j + 1);
                    result += search(inputs, 'X', 5, i - 1, j + 1);
                    result += search(inputs, 'X', 6, i - 1, j);
                    result += search(inputs, 'X', 7, i - 1, j - 1);
                }
            }
        }

        return result;
    }

    public static Integer part2(ArrayList<String> inputs) {
        int result = 0;

        for (int j = 0; j < inputs.size(); j++) {
            for (int i = 0; i < inputs.get(j).length(); i++) {
                if (inputs.get(j).charAt(i) == 'A') {
                    result += searchX(inputs, i, j);
                }
            }
        }
        return result;
    }

    public static Integer searchX(ArrayList<String> inputs, int i, int j) {
        if (i <= 0 || j <= 0 || i >= inputs.get(j).length() - 1 || j >= inputs.size() - 1) {
            return 0;
        }
        char topLeft = inputs.get(j - 1).charAt(i - 1);
        char topRight = inputs.get(j - 1).charAt(i + 1);
        char bottomLeft = inputs.get(j + 1).charAt(i - 1);
        char bottomRight = inputs.get(j + 1).charAt(i + 1);

        if(charsToMatch.equals(Set.copyOf(List.of(topLeft, bottomRight))) && charsToMatch.equals(Set.copyOf(List.of(bottomLeft, topRight)))) {
            return 1;
        }

        return 0;
    }

    public static Integer search(ArrayList<String> inputs, char current, int direction, int i, int j) {
        char charToFind = order.get(current);
        if (charToFind == '$') {
            return 1;
        }
        if (direction == 0 && j >= 0) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i, j - 1);
        }
        if (direction == 1 && j >= 0 && i < inputs.get(j).length()) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i + 1, j - 1);
        }
        if (direction == 2 && i < inputs.get(j).length()) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i + 1, j);
        }
        if (direction == 3 && j < inputs.size() && i < inputs.get(j).length()) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i + 1, j + 1);
        }
        if (direction == 4 && j < inputs.size()) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i, j + 1);
        }
        if (direction == 5 && i >= 0 && j < inputs.size()) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i - 1, j + 1);
        }
        if (direction == 6 && i >= 0) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i - 1, j);
        }
        if (direction == 7 && i >= 0 && j >= 0) {
            char next = inputs.get(j).charAt(i);
            if (charToFind != next) {
                return 0;
            }
            return search(inputs, next, direction, i - 1, j - 1);
        }
        return 0;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1) ? part1(inputs).toString() : part2(inputs).toString();
    }
}
