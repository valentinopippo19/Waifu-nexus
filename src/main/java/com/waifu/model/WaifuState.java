package com.waifu.model;

public interface WaifuState {
    String name();
    int modifyIncomingDamage(int damage);
    boolean canAttack();
}
