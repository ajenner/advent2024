package days;

import templates.DayTemplate;

import java.util.*;

public class Day19 extends DayTemplate {

    HashSet<String> towels;
    HashMap<String, Long> visitedDesigns = new HashMap<>();
    ArrayList<String> designs;

    public Long part1() {
        return designs.stream().filter(design -> validDesignCount(design) > 0L).count();
    }

    public Long part2() {
        return designs.stream().mapToLong(this::validDesignCount).sum();
    }

    private Long validDesignCount(String designSection) {
        long count = 0L;
        if (visitedDesigns.containsKey(designSection)) {
            return visitedDesigns.get(designSection);
        }
        for (String towel : towels.stream().filter(designSection::startsWith).toList()) {
            if (designSection.equals(towel)) {
                count++;
            } else {
                count += validDesignCount(designSection.substring(towel.length()));
            }
        }
        visitedDesigns.put(designSection, count);
        return count;
    }

    private void buildTowels(ArrayList<String> inputs) {
        towels = new HashSet<>();
        visitedDesigns = new HashMap<>();
        designs = new ArrayList<>();
        towels.addAll(List.of(inputs.getFirst().split(", ")));
        for (int i = 2; i < inputs.size(); i++) {
            designs.add(inputs.get(i));
        }
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildTowels(inputs);
        return (part1)? part1().toString() : part2().toString();
    }
}
