package days;

import templates.DayTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day07 extends DayTemplate {

    public Long calculateLine(String line, boolean part1) {
        var parts = line.split(": ");
        var result = Long.parseLong(parts[0]);
        var digits = Arrays.stream(parts[1].split(" ")).map(Long::valueOf).toList();
        if (validate(result, digits, part1)) {
            return result;
        }
        return 0L;
    }

    public boolean validate(long target, List<Long> factors, boolean part1) {
        return validate(target, factors.getFirst(), factors, 1, part1);
    }

    public boolean validate(long target, long acc, List<Long> factors, int index, boolean part1) {
        if (factors.size() == index || acc > target) {
            return target == acc;
        }
        return validate(target, acc * factors.get(index), factors, index + 1, part1) ||
                validate(target, acc + factors.get(index), factors, index + 1, part1) ||
                !part1 && validate(target, (long) Math.pow(10, Math.floor(Math.log10(factors.get(index))) + 1) * acc + factors.get(index), factors, index + 1, false);
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return String.valueOf(inputs.stream()
                .mapToLong(line -> calculateLine(line, part1))
                .sum()
        );
    }

}