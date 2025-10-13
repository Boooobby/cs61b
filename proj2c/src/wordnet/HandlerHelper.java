package wordnet;

import browser.NgordnetQuery;
import browser.NgordnetQueryType;
import edu.princeton.cs.algs4.In;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

import static java.util.Collections.sort;

public class HandlerHelper {

    private final DirectedGraph dg;
    private DirectedGraph inverseDg;

    public HandlerHelper(String synsetsFile, String hyponymsFile) {
        In in1 = new In(synsetsFile);
        In in2 = new In(hyponymsFile);
        String[] synsets = in1.readAllLines();
        String[] hyponyms = in2.readAllLines();
        dg = new DirectedGraph(synsets, hyponyms);

        inverseDg = new DirectedGraph(synsets, hyponyms);
        ArrayList<ArrayList<Integer>> adjListOfDg = dg.getAdjList();
        ArrayList<ArrayList<Integer>> adjListOfInverseDg = inverseDg.getAdjList();
        for (ArrayList<Integer> arr : adjListOfInverseDg) {
            arr.clear();
        }
        for (int i = 0; i < dg.size(); i++) {
            ArrayList<Integer> arr = adjListOfDg.get(i);
            for (int idx : arr) {
                ArrayList<Integer> inverseArr = adjListOfInverseDg.get(idx);
                inverseArr.add(i);
            }
        }
    }

    private static class TimesWordPair implements Comparable<TimesWordPair> {
        private final Double times;
        private final String word;

        private TimesWordPair(double times, String word) {
            this.times = times;
            this.word = word;
        }

        @Override
        public int compareTo(TimesWordPair o) {
            int cmp = o.times.compareTo(times);
            if (cmp == 0) {
                return word.compareTo(o.word);
            }
            return cmp;
        }
    }

    public String getAllHyponyms(NgordnetQuery q, NGramMap ngm) {
        if (q.ngordnetQueryType() == NgordnetQueryType.HYPONYMS) {
            return getAllHyponyms(q, ngm, this.dg);
        } else {
            return getAllHyponyms(q, ngm, inverseDg);
        }
    }

    public String getAllHyponyms(NgordnetQuery q, NGramMap ngm, DirectedGraph DG) {
        Set<String> wordSet = getAllHyponyms(q.words(), DG);
        if (q.k() == 0) {
            List<String> res = new ArrayList<>(wordSet);
            return res.toString();
        }
        Map<String, TimeSeries> stringTimeSeriesMap = new HashMap<>();
        for (String word : wordSet) {
            stringTimeSeriesMap.put(word, ngm.countHistory(word, q.startYear(), q.endYear()));
        }

        PriorityQueue<TimesWordPair> minHeap = new PriorityQueue<>();
        for (String word : stringTimeSeriesMap.keySet()) {
            double totalTimes = countTimes(stringTimeSeriesMap.get(word));
            if (totalTimes == 0) {
                continue;
            }
            TimesWordPair twp = new TimesWordPair(totalTimes, word);
            minHeap.add(twp);
        }

        int k = q.k();
        List<String> tempList = new ArrayList<>();
        while (k > 0 && !minHeap.isEmpty()) {
            TimesWordPair twp = minHeap.poll();
            tempList.add(twp.word);
            k--;
        }
        sort(tempList);

        List<String> resList = new ArrayList<>();
        int kk = q.k();
        for (int i = 0; i < kk; i++) {
            resList.add(tempList.get(i));
        }
        return resList.toString();
    }

    private double countTimes(TimeSeries ts) {
        double res = 0;
        List<Double> dataList = ts.data();
        for (double d : dataList) {
            res += d;
        }
        return res;
    }

    private Set<String> getAllHyponyms(List<String> words, DirectedGraph DG) {
        List<Set<String>> hyponymsList = new ArrayList<>();
        for (String word : words) {
            hyponymsList.add(getAllHyponyms(word, DG));
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
        return resSet;
    }

    private Set<String> getAllHyponyms(String word, DirectedGraph DG) {
        List<Integer> wordSet = existSet(word, DG);
        Set<String> allHyponyms = new TreeSet<>();
        for (int idx : wordSet) {
            traverse(idx, allHyponyms, DG);
        }
        return allHyponyms;
    }

    private List<Integer> existSet(String word, DirectedGraph DG) {
        List<Integer> res = new ArrayList<>();
        int size = DG.size();
        for (int i = 0; i < size; i++) {
            if (DG.getNode(i).contains(word)) {
                res.add(i);
            }
        }
        return res;
    }

    private void traverse(int idx, Set<String> targetSet, DirectedGraph DG) {
        targetSet.addAll(DG.getNode(idx));
        for (int i : DG.neighbors(idx)) {
            traverse(i, targetSet, DG);
        }
    }
}
