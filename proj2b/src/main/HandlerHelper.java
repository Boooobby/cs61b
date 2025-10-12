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

    public String getAllHyponyms(List<String> words) {
        List<Set<String>> hyponymsList = new ArrayList<>();
        for (String word : words) {
            hyponymsList.add(getAllHyponyms(word));
        }

        Set<String> resSet = new TreeSet<>();
        Set<String> std = hyponymsList.getFirst();
        for (String word : std) {
            boolean flag = true;
            for (Set<String> s : hyponymsList) {
                if (!s.contains(word)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resSet.add(word);
            }
        }

        List<String> resList = new ArrayList<>(resSet);
        return resList.toString();
    }

    public Set<String> getAllHyponyms(String word) {
        List<Integer> wordSet = existSet(word);
        Set<String> allHyponyms = new TreeSet<>();
        for (int idx : wordSet) {
            traverse(idx, allHyponyms);
        }
        return allHyponyms;
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
