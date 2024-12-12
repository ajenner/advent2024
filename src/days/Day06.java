package days;

import common.Triple;
import templates.DayTemplate;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 extends DayTemplate {

    public static Integer part1(ArrayList<String> inputs) {
        int startX = 0;
        int startY;
        for (startY = 0; startY < inputs.size(); startY++) {
            startX = inputs.get(startY).indexOf('^');
            if (startX != -1) {
                break;
            }
        }
        var guard = new Guard(startX, startY, '^', inputs);
        return guard.walk();
    }

    public static Integer part2(ArrayList<String> inputs) {
        int startX = 0;
        int startY;
        for (startY = 0; startY < inputs.size(); startY++) {
            startX = inputs.get(startY).indexOf('^');
            if (startX != -1) {
                break;
            }
        }
        var guard = new Guard(startX, startY, '^', inputs);
        guard.walk();
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                if (inputs.get(y).charAt(x) == 'X') {
                    ArrayList<String> cloneMap = (ArrayList<String>) inputs.clone();
                    char[] chars = cloneMap.get(y).toCharArray();
                    chars[x] = 'O';
                    cloneMap.set(y, String.valueOf(chars));
                    guard.resetToStartWithNewMap(startX, startY, '^', cloneMap);
                    guard.walk();
                }
            }
        }
        return guard.potentialCycles;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }

    static class Guard {
        int x;
        int y;
        char direction;
        Map<Character, Character> rotations = Map.of('^', '>',
                '>', 'v',
                'v', '<',
                '<', '^');
        int stepsTaken;
        boolean finished;
        int potentialCycles;
        ArrayList<String> map;
        ArrayList<Triple<Integer, Integer, Character>> seenCorners;


        public Guard (int x, int y, char direction, ArrayList<String> map) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.stepsTaken = 1;
            this.finished = false;
            this.potentialCycles = 0;
            this.map = map;
            this.seenCorners = new ArrayList<>();
        }

        public int walk () {
            while (!finished) {
                switch (direction) {
                    case '^':
                        step(0, -1);
                        break;
                    case '>':
                        step(1, 0);
                        break;
                    case 'v':
                        step(0, 1);
                        break;
                    case '<':
                        step(-1, 0);
                        break;
                }
            }
            return stepsTaken;
        }

        private void step (int deltaX, int deltaY) {
            if (y + deltaY < 0 || y + deltaY >= map.size() || x + deltaX < 0 || x + deltaX >= map.get(y).length()) {
                this.finished = true;
                return;
            }
            if (map.get(y + deltaY).charAt(x + deltaX) == '#' || map.get(y + deltaY).charAt(x + deltaX) == 'O') {
                var seenCorner = new Triple<>(x + deltaX, y + deltaY, direction);
                if (seenCorners.contains(seenCorner)) {
                    this.finished = true;
                    potentialCycles++;
                }
                seenCorners.add(seenCorner);
                this.direction = rotations.get(this.direction);
                return;
            }
            if (map.get(y + deltaY).charAt(x + deltaX) == '.') {
                char[] chars = map.get(y + deltaY).toCharArray();
                chars[x + deltaX] = 'X';
                map.set(y + deltaY, String.valueOf(chars));
                stepsTaken++;
            }
            y = y + deltaY;
            x = x + deltaX;
        }

        public void resetToStartWithNewMap (int x, int y, char direction, ArrayList<String> map) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.map = map;
            this.finished = false;
            this.seenCorners = new ArrayList<>();
        }
    }
}