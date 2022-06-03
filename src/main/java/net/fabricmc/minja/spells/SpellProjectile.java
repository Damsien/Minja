package net.fabricmc.minja.spells;

import net.fabricmc.minja.textures.Texture;

public abstract class SpellProjectile extends Spell {
    public SpellProjectile(String name, int manaCost, Texture texture, String type) {
        super(name, manaCost, texture, type);
    }
}
