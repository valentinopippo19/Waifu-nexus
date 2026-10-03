package com.waifu.model;

public interface SquadComponent {
    String getName();
    int totalAttack();
    int totalDefense();
    int totalHp();
    void print(String indent);
}
