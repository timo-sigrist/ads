package ch.zhaw.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


public class ADS8_2_test {
    LabyrinthServer labyrinthServer;
    Graph<DijkstraNode, Edge> graph;

    @BeforeEach
    public void init() {
        String labyrinth = "0-6 4-6\n4-6 7-6\n7-6 9-6\n7-6 7-4\n7-4 6-4\n7-4 9-4\n9-4 9-1\n" +
                "7-4 7-1\n7-1 5-1\n4-6 4-4\n4-4 4-3\n4-4 1-4\n1-4 1-1\n1-1 3-1\n3-1 3-2\n3-1 3-0\n";
        labyrinthServer = new LabyrinthServer();
        graph = labyrinthServer.createGraph(labyrinth);
    }

    private void testEdge(String startName, String destName) {
        DijkstraNode node = graph.findNode(startName);
        for (Edge edge : node.getEdges()) {
            if (edge.getDest().getName().equals(destName)) {
                return;
            }
        }
        fail(startName + " not connected to " + destName);
    }

    @Test
    public void testCreateGraph() {
        testEdge("0-6", "4-6");
        testEdge("4-6", "0-6");
        testEdge("1-4", "1-1");
        testEdge("3-1", "3-0");
        testEdge("3-0", "3-1");
    }
}
