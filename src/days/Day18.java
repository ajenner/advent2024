package days;

import common.Point;
import templates.DayTemplate;

import java.util.*;

public class Day18 extends DayTemplate {

    static HashSet<Point> corrupted = new HashSet<>();
    static int endX = 70;
    static int endY = 70;

    public int dijkstra() {
        var queue = new PriorityQueue<Node>();
        var visited = new HashSet<Point>();

        var start = new Node(new Point(0, 0), 0);

        queue.add(start);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (visited.contains(current.point)) {
                continue;
            }
            if (current.point.x() == endX && current.point.y() == endY) {
                return current.distance();
            }
            visited.add(current.point);
            queue.addAll(current.getNeighbours());
        }
        return 0;
    }

    private void buildCorruptedMemory(ArrayList<String> inputs, int iters) {
        corrupted = new HashSet<>();
        for (int i = 0; i < iters && i < inputs.size(); i++) {
            var byteLoc = inputs.get(i).split("\\D+");
            corrupted.add(new Point(Integer.parseInt(byteLoc[0]), Integer.parseInt(byteLoc[1])));
        }
    }

    private String part2(ArrayList<String> inputs) {
        var i = inputs.size() / 2;
        var j = inputs.size();
        while (i <= j) {
            int mid = i + (j - i) / 2;
            buildCorruptedMemory(inputs, mid);
            var distance = dijkstra();
            if (distance != 0) {
                i = mid + 1;
            } else {
                j = mid - 1;
            }
        }
        var byteLoc = inputs.get(i - 1).split("\\D+");
        return byteLoc[0] + "," + byteLoc[1];
    }

    @Override
    public boolean runSamples(boolean part1) {
        return false;
    }

    public Integer part1(ArrayList<String> inputs) {
        buildCorruptedMemory(inputs, 1024);
        return dijkstra();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return part1 ? part1(inputs).toString() : part2(inputs);
    }

    record Node(Point point, int distance) implements Comparable<Node> {

        public Set<Node> getNeighbours() {
            Set<Node> neighbours = new HashSet<>();
            for (Point.Direction dir : Point.Direction.ORTHOGONALS) {
                var neighbour = getNextNode(dir);
                if (neighbour != null) {
                    neighbours.add(neighbour);
                }
            }

            return neighbours;
        }

        private Node getNextNode(Point.Direction nextDirection) {
            Node nextNode = new Node(point.moveDirection(nextDirection), distance + 1);

            if (validMove(nextNode.point)) {
                return nextNode;
            }
            return null;
        }

        private boolean validMove(Point nextPoint) {
            return nextPoint.inBounds(endX + 1, endY + 1) && !corrupted.contains(nextPoint);
        }

        @Override
        public int compareTo(final Node o) {
            return Integer.compare(distance, o.distance);
        }
    }
}
