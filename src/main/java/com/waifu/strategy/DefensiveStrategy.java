package com.waifu.strategy;

import com.waifu.model.Waifu;

public class DefensiveStrategy implements CombatStrategy {
    public int calculateDamage(Waifu a, Waifu d) {
        return Math.max(1, a.calculateBaseDamage() - d.getDefense() / 2);
    }
    public String name() { return "DEFENSIVA"; }
}
