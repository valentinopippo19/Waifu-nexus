package com.waifu.strategy;

import com.waifu.model.Waifu;

public class BalancedStrategy implements CombatStrategy {
    public int calculateDamage(Waifu a, Waifu d) {
        return Math.max(1, a.calculateBaseDamage() + a.getDefense() / 5 - d.getDefense() / 2);
    }
    public String name() { return "EQUILIBRADA"; }
}
