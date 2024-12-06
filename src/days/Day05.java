package days;

import templates.DayTemplate;

import java.util.*;

public class Day05 extends DayTemplate {

    static HashMap<String, ArrayList<String>> order;
    static ArrayList<String> updates = new ArrayList<>();

    public static Long part1(ArrayList<String> inputs) {
        var result = 0L;
        for (String update : updates) {
            result += calculatePageValue(update, true);
        }
        return result;
    }

    public static Long part2(ArrayList<String> inputs) {
        var result = 0L;
        for (String update : updates) {
            result += calculatePageValue(update, false);
        }
        return result;    }

    private static Long calculatePageValue(String update, boolean part1) {
        var pages = new ArrayList<>(Arrays.stream(update.split(",")).toList());
        var orderSoFar = new ArrayList<>();
        var invalid = false;
        for (int i = 0; i < pages.size(); i++) {
            var page = pages.get(i);
            orderSoFar.add(page);
            if (order.get(page) == null) {
                continue;
            }

            var predecessors = new ArrayList<>(order.get(page));
            predecessors.retainAll(pages);
            if (!orderSoFar.containsAll(predecessors)) {
                invalid = true;
                if (part1) {
                    return 0L;
                } else {
                    for (int j = i + 1; j < pages.size(); j++) {
                        var next = pages.get(j);
                        for (String predecessor : predecessors) {
                            if (next.equals(predecessor)) {
                                pages.set(pages.indexOf(page), next);
                                pages.set(j, page);
                            }
                        }
                    }
                    orderSoFar = new ArrayList<>();
                    i = -1;
                }
            }
        }
        if (invalid) {
            return Long.parseLong(pages.get((pages.size()) / 2));
        }
        if (!part1) {
            return 0L;
        }
        return Long.parseLong(pages.get((pages.size()) / 2));
    }

    private static void buildMaps(ArrayList<String> inputs) {
        boolean orderBuilt = false;
        for(String line : inputs) {
            if (!line.contains("|") && !orderBuilt) {
                orderBuilt = true;
                continue;
            }
            if (orderBuilt) {
                updates.add(line);
            } else {
                String[] parts = line.split("\\|");
                if(order.get(parts[1]) == null) {
                    ArrayList<String> entry = new ArrayList<>();
                    entry.add(parts[0]);
                    order.put(parts[1], entry);
                } else {
                    order.get(parts[1]).add(parts[0]);
                }
            }
        }
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        order = new HashMap<>();
        updates = new ArrayList<>();
        buildMaps(inputs);
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}
