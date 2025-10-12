package main;

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HandlerHelper {

    private final DirectedGraph dg;

    public HandlerHelper(String synsetsFile, String hyponymsFile) {
        In in1 = new In(synsetsFile);
        In in2 = new In(hyponymsFile);
        String[] synsets = in1.readAllLines();
        String[] hyponyms = in2.readAllLines();
        dg = new DirectedGraph(synsets, hyponyms);
    }

    public String getAllHyponyms(String word) {
        List<Integer> wordSet = existSet(word);
        Set<String> allHyponyms = new TreeSet<>();
        for (int idx : wordSet) {
            traverse(idx, allHyponyms);
        }

        List<String> res = new ArrayList<>(allHyponyms);
        return res.toString();
    }

    private List<Integer> existSet(String word) {
        List<Integer> res = new ArrayList<>();
        int size = dg.size();
        for (int i = 0; i < size; i++) {
            if (dg.getNode(i).contains(word)) {
                res.add(i);
            }
        }
        return res;
    }

    private void traverse(int idx, Set<String> targetSet) {
        targetSet.addAll(dg.getNode(idx));
        for (int i : dg.neighbors(idx)) {
            traverse(i, targetSet);
        }
    }
}
