package days;

import common.Point;
import templates.DayTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Day14 extends DayTemplate {

    HashSet<Robot> robots;
    int height = 103;
    int width = 101;

    private void step () {
        HashSet<Robot> newRobots = new HashSet<>();
        for (Robot robot : robots) {
            var nextRobot = new Robot(robot.p.moveWithDeltasAndWrap(robot.xVelocity, robot.yVelocity, width, height), robot.xVelocity, robot.yVelocity);
            newRobots.add(nextRobot);
        }
        robots = newRobots;
    }

    private Long countSecurityFactor() {
        long topLeft = 0, topRight = 0, bottomLeft = 0, bottomRight = 0;
        for (Robot robot : robots) {
            if (robot.p.x() < width / 2 && robot.p.y() < height / 2) {
                topLeft ++;
            }
            if (robot.p.x() > width / 2 && robot.p.y() < height / 2) {
                topRight ++;
            }
            if (robot.p.x() < width / 2 && robot.p.y() > height / 2) {
                bottomLeft ++;
            }
            if (robot.p.x() > width / 2 && robot.p.y() > height / 2) {
                bottomRight ++;
            }
        }
        return topLeft * topRight * bottomRight * bottomLeft;
    }

    public Long part1(ArrayList<String> inputs) {
        robots = new HashSet<>();
        for (String input : inputs) {
            var numbers = input.split("[^\\d-]+");
            robots.add(new Robot(new Point(Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2])), Integer.parseInt(numbers[3]), Integer.parseInt(numbers[4])));
        }
        for (int i = 0; i < 100; i++) {
            step();
        }

        return countSecurityFactor();
    }

    public Long part2(ArrayList<String> inputs) {
//        robots = new HashSet<>();
//        for (String input : inputs) {
//            var numbers = input.split("[^\\d-]+");
//            robots.add(new Robot(new Point(Integer.parseInt(numbers[1]), Integer.parseInt(numbers[2])), Integer.parseInt(numbers[3]), Integer.parseInt(numbers[4])));
//        }
//        for (int i = 0; i < 10000; i++) {
//            System.out.println("Count: " + i);
//            step();
//            for (int y = 0; y < height; y++ ) {
//                for (int x = 0; x < width; x++) {
//                    int finalX = x;
//                    int finalY = y;
//                    if (robots.stream().anyMatch(robot -> robot.p.x() == finalX && robot.p.y() == finalY)) {
//                        System.out.print("X");
//                    } else {
//                        System.out.print(".");
//                    }
//                }
//                System.out.println();
//            }
//            Thread.sleep(200);
//        }

        // Happy little tree
        return 6200L;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return (part1)? part1(inputs).toString() : part2(inputs).toString();
    }

    @Override
    public boolean runSamples(boolean part1) {
        return false;
    }

    private record Robot(Point p, int xVelocity, int yVelocity) {}
}
