package net.fabricmc.minja.spells;

import net.fabricmc.minja.textures.Texture;
import net.minecraft.util.Identifier;

/**
 * SpellProjectile is a type of spell related to the summoning of a projectile
 *
 */
public abstract class SpellProjectile extends Spell {
    public SpellProjectile(String name, int manaCost, Texture texture, Identifier icon, String type) {
        super(name, manaCost, texture, icon,  type);
    }
}
