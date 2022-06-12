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
    private Identifier icon;
    private Texture texture;
    private String type;

    public Spell(String name, int manaCost, Texture texture, Identifier icon, String type) {
        this.name = name;
        this.manaCost = manaCost;
        this.texture = texture;
        this.icon = icon;
        this.type = type;
    }

    abstract public void cast(LivingEntity player);

    public final String quickDescription() {
        return "(" + getType() + ") " + getName();
    }

    public final String getName() {
        return name;
    }

    public final int getManaCost() {
        return manaCost;
    }

    public final Texture getTexture() {
        return texture;
    }

    public final Identifier getIcon() {
        return icon;
    }

    public final String getType() {
        return type;
    }
}
