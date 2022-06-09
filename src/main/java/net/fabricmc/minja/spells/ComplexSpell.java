package net.fabricmc.minja.spells;

import net.fabricmc.minja.textures.Texture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class ComplexSpell extends Spell {

    public ComplexSpell(String name, int manaCost, Texture texture, Identifier icon, String type) {
        super(name, manaCost, texture, icon, type);
    }

    abstract public void precast(LivingEntity player);

}
