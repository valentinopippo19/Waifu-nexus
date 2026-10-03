package com.waifu.model;

public class HealthyState implements WaifuState {
    public String name() { return "SALUDABLE"; }
    public int modifyIncomingDamage(int damage) { return damage; }
    public boolean canAttack() { return true; }
}
