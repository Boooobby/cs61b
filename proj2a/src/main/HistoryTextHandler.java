package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap ngMap;

    public HistoryTextHandler(NGramMap map) {
        ngMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word).append(": ");
            TimeSeries ts = ngMap.weightHistory(word, startYear, endYear);
            sb.append(ts.toString()).append("\n");
        }
        return sb.toString();
    }
}
