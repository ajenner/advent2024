package days;

import common.Point;
import templates.DayTemplate;

import java.util.*;

public class Day18 extends DayTemplate {

    static HashSet<Point> corrupted = new HashSet<>();
    static int endX = 70;
    static int endY = 70;
    int iters = 1024;

    public int dijkstra() {
        var queue = new PriorityQueue<Node>();
        var visited = new HashSet<Point>();

        var start = new Node(new Point(0, 0), 0, null);

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

    private void buildCorruptedMemory(ArrayList<String> inputs) {
        for (int i = 0; i < iters; i++) {
            var byteLoc = inputs.get(i).split("\\D+");
            corrupted.add(new Point(Integer.parseInt(byteLoc[0]), Integer.parseInt(byteLoc[1])));
        }
    }

    private String buildAndCheck(ArrayList<String> inputs) {
        var distance = dijkstra();
        var i = 1024;
        var byteLoc = new String[]{"0", "0"};
        while (distance != 0) {
            byteLoc = inputs.get(i++).split("\\D+");
            corrupted.add(new Point(Integer.parseInt(byteLoc[0]), Integer.parseInt(byteLoc[1])));
            distance = dijkstra();
        }
        return byteLoc[0] + "," + byteLoc[1];
    }

    @Override
    public boolean runSamples(boolean part1) {
        return false;
    }

    @Override
    public boolean exclude() {
        return true;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildCorruptedMemory(inputs);
        return part1 ? String.valueOf(dijkstra()) : buildAndCheck(inputs);
    }

    record Node(Point point, int distance, Node previous) implements Comparable<Node> {

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

        private Node getNextNode( Point.Direction nextDirection) {
            Node nextNode = new Node(point.moveDirection(nextDirection), distance + 1, this);

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
            if (distance != o.distance) {
                return Integer.compare(distance, o.distance);
            } else {
                return point.compareTo(o.point);
            }
        }
    }
}
