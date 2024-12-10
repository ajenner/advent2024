package days;

import common.Point;
import templates.DayTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 extends DayTemplate {

    private final HashMap<Character, ArrayList<Point>> antennas = new HashMap<>();

    private void buildAntennaPositions(ArrayList<String> inputs) {
        for (var i = 0; i < inputs.size(); i++) {
            for (var j = 0; j < inputs.get(i).length(); j++) {
                var currentChar = inputs.get(i).charAt(j);
                if (currentChar != '.') {
                    var points = antennas.getOrDefault(currentChar, new ArrayList<>());
                    points.add(new Point(j, i));
                    antennas.put(currentChar, points);
                }
            }
        }
    }

    private Set<Point> calculateAntinodePositions(ArrayList<String> inputs, boolean part1) {
        Set<Point> antinodes = new HashSet<>();
        antennas.values().forEach(value -> {
            if (value.isEmpty()) {
                return;
            }
            for (var i = 0; i < value.size(); i++) {
                var x = value.get(i).x();
                var y = value.get(i).y();
                for (var j = i + 1; j < value.size(); j++) {
                    var maybeX = value.get(j).x();
                    var maybeY = value.get(j).y();

                    var deltaX = x - maybeX;
                    var deltaY = y - maybeY;

                    var antinode1 = new Point(x + deltaX, y + deltaY);
                    var antinode2 = new Point(maybeX - deltaX, maybeY - deltaY);

                    if (!part1) {
                        antinodes.add(new Point(x,y));
                        antinodes.add(new Point(maybeX, maybeY));
                    }

                    while (antinode1.inBounds(inputs.getFirst().length(), inputs.size())) {
                        antinodes.add(antinode1);
                        if (part1) {
                            break;
                        }
                        antinode1 = new Point(antinode1.x() + deltaX, antinode1.y() + deltaY);
                    }

                    while (antinode2.inBounds(inputs.getFirst().length(), inputs.size())) {
                        antinodes.add(antinode2);
                        if (part1) {
                            break;
                        }
                        antinode2 = new Point(antinode2.x() - deltaX, antinode2.y() - deltaY);
                    }
                }
            }
        });
        return antinodes;
    }

    public Integer part1(ArrayList<String> inputs) {
        return calculateAntinodePositions(inputs, true).size();
    }

    public Integer part2(ArrayList<String> inputs) {
        return calculateAntinodePositions(inputs, false).size();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildAntennaPositions(inputs);
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }
}
