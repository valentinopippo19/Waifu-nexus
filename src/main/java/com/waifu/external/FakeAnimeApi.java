package com.waifu.external;

public class FakeAnimeApi implements ExternalAnimeApi {
    public ExternalCharacter fetchCharacter(String alias) {
        return new ExternalCharacter(alias, "Otaku Legends", "LIGHT", 76, 50, 195);
    }
}
