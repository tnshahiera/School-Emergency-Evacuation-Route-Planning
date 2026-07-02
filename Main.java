import java.util.*;

class Edge {
    int dest;
    int weight;

    public Edge(int dest, int weight) {
        this.dest = dest;
        this.weight = weight;
    }
}

class Node implements Comparable<Node> {
    int vertex;
    int distance;

    public Node(int vertex, int distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.distance, other.distance);
    }
}

public class Main {
    static final int V = 12;

    static String[] locationNames = {
        "A (Bilik Gerakan)",
        "B (Kantin)",
        "C (Pra 1/2/3)",
        "D (Tapak Perhimpunan)",
        "E (JKR1130 - Pejabat Atas)",
        "F (Dewan Bawah)",
        "G (Surau / Pusat Sumber)",
        "H1 (Blok Utama)",
        "H2 (Blok Kelas 4)",
        "I (Padang)",
        "J (Pintu Masuk Kiri)",
        "K (Pintu Masuk Kanan)"
    };

    static boolean[] blocked = new boolean[V];

    public static void addEdge(List<List<Edge>> graph,
                                int src,
                                int dest,
                                int weight) {
        graph.get(src).add(new Edge(dest, weight));
        graph.get(dest).add(new Edge(src, weight));
    }

    public static String getRiskLevel(int distance) {
        if (distance <= 20)
            return "LOW";
        if (distance <= 40)
            return "MEDIUM";
        return "HIGH";
    }

    public static String buildPath(int vertex, int[] parent) {
        List<String> path = new ArrayList<>();
        while (vertex != -1) {
            path.add(locationNames[vertex]);
            vertex = parent[vertex];
        }
        return String.join("\n | \n", path);
    }

    public static void dijkstra(List<List<Edge>> graph, int safeArea) {
        int[] distance = new int[V];
        int[] parent = new int[V];

        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Node> pq = new PriorityQueue<>();

        distance[safeArea] = 0;
        pq.add(new Node(safeArea, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;

            for (Edge edge : graph.get(u)) {
                int v = edge.dest;

                if (blocked[v])
                    continue;

                int newDistance = distance[u] + edge.weight;

                if (newDistance < distance[v]) {
                    distance[v] = newDistance;
                    parent[v] = u;
                    pq.add(new Node(v, newDistance));
                }
            }
        }

        System.out.println("====================================================");
        System.out.println("        SCHOOL FIRE EVACUATION PLANNING SYSTEM");
        System.out.println("        Using Dijkstra's Shortest Path Algorithm");
        System.out.println("====================================================\n");

        System.out.println("Fire Source : H1 (Blok Utama)");
        System.out.println("Safe Area   : I (Padang)");
        System.out.println("Status      : H1 has been blocked.\n");

        System.out.println("====================================================");
        System.out.println("               EVACUATION ROUTE ANALYSIS");
        System.out.println("====================================================\n");

        for (int i = 0; i < V; i++) {
            if (i == safeArea || blocked[i])
                continue;

            System.out.println("LOCATION : " + locationNames[i]);

            if (distance[i] == Integer.MAX_VALUE) {
                System.out.println("Route  : NO SAFE ROUTE AVAILABLE");
                System.out.println("Status : DANGER");
            } else {
                System.out.println("\nShortest Route:");
                System.out.println(buildPath(i, parent));
                System.out.println("\nTotal Distance : "
                        + distance[i] + " meters");
                System.out.println("Risk Level     : "
                        + getRiskLevel(distance[i]));
                System.out.println("Status         : SAFE");
            }

            System.out.println("\n----------------------------------------------------\n");
        }

        System.out.println("====================================================");
        System.out.println("            EVACUATION ANALYSIS COMPLETE");
        System.out.println("====================================================");
    }

    public static void main(String[] args) {
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        // A = Bilik Gerakan
        // B = Kantin
        // C = Pra 1/2/3
        // D = Tapak Perhimpunan
        // E = Pejabat Atas
        // F = Dewan Bawah
        // G = Surau
        // H1 = Blok Utama
        // H2 = Blok Kelas 4
        // I = Padang
        // J = Pintu Masuk Kiri
        // K = Pintu Masuk Kanan

        addEdge(graph, 0, 1, 10); // A-B
        addEdge(graph, 0, 6, 20); // A-G
        addEdge(graph, 1, 2, 8);  // B-C
        addEdge(graph, 1, 3, 6);  // B-D
        addEdge(graph, 3, 4, 12); // D-E
        addEdge(graph, 4, 5, 5);  // E-F
        addEdge(graph, 5, 9, 15); // F-I
        addEdge(graph, 6, 9, 18); // G-I
        addEdge(graph, 2, 7, 18); // C-H1
        addEdge(graph, 7, 8, 10); // H1-H2
        addEdge(graph, 7, 9, 20); // H1-I
        addEdge(graph, 8, 9, 15); // H2-I
        addEdge(graph, 9, 10, 25); // I-J
        addEdge(graph, 9, 11, 20); // I-K

        // FIRE LOCATION
        int FIRE_NODE = 7; // H1

        // SAFE AREA
        int SAFE_AREA = 9; // I

        blocked[FIRE_NODE] = true;

        dijkstra(graph, SAFE_AREA);
    }
}
