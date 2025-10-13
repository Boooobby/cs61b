package wordnet;

import browser.NgordnetQuery;
import edu.princeton.cs.algs4.In;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HandlerHelper {

    private final DirectedGraph dg;

    public HandlerHelper(String synsetsFile, String hyponymsFile) {
        In in1 = new In(synsetsFile);
        In in2 = new In(hyponymsFile);
        String[] synsets = in1.readAllLines();
        String[] hyponyms = in2.readAllLines();
        dg = new DirectedGraph(synsets, hyponyms);
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
            return times.compareTo(o.times);
        }
    }

    public String getAllHyponyms(NgordnetQuery q, NGramMap ngm) {
        Set<String> wordSet = getAllHyponyms(q.words());
        if (q.k() == 0) {
            List<String> res = new ArrayList<>(wordSet);
            return res.toString();
        }
        Map<String, TimeSeries> stringTimeSeriesMap = new HashMap<>();
        for (String word : wordSet) {
            stringTimeSeriesMap.put(word, ngm.countHistory(word, q.startYear(), q.endYear()));
        }

        PriorityQueue<TimesWordPair> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (String word : stringTimeSeriesMap.keySet()) {
            double totalTimes = countTimes(stringTimeSeriesMap.get(word));
            if (totalTimes == 0) {
                continue;
            }
            TimesWordPair twp = new TimesWordPair(totalTimes, word);
            maxHeap.add(twp);
        }

        int k = q.k();
        List<String> res = new ArrayList<>();
        int iterTimes = Math.min(k, maxHeap.size());
        for (int i = 0; i < iterTimes; i++) {
            String word = maxHeap.poll().word;
            res.add(word);
        }
        return res.toString();
    }

    private double countTimes(TimeSeries ts) {
        double res = 0;
        List<Double> dataList = ts.data();
        for (double d : dataList) {
            res += d;
        }
        return res;
    }

    private Set<String> getAllHyponyms(List<String> words) {
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
        return resSet;
    }

    private Set<String> getAllHyponyms(String word) {
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
