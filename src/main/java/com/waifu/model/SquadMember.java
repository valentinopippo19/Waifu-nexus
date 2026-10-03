package com.waifu.model;

public class SquadMember implements SquadComponent {
    private final Waifu waifu;

    public SquadMember(Waifu waifu) { this.waifu = waifu; }

    public Waifu getWaifu() { return waifu; }
    public String getName() { return waifu.getName(); }
    public int totalAttack() { return waifu.getAttack(); }
    public int totalDefense() { return waifu.getDefense(); }
    public int totalHp() { return waifu.getHp(); }

    public void print(String indent) {
        System.out.println(indent + "- " + waifu);
    }
}
