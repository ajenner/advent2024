package days;

import templates.DayTemplate;

import java.util.ArrayList;

public class Day01 extends DayTemplate {

    private static ArrayList<Integer> firstDigits = new ArrayList<>();
    private static ArrayList<Integer> secondDigits = new ArrayList<>();


    public static Integer part1(ArrayList<String> inputs) {
        int result = 0;
        for (int i = 0; i < firstDigits.size(); i++) {
            result += Math.abs(secondDigits.get(i) - firstDigits.get(i));
        }
        return result;
    }

    private static void buildLists(String input) {
        String[] parts = input.split(" {3}");
        firstDigits.add(Integer.parseInt(parts[0]));
        secondDigits.add(Integer.parseInt(parts[1]));
    }

    public static Integer part2(ArrayList<String> inputs) {
        int result = 0;
        for (Integer firstDigit : firstDigits) {
            long count = secondDigits.stream().filter(x -> x.equals(firstDigit)).count();
            result += (int) (firstDigit * count);
        }
        return result;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        firstDigits = new ArrayList<>();
        secondDigits = new ArrayList<>();
        for (String input : inputs) {
            buildLists(input);
        }
        firstDigits.sort(null);
        secondDigits.sort(null);
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}
