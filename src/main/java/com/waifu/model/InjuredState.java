package com.waifu.model;

public class InjuredState implements WaifuState {
    public String name() { return "HERIDA"; }
    public int modifyIncomingDamage(int damage) { return Math.max(1, (int) Math.ceil(damage * 1.15)); }
    public boolean canAttack() { return true; }
}
