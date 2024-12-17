package days;

import common.Point;
import templates.DayTemplate;

import java.util.*;

public class Day16 extends DayTemplate {

    public int dijkstra(ArrayList<String> inputs, boolean part1) {
        var queue = new PriorityQueue<Node>();
        var visited = new HashSet<Entry>();
        int endX = inputs.getFirst().length() - 2;
        int endY = 1;
        int shortest = -1;
        var viewingSpots = new HashSet<Point>();

        var rightStart = new Node(new Entry(new Point(1, inputs.size() - 2), Point.Direction.RIGHT), 0, null);
        queue.add(rightStart);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (part1 && visited.contains(current.node())) {
                continue;
            }
            if (current.node().point().x() == endX && current.node().point().y() == endY) {
                if (shortest == -1) {
                    if (part1) {
                        return current.distance();
                    }
                    shortest = current.distance();
                }
                if (shortest == current.distance()) {
                    viewingSpots.addAll(current.path());
                }
            }
            visited.add(current.node());
            if (part1) {
                queue.addAll(current.getNeighbours(inputs));
            } else {
                for (var next : current.getNeighbours(inputs)) {
                    if (!visited.contains(next.node())) {
                        queue.add(next);
                    }
                }
            }
        }
        return viewingSpots.size();
    }

    @Override
    public Object solve(boolean part1, ArrayList<String> inputs) {
        return String.valueOf(dijkstra(inputs, part1));
    }

    record Node(Entry node, int distance, Node previous) implements Comparable<Node> {

        public Set<Node> getNeighbours(ArrayList<String> inputs) {
            Set<Node> neighbours = new HashSet<>();
            var left = getNextNode(inputs, node.point().rotateDirectionLeft90Degrees(node.direction()));
            if (left != null) {
                neighbours.add(left);
            }

            var right = getNextNode(inputs, node.point().rotateDirectionRight90Degrees(node.direction()));
            if (right != null) {
                neighbours.add(right);
            }

            var straight = getNextNode(inputs, node.direction());
            if (straight != null) {
                neighbours.add(straight);
            }

            return neighbours;
        }

        private Node getNextNode(ArrayList<String> inputs, Point.Direction nextDirection) {
            Node nextNode;
            if (node.direction() == nextDirection) {
                nextNode = new Node(new Entry(node.point().moveDirection(nextDirection), nextDirection), distance + 1, this);
            } else {
                nextNode = new Node(new Entry(node.point(), nextDirection), distance + 1000, this);
            }
            if (validMove(inputs, nextNode.node.point())) {
                return nextNode;
            }
            return null;
        }

        private boolean validMove(ArrayList<String> inputs, Point nextPoint) {
            return nextPoint.inBounds(inputs.getFirst().length(), inputs.size()) && inputs.get(nextPoint.y()).charAt(nextPoint.x()) != '#';
        }

        public Set<Point> path() {
            Set<Point> path = new HashSet<>();
            var node = this;
            while (node != null) {
                path.add(node.node.point());
                node = node.previous;
            }
            return path;
        }

        @Override
        public int compareTo(final Node o) {
            if (distance != o.distance) {
                return Integer.compare(distance, o.distance);
            } else {
                return node.compareTo(o.node);
            }
        }
    }

    record Entry(Point point, Point.Direction direction) implements Comparable<Entry> {

        @Override
        public int compareTo(final Entry o) {
            if (this.point.y() != o.point.y()) {
                return Integer.compare(this.point.y(), o.point.y());
            } else {
                return Integer.compare(this.point.x(), o.point.x());
            }
        }
    }
}
