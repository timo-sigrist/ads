package ch.zhaw.ads;

import java.util.*;

public class RouteServer implements CommandExecutor {
    /**
    build the graph given a text file with the topology
    */
    public Graph<DijkstraNode, Edge> createGraph(String topo) throws Exception {
        AdjListGraph<DijkstraNode, Edge> graph = new AdjListGraph<DijkstraNode, Edge>(DijkstraNode.class, Edge.class);
        Scanner scanner = new Scanner(topo);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineContent = line.split(" ");
            String start = lineContent[0]; String dest = lineContent[1]; String dist = lineContent[2];
            try {
                DijkstraNode startNode = (DijkstraNode) graph.addNode(start);
                DijkstraNode destNode = (DijkstraNode) graph.addNode(dest);
                startNode.addEdge(new Edge(destNode, Double.parseDouble(dist)));
                destNode.addEdge(new Edge(startNode, Double.parseDouble(dist)));
            } catch (Throwable e) {
                System.out.println("Error!");
            }
        }
        scanner.close();
        return graph;
    }


    /**
    apply the dijkstra algorithm
    */
    public void dijkstraRoute(Graph<DijkstraNode, Edge> graph, String from, String to) {
        Iterable<DijkstraNode> graphNodes = graph.getNodes();
        PriorityQueue<DijkstraNode> queue = new PriorityQueue<DijkstraNode>();

        // reset
        graphNodes.forEach(node -> {
            node.setMark(false);
            node.setDist(0);
            node.setPrev(null);
        });
        DijkstraNode current = graph.findNode(from);
        queue.add(current);
        while (!queue.isEmpty()) {
            current = queue.poll();
            if (!current.getName().equals(to)) {
                current.setMark(true);
                Iterable<Edge> edges = current.getEdges();
                DijkstraNode finalCurrent = current;
                edges.forEach(edge -> {
                    DijkstraNode neighbour = (DijkstraNode) edge.getDest();
                    if (!neighbour.getMark()) {
                        double dist = finalCurrent.getDist() + edge.getWeight();
                        if (neighbour.getDist() > dist || neighbour.getDist() == 0) {
                            neighbour.setDist(dist);
                            neighbour.setPrev(finalCurrent);

                            // Node in Queue aktualisieren
                            queue.remove(neighbour);
                            queue.add(neighbour);
                        }
                    }
                });
            } else {
                return;
            }
        }
    }

    /**
    find the route in the graph after applied dijkstra
    the route should be returned with the start town first
    */
    public List<DijkstraNode> getRoute(Graph<DijkstraNode, Edge> graph, String to) {
        List<DijkstraNode> route = new LinkedList<>();
        DijkstraNode town = graph.findNode(to);
        do {
            route.add(0, town);
            town = town.getPrev();
        } while (town != null);
        return route;
    }

    public String execute(String topo) throws Exception {
        Graph<DijkstraNode, Edge> graph = createGraph(topo);
        dijkstraRoute(graph, "Winterthur", "Lugano");
        List<DijkstraNode> route = getRoute(graph, "Lugano");
        // generate result string
        StringBuilder builder = new StringBuilder();
        for (DijkstraNode rt : route) builder.append(rt).append("\n");
        return builder.toString();
    }

    public static void main(String[] args)throws Exception {
        String swiss = "Winterthur Zürich 25\n" +
                    "Zürich Bern 126\n" +
                    "Zürich Genf 277\n" +
                    "Zürich Luzern 54\n" +
                    "Zürich Chur 121\n" +
                    "Zürich Berikon 16\n" +
                    "Bern Genf 155\n" +
                    "Genf Lugano 363\n" +
                    "Lugano Luzern 206\n" +
                    "Lugano Chur 152\n" +
                    "Chur Luzern 146\n" +
                    "Luzern Bern 97\n" +
                    "Bern Berikon 102\n" +
                    "Luzern Berikon 41\n";
        RouteServer server = new RouteServer();
        System.out.println(server.execute(swiss));
    }
}
