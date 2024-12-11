package days;

import templates.DayTemplate;

import java.util.*;

public class Day11 extends DayTemplate {

    Map<Long, Long> stones;

    private void buildInitialStones(ArrayList<String> inputs) {
        stones = new HashMap<>();
        for (String stone : inputs.get(0).split(" ")) {
            stones.put(Long.parseLong(stone), 1L);
        }
    }

    private List<Long> advanceState(Long stone) {
        long numDigits = (long) (Math.log10(stone) + 1);
        if (stone == 0L) {
            return List.of(1L);
        }
        if (numDigits % 2 == 0) {
            var divisor = Math.pow(10, numDigits / 2);
            long left = (long) (stone / divisor);
            long right = (long) (stone % divisor);
            return List.of(left, right);
        }
        return List.of(stone * 2024);
    }

    private Long dynamicSteps(int steps) {
        List<Long> nextStones;
        for (int i = 0; i < steps; i++) {
            Map<Long, Long> nextCount = new HashMap<>();
            for (var stone : stones.entrySet()) {
                nextStones = advanceState(stone.getKey());
                for (Long nextStone : nextStones) {
                    nextCount.put(nextStone, nextCount.getOrDefault(nextStone, 0L) + stone.getValue());
                }
            }
            stones = nextCount;
        }
        return stones.values().stream().mapToLong(l -> l).sum();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildInitialStones(inputs);
        return (part1) ? dynamicSteps(25).toString() : dynamicSteps(75).toString();
    }
}
