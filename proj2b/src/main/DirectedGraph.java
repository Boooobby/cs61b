package main;

import java.util.*;

public class DirectedGraph {

    private ArrayList<ArrayList<Integer>> adjList;
    HashMap<Integer, Set<String>> map;
    private int size;

    public DirectedGraph(String[] strings, String[] indexes) {
        size = strings.length;
        adjList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            adjList.add(new ArrayList<>());
        }
        map = new HashMap<>();

        // createNode
        for (int i = 0; i < strings.length; i++) {
            String[] splitStrings = strings[i].split(",")[1].split(" ");
            List<String> stringList = new ArrayList<>(Arrays.asList(splitStrings));
            createNode(i, stringList);
        }

        //addEdge
        for (String s : indexes) {
            String[] splitIndexes = s.split(",");
            int src = Integer.parseInt(splitIndexes[0]);
            for (int i = 1; i < splitIndexes.length; i++) {
                addEdge(src, Integer.parseInt(splitIndexes[i]));
            }
        }
    }

    public void createNode(int idx, List<String> stringList) {
        Set<String> set = new HashSet<>(stringList);
        map.put(idx, set);
    }

    public void addEdge(int a, int b) {
        adjList.get(a).add(b);
    }

    public Set<String> getNode(int idx) {
        return map.get(idx);
    }

    public List<Integer> neighbors(int idx) {
        return adjList.get(idx);
    }

    public int size() {
        return size;
    }
}
