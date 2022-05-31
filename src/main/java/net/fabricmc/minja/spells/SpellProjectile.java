package net.fabricmc.minja.spells;

import net.fabricmc.minja.textures.LightingBallTexture;

public abstract class SpellProjectile extends Spell {
    public SpellProjectile(String name, int manaCost, String type) {
        super(name, manaCost, new LightingBallTexture(), type);
    }
}
