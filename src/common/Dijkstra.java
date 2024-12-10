package common;

import java.util.*;

public class Dijkstra {
    private final int[] distances;
    private final Set<Integer> settled;
    private final PriorityQueue<Node> priorityQueue;
    private final int numVertices;
    List<List<Node>> adjacentNodes;

    public Dijkstra(int numVertices) {
        this.numVertices = numVertices;
        distances = new int[numVertices];
        settled = new HashSet<>();
        priorityQueue = new PriorityQueue<>(numVertices, new Node());
    }

    public void dijkstra(List<List<Node>> adj, int sourceNode) {
        this.adjacentNodes = adj;

        for (int i = 0; i < numVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        priorityQueue.add(new Node(sourceNode, 0));
        distances[sourceNode] = 0;

        while (settled.size() != numVertices) {
            if (priorityQueue.isEmpty()) {
                return;
            }
            int smallestNodeDistance = priorityQueue.poll().node;
            if (settled.contains(smallestNodeDistance)) {
                continue;
            }
            settled.add(smallestNodeDistance);
            processNeighbours(smallestNodeDistance);
        }
    }


    private void processNeighbours(int smallestNodeDistance) {
        int edgeDistance = -1;
        int newDistance = -1;

        for (int i = 0; i < adjacentNodes.get(smallestNodeDistance).size(); i++) {
            Node currentNode = adjacentNodes.get(smallestNodeDistance).get(i);

            if (!settled.contains(currentNode.node)) {
                edgeDistance = currentNode.cost;
                newDistance = distances[smallestNodeDistance] + edgeDistance;

                if (newDistance < distances[currentNode.node]) {
                    distances[currentNode.node] = newDistance;
                }

                priorityQueue.add(new Node(currentNode.node, distances[currentNode.node]));
            }
        }
    }
}

class Node implements Comparator<Node> {

    public int node;
    public int cost;

    public Node() {}

    public Node(int node, int cost) {
        this.node = node;
        this.cost = cost;
    }

    @Override
    public int compare(Node node1, Node node2) {
        return Integer.compare(node1.cost, node2.cost);
    }
}
