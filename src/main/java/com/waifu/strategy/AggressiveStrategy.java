package com.waifu.strategy;

import com.waifu.model.Waifu;

public class AggressiveStrategy implements CombatStrategy {
    public int calculateDamage(Waifu a, Waifu d) {
        return Math.max(1, a.calculateBaseDamage() + 20 - d.getDefense() / 3);
    }
    public String name() { return "AGRESIVA"; }
}
