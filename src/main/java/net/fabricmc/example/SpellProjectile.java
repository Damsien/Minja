package net.fabricmc.example;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;

public abstract class SpellProjectile extends Spell {

    public ProjectileEntity projectile;

    //List of Minja projectile's spells
    //CAMILLE : NE PAS SUPPRIMER
    //public static final SpellProjectile lightningball = new SpellProjectile("Lightning Ball", 1, "xxx", "basic", getProjectile());


    public SpellProjectile(String name, int manaCost, String icon, String type, ProjectileEntity projectile) {
        super(name, manaCost, icon, type);
        this.projectile = projectile;
    }

    //CAMILLE : NE PAS SUPPRIMER
    /*public static SpellProjectile getSpell(String spell) {
        switch (spell){
            case "lightningball" :
                return lightningball;
            default :
                return null;
        }
    }*/

    public static ProjectileEntity getProjectile() {
        return projectile;
    }
}
