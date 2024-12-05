package days;

import templates.DayTemplate;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends DayTemplate {

    public static Long part1(ArrayList<String> inputs) {
        return performMultiplication(String.join("", inputs));
    }

    public static Long part2(ArrayList<String> inputs) {
        Pattern pattern = Pattern.compile("don't\\(\\).*?(do\\(\\)|$)");
        String flatInput = String.join("", inputs);
        return performMultiplication(String.join("", pattern.split(flatInput)));
    }

    public static Long performMultiplication(String input) {
        Long result = 0L;
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Long digit1 = Long.parseLong(matcher.group(1));
            Long digit2 = Long.parseLong(matcher.group(2));
            result += digit1 * digit2;
        }
        return result;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {

        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}
