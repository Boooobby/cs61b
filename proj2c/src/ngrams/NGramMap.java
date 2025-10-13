package ngrams;

import edu.princeton.cs.algs4.In;
import net.sf.saxon.functions.Minimax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // TODO: Add any necessary static/instance variables.
    private final HashMap<String, TimeSeries> wordHistory;
    private final TimeSeries corpusHistory;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        wordHistory = readWords(wordsFilename);
        corpusHistory = readCorpus(countsFilename);
    }

    private HashMap<String, TimeSeries> readWords(String wordsFilename) {
        HashMap<String, TimeSeries> res = new HashMap<>();
        In in = new In(wordsFilename);
        while (in.hasNextLine()) {
            String[] nextLineSplit = in.readLine().split("\t");
            String word = nextLineSplit[0];
            int year = Integer.parseInt(nextLineSplit[1]);
            double occurTimes = Double.parseDouble(nextLineSplit[2]);
            if (res.containsKey(word)) {
                res.get(word).put(year, occurTimes);
            } else {
                res.put(word, new TimeSeries());
                res.get(word).put(year, occurTimes);
            }
        }
        return res;
    }

    private TimeSeries readCorpus(String countsFilename) {
        TimeSeries res = new TimeSeries();
        In in = new In(countsFilename);
        while (in.hasNextLine()) {
            String[] nextLineSplit = in.readLine().split(",");
            int year = Integer.parseInt(nextLineSplit[0]);
            double numOfWords = Double.parseDouble(nextLineSplit[1]);
            res.put(year, numOfWords);
        }
        return res;
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries res;
        if (wordHistory.containsKey(word)) {
            res = new TimeSeries(wordHistory.get(word), startYear, endYear);
        } else {
            res = new TimeSeries();
        }
        return res;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        // TODO: Fill in this method.
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        return new TimeSeries(corpusHistory, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries res;
        if (wordHistory.containsKey(word)) {
            res = new TimeSeries(wordHistory.get(word), startYear, endYear);
            List<Integer> yearList = res.years();
            for (int year : yearList) {
                res.put(year, res.get(year) / corpusHistory.get(year));
            }
        } else {
            res = new TimeSeries();
        }
        return res;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries res = new TimeSeries();
        List<TimeSeries> listOfWeight = new ArrayList<>();
        for (String word : words) {
            listOfWeight.add(weightHistory(word, startYear, endYear));
        }
        for (TimeSeries ts : listOfWeight) {
            List<Integer> yearList = ts.years();
            for (int year : yearList) {
                if (res.containsKey(year)) {
                    res.put(year, res.get(year) + ts.get(year));
                } else {
                    res.put(year, ts.get(year));
                }
            }
        }
        return res;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
