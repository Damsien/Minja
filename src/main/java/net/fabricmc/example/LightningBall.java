package net.fabricmc.example;

import net.fabricmc.example.mixin.PlayerEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.entity.LightningEntity;

public class LightningBall extends SpellProjectile {

    private class LightningBallProjectile extends SmallFireballEntity {
        public LightningBallProjectile(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            super(world, x, y, z, velocityX, velocityY, velocityZ);
        }

        @Override
        protected void onEntityHit(EntityHitResult entityHitResult) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world); // Create the lightning bolt
            lightning.setPosition(entityHitResult.getPos()); // Set its position
            world.spawnEntity(lightning); // Spawn the lightning entity
        }

        @Override
        protected void onBlockHit(BlockHitResult blockHitResult) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world); // Create the lightning bolt
            lightning.setPosition(blockHitResult.getPos()); // Set its position
            world.spawnEntity(lightning); // Spawn the lightning entity
        }

        @Override
        protected void onCollision(HitResult hitResult) {
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world); // Create the lightning bolt
            lightning.setPosition(hitResult.getPos()); // Set its position
            world.spawnEntity(lightning); // Spawn the lightning entity
        }
    }

    public LightningBall() {
        super("Lightning Ball", 10, "icon.png", "Basic", null);
    }

    @Override
    public void cast(LivingEntity player) {
        player.playSound(SoundEvents.ENTITY_PIG_AMBIENT, 1.0F, 1.0F);
        Vec3d playerPos = player.getPos();
        LightningBallProjectile proj = new LightningBallProjectile(player.world, playerPos.x, playerPos.y, playerPos.z, 1, 0, 0);
    }
}
