package net.fabricmc.minja.spells;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.LightningEntity;

public class Spark extends SpellProjectile {

    public Spark() {
        super("Spark", 5, "Basic", null);
    }

    @Override
    public void cast(LivingEntity player) {
        SparkItem.getSpark().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
