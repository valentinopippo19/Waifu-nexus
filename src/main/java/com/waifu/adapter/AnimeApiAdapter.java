package com.waifu.adapter;

import com.waifu.external.ExternalAnimeApi;
import com.waifu.external.ExternalCharacter;
import com.waifu.model.Element;
import com.waifu.model.Waifu;

public class AnimeApiAdapter {
    private final ExternalAnimeApi api;
    private int nextImportedId = 1000;

    public AnimeApiAdapter(ExternalAnimeApi api) { this.api = api; }

    public Waifu importWaifu(String alias) {
        ExternalCharacter c = api.fetchCharacter(alias);
        Element element = Element.valueOf(c.affinity());
        return new Waifu(nextImportedId++, c.alias(), c.series(), element,
                c.power(), c.resistance(), c.vitality());
    }
}
