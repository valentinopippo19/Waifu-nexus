package com.waifu.factory;

import com.waifu.model.Element;
import com.waifu.model.Waifu;

public class WaifuFactory {
    private int nextId = 1;

    public Waifu create(String type) {
        return switch (type.toLowerCase()) {
            case "fire" -> new Waifu(nextId++, "Akari", "Crimson Academy",
                    Element.FIRE, 80, 35, 180);
            case "water" -> new Waifu(nextId++, "Mizuki", "Azure Moon",
                    Element.WATER, 65, 55, 200);
            case "wind" -> new Waifu(nextId++, "Fuyumi", "Sky Garden",
                    Element.WIND, 72, 42, 190);
            case "light" -> new Waifu(nextId++, "Hikari", "Starlight Saga",
                    Element.LIGHT, 70, 60, 210);
            case "shadow" -> new Waifu(nextId++, "Yoru", "Nightfall Academy",
                    Element.SHADOW, 90, 30, 170);
            default -> throw new IllegalArgumentException("Tipo de waifu desconocido: " + type);
        };
    }
}
