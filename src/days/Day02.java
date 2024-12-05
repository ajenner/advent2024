package days;

import templates.DayTemplate;

import java.util.ArrayList;

public class Day02 extends DayTemplate {

    private static ArrayList<ArrayList<Integer>> levels = new ArrayList<>();

    public static Integer part1(ArrayList<String> inputs) {
        int result = 0;
        for (ArrayList<Integer> level : levels) {
            result += determineSafety(level);
        }
        return result;
    }

    public static Integer part2(ArrayList<String> inputs) {
        int result = 0;
        for (ArrayList<Integer> level : levels) {
            int res = determineSafety(level);
            if (res == 0) {
                res = testRemovals(level);
            }
            result += res;
        }
        return result;
    }

    private static void buildLevels(String input) {
        ArrayList<Integer> level = new ArrayList<>();
        String[] parts = input.split(" ");
        for (String part : parts) {
            level.add(Integer.parseInt(part));
        }
        levels.add(level);
    }

    private static Integer determineSafety(ArrayList<Integer> level) {
        int ascOrDesc = -1;
        for (int i = 0; i < level.size() - 1; i++) {
            int firstDigit = level.get(i);
            int secondDigit = level.get(i + 1);
            if (ascOrDesc == -1) {
                if (firstDigit < secondDigit) {
                    ascOrDesc = 0;
                } else {
                    ascOrDesc = 1;
                }
            }

            if (firstDigit < secondDigit && ascOrDesc == 1) {
                return 0;
            }

            if (firstDigit > secondDigit && ascOrDesc == 0) {
                return 0;
            }

            if (Math.abs(secondDigit - firstDigit) > 3 || Math.abs(secondDigit - firstDigit) < 1 ) {
                return 0;
            }
        }
        return 1;
    }

    private static Integer testRemovals(ArrayList<Integer> level) {
        for (int i = 0; i < level.size(); i++) {
            ArrayList<Integer> clone = (ArrayList<Integer>) level.clone();
            clone.remove(i);
            if (determineSafety(clone) == 1) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        levels = new ArrayList<>();
        for (String input : inputs) {
            buildLevels(input);
        }
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}
