import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

class Graph {
    private ArrayList<ArrayList<Pair<Float, Pair<Character, Integer>>>> adj;
    private HashMap<String, Integer> stateMappingNumber;
    private int countOfState;

    public Graph(int size) {
        adj = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            adj.add(new ArrayList<>());
        }
        stateMappingNumber = new HashMap<>();
        countOfState = 0;
    }

    public void addInGraph(int source, int destination, float price, float time, char mode) {
        adj.get(source).add(new Pair<>(price, new Pair<>(mode, destination)));
    }

    public void makeGraph(String source, String destination, float price, float time, char mode) {
        int sourceNum, destinationNum;

        if (stateMappingNumber.containsKey(source)) {
            sourceNum = stateMappingNumber.get(source);
        } else {
            sourceNum = countOfState;
            stateMappingNumber.put(source, countOfState);
            countOfState++;
        }

        if (stateMappingNumber.containsKey(destination)) {
            destinationNum = stateMappingNumber.get(destination);
        } else {
            destinationNum = countOfState;
            stateMappingNumber.put(destination, countOfState);
            countOfState++;
        }

        addInGraph(sourceNum, destinationNum, price, time, mode);
    }

    public int minimumTimeRoute(int source, int destination) {
        PriorityQueue<Pair<Integer, Pair<Integer, String>>> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.getFirst(), b.getFirst())
        );

        pq.add(new Pair<>(0, new Pair<>(source, "")));

        int[] vis = new int[countOfState];

        while (!pq.isEmpty()) {
            Pair<Integer, Pair<Integer, String>> top = pq.poll();

            int time = top.getFirst();
            int node = top.getSecond().getFirst();
            String path = top.getSecond().getSecond();

            vis[node] = 1;

            if (node == destination) {
                System.out.println(path);
                return time;
            }

            for (Pair<Float, Pair<Character, Integer>> it : adj.get(node)) {
                if (vis[it.getSecond().getSecond()] == 0) {
                    String temp = " ";
                    temp += it.getSecond().getSecond() + "_";
                    temp += it.getSecond().getFirst();
                    pq.add(new Pair<>(Math.round(time + it.getFirst()), new Pair<>(it.getSecond().getSecond(), path + temp)));
                }
            }
        }

        System.out.println("No route");
        return -1;
    }
}

class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph(1000000);

        graph.makeGraph("Delhi", "Chandigarh", 5000.0f, 2.0f, 'F');
        graph.makeGraph("Delhi", "Chandigarh", 2000.0f, 4.0f, 'T');
        graph.makeGraph("Delhi", "Chandigarh", 300.0f, 6.0f, 'B');

        // Add more makeGraph calls for other routes...

        for (String state : graph.stateMappingNumber.keySet()) {
            System.out.println("state " + state + " mapping with " + graph.stateMappingNumber.get(state) + " state");
        }

        System.out.println();

        System.out.println(graph.minimumTimeRoute(graph.stateMappingNumber.get("Delhi"), graph.stateMappingNumber.get("Delhi")));
    }
}
