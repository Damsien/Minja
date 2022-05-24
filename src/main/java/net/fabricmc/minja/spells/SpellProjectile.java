package net.fabricmc.minja.spells;

import net.fabricmc.minja.spells.Spell;
import net.minecraft.entity.projectile.ProjectileEntity;

public abstract class SpellProjectile extends Spell {

    public ProjectileEntity projectile;

    public SpellProjectile(String name, int manaCost, String icon, String type, ProjectileEntity projectile) {
        super(name, manaCost, icon, type);
        this.projectile = projectile;
    }

    public ProjectileEntity getProjectile() {
        return projectile;
    }
}
