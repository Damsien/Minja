package net.fabricmc.minja.spells;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.minja.objects.Wand;
import net.fabricmc.minja.textures.Texture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/**
 * Spell defines attributes of a spell and a cast method
 *
 */
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

    /**
     * Cast the spell. Activated by a wand hold by player
     *
     * @param player Player holding the wand
     */
    abstract public void cast(LivingEntity player);

    /**
     * Quick description of the spell
     *
     * @return "(SpellType) SpellName"
     */
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
