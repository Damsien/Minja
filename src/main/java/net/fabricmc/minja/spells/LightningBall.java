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

public class LightningBall extends SpellProjectile {

    public LightningBall() {
        super("Lightning Ball", 10, "Basic", null);
    }

    @Override
    public void cast(LivingEntity player) {
        LightningBallItem.getLightningBall().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
