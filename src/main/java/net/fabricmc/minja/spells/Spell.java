package net.fabricmc.minja.spells;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.minja.objects.Wand;
import net.fabricmc.minja.textures.Texture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public abstract class Spell {
    private String name;
    private int manaCost;
    private Texture icon;
    private String type;

    public Spell(String name, int manaCost, Texture icon, String type) {
        this.name = name;
        this.manaCost = manaCost;
        this.icon = icon;
        this.type = type;
    }

    abstract public void cast(LivingEntity player);

    public String quickDescription() {
        return "(" + getType() + ") " + getName() + ": " + getManaCost() + " mana";
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public Texture getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }
}
