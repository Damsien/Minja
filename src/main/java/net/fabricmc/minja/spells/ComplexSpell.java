package net.fabricmc.minja.spells;

import net.fabricmc.minja.textures.Texture;
import net.minecraft.entity.LivingEntity;

public abstract class ComplexSpell extends Spell {

    public ComplexSpell(String name, int manaCost, Texture icon, String type) {
        super(name, manaCost, icon, type);
    }

    abstract public void precast(LivingEntity player);

}
