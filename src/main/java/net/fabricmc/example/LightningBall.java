package net.fabricmc.example;

import net.fabricmc.example.mixin.PlayerEntityMixin;
import net.minecraft.entity.projectile.ProjectileEntity;

public class LightningBall extends SpellProjectile {

    public LightningBall() {
        super("Lightning Ball", 10, "icon.png", "Basic", null);
    }

    @Override
    public void cast(PlayerEntityMixin player) {

    }
}
