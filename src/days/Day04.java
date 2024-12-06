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
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            result += search(inputs, 'X',  i, j, x, y);
                        }
                    }
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

    public static Integer search(ArrayList<String> inputs, char current, int x, int y, int deltaX, int deltaY) {
        char charToFind = order.get(current);
        if (charToFind == '$') {
            return 1;
        }
        if (x + deltaX < 0 || x + deltaX >= inputs.get(y).length() || y + deltaY < 0 || y + deltaY >= inputs.size()) {
            return 0;
        }
        char next = inputs.get(y + deltaY).charAt(x + deltaX);
        if (charToFind != next) {
            return 0;
        }
        return search(inputs, next, x + deltaX, y + deltaY, deltaX, deltaY);
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1) ? part1(inputs).toString() : part2(inputs).toString();
    }
}
