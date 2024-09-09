package ch.zhaw.ads;

import java.awt.Color;
import java.util.StringTokenizer;

public class LabyrinthServer implements CommandExecutor {
    ServerGraphics g = new ServerGraphics();

    public Graph<DijkstraNode, Edge> createGraph(String s) {
        Graph<DijkstraNode, Edge> graph = new AdjListGraph<>(DijkstraNode.class, Edge.class);

        StringTokenizer tok = new StringTokenizer(s);
        while (tok.hasMoreTokens()) {
            String from = tok.nextToken();
            String to = tok.nextToken();
            try {
                graph.addEdge(from, to, 1);
                graph.addEdge(to, from, 1);
            } catch (Throwable e) {}
        }
        return graph;
    }

    public void drawLabyrinth(Graph<DijkstraNode, Edge> graph) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, 1, 1);
        g.setColor(Color.white);
        for (DijkstraNode n : graph.getNodes()) {
            for (Edge e : n.getEdges()) {
                g.drawPath(n.getName(), e.getDest().getName(), false);
            }
        }
    }

    private boolean search(DijkstraNode current, DijkstraNode ziel) {
        current.setMark(true);
        if (current == ziel) return true;
        for (Edge e : current.getEdges()) {
            DijkstraNode n = (DijkstraNode) e.getDest();
            if (!n.getMark()) {
                n.setPrev(current);
                if (search(n, ziel)) return true;
            }
        }
        current.setMark(false);
        return false;
    }


    public void drawRoute(Graph<DijkstraNode, Edge> graph, String startNode, String zielNode) {
        g.setColor(Color.red);
        DijkstraNode start = graph.findNode(startNode);
        DijkstraNode ziel = graph.findNode(zielNode);

        if (search(start, ziel)) {
            do {
                g.drawPath(ziel.getName(), ziel.getPrev().getName(), true);
                ziel = ziel.getPrev();
            }
            while (ziel.getPrev() != null);
        }
    }

    public String execute(String s) {
        Graph<DijkstraNode, Edge> graph;
        graph = createGraph(s);
        drawLabyrinth(graph);
        drawRoute(graph, "0-6", "3-0");
        return g.getTrace();
    }
}
