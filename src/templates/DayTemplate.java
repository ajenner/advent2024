package templates;

import java.util.ArrayList;

public abstract class DayTemplate {

    public abstract Object solve(boolean part1, ArrayList<String> inputs);

    public Object timeAndLogResult(String dayName, boolean part1, ArrayList<String> inputs) {
        Long startTime = System.currentTimeMillis();
        Object result = solve(part1, inputs);
        Long endTime = System.currentTimeMillis();
        System.out.println("Day" + dayName + " " + ((part1)? "part1: " : "part2: ") + result);
        System.out.println("Time to complete: " + (endTime - startTime) + "ms");
        return result;
    }

    /**
     * Allows cursed days to be excluded from execution
     */
    public boolean exclude() {
        return false;
    }

}
