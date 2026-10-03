package com.waifu.model;

public class KnockedOutState implements WaifuState {
    public String name() { return "KO"; }
    public int modifyIncomingDamage(int damage) { return 0; }
    public boolean canAttack() { return false; }
}
