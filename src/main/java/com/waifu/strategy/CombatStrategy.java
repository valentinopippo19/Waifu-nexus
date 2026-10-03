package com.waifu.strategy;

import com.waifu.model.Waifu;

public interface CombatStrategy {
    int calculateDamage(Waifu attacker, Waifu defender);
    String name();
}
