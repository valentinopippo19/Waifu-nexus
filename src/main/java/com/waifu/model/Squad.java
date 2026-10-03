package com.waifu.model;

import java.util.ArrayList;
import java.util.List;

public class Squad implements SquadComponent {
    private final String name;
    private final List<SquadComponent> children = new ArrayList<>();

    public Squad(String name) { this.name = name; }

    public void add(SquadComponent component) { children.add(component); }
    public void remove(SquadComponent component) { children.remove(component); }

    public String getName() { return name; }

    public int totalAttack() {
        return children.stream().mapToInt(SquadComponent::totalAttack).sum();
    }

    public int totalDefense() {
        return children.stream().mapToInt(SquadComponent::totalDefense).sum();
    }

    public int totalHp() {
        return children.stream().mapToInt(SquadComponent::totalHp).sum();
    }

    public void print(String indent) {
        System.out.println(indent + "+ " + name +
                " [ATK=" + totalAttack() + ", DEF=" + totalDefense() + ", HP=" + totalHp() + "]");
        for (SquadComponent child : children) child.print(indent + "  ");
    }
}
