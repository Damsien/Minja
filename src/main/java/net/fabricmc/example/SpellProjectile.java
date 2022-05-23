package net.fabricmc.example;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;

public abstract class SpellProjectile extends Spell {

    //List of Minja projectile's spells
    public static final SpellProjectile lightningball = new SpellProjectile("Lightning Ball", 1, "xxx", "basic", proj);

    public ProjectileEntity projectile;

    public SpellProjectile(String name, int manaCost, String icon, String type, ProjectileEntity projectile) {
        super(name, manaCost, icon, type);
        this.projectile = projectile;
    }

    public static SpellProjectile getSpell(String spell) {
        switch (spell){
            case "lightningball" :
                return lightningball;
            default :
                return null;
        }
    }

    public ProjectileEntity getProjectile() {
        return projectile;
    }
}
