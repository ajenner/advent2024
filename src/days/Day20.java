package days;

import common.Point;
import templates.DayTemplate;

import java.util.*;

public class Day20 extends DayTemplate {

    static HashSet<Point> walls = new HashSet<>();
    static HashSet<Cheat> cheats = new HashSet<>();
    static int endX;
    static int endY;

    static Point startPoint;
    static Point endPoint;

    public Long dijkstra(boolean part1) {
        var queue = new PriorityQueue<Node>();
        var visited = new HashSet<Point>();

        var start = new Node(startPoint, 0, null);

        queue.add(start);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (visited.contains(current.point)) {
                continue;
            }
            if (current.point.equals(endPoint)) {
                return buildCheats(current.path(), part1);
            }
            visited.add(current.point);
            queue.addAll(current.getNeighbours());
        }
        return 0L;
    }

    private Long buildCheats(Set<Node> path, boolean part1) {
        Long result = 0L;
        for (Node current : path) {
            if (part1) {
                result += path.stream().filter(node -> node.point.manhattanDistance(current.point) == 2 && node.distance - current.distance >= 102).count();
            } else {
                result += path.stream().filter(node -> {
                    var distance = node.point.manhattanDistance(current.point);
                    if (distance <= 20) {
                        if (node.distance - current.distance >= 100 + distance) {
                            return true;
                        }
                    }
                    return false;
                }).count();
            }
        }
        return result;
    }

    private void buildStartingGrid(ArrayList<String> inputs) {
        walls = new HashSet<>();
        cheats = new HashSet<>();
        endX = inputs.get(0).length();
        endY = inputs.size();
        for (int y = 0; y < endY; y++) {
            for (int x = 0; x < endX; x++) {
                var currentChar = inputs.get(y).charAt(x);
                if (currentChar == 'S') {
                    startPoint = new Point(x, y);
                } else if (currentChar == 'E') {
                    endPoint = new Point(x, y);
                } else if (currentChar == '#') {
                    walls.add(new Point(x, y));
                }
            }
        }
    }

    @Override
    public boolean runSamples(boolean part1) {
        return false;
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        buildStartingGrid(inputs);
        return dijkstra(part1);
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

        private Node getNextNode(Point.Direction nextDirection) {
            Node nextNode = new Node(point.moveDirection(nextDirection), distance + 1, this);

            if (validMove(nextNode.point)) {
                return nextNode;
            }
            return null;
        }

        private boolean validMove(Point nextPoint) {
            return nextPoint.inBounds(endX + 1, endY + 1) && !walls.contains(nextPoint);
        }

        public Set<Node> path() {
            Set<Node> path = new HashSet<>();
            var node = this;
            while (node != null) {
                path.add(node);
                node = node.previous;
            }
            return path;
        }

        @Override
        public int compareTo(final Node o) {
            return Integer.compare(distance, o.distance);
        }
    }
    record Cheat(Point start, Point end) {}
}
