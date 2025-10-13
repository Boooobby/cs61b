package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import wordnet.HandlerHelper;

public class HyponymsHandler extends NgordnetQueryHandler {

    private final HandlerHelper hh;
    private final NGramMap ngm;

    public HyponymsHandler(String synsetsFile, String hyponymsFile, NGramMap ngm) {
        hh = new HandlerHelper(synsetsFile, hyponymsFile);
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        return hh.getAllHyponyms(q, ngm);
    }
}
