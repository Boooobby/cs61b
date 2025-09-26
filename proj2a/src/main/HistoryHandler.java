package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {

    private final NGramMap ngMap;

    public HistoryHandler(NGramMap map) {
        ngMap = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<TimeSeries> lts = new ArrayList<>();
        List<String> labels = new ArrayList<>(q.words());

        for (String word : labels) {
            lts.add(ngMap.weightHistory(word, q.startYear(), q.endYear()));
        }
        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
