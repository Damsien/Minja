package net.fabricmc.example;

import net.fabricmc.example.mixin.PlayerEntityMixin;

public abstract class Spell {
    private String name;
    private int manaCost;
    private String icon;
    private String type;

    public Spell(String name, int manaCost, String icon, String type) {
        this.name = name;
        this.manaCost = manaCost;
        this.icon = icon;
        this.type = type;
    }

    abstract public void cast(PlayerEntityMixin player);

    public String quickDescription() {
        return "(" + getType() + ") " + getName() + ": " + getManaCost() + " mana";
    }
    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }
}
