package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import wordnet.HandlerHelper;

public class HyponymsHandler extends NgordnetQueryHandler {

    private HandlerHelper hh;

    public HyponymsHandler(String synsetsFile, String hyponymsFile) {
        hh = new HandlerHelper(synsetsFile, hyponymsFile);
    }

    @Override
    public String handle(NgordnetQuery q) {
        return hh.getAllHyponyms(q.words());
    }
}
