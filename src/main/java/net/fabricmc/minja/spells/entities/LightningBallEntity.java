package net.fabricmc.minja.spells.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.minja.Minja;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * LightningBallEntity is an entity summoning a lightning bolt on collision with any block or entity
 *
 */
public class LightningBallEntity extends ThrownItemEntity {

        public LightningBallEntity(EntityType<LightningBallEntity> lightningBallEntityEntityType, World world) {
            super(lightningBallEntityEntityType, world);
        }

        public LightningBallEntity(LivingEntity owner, World world) {
            super(Minja.LightningBallEntityType, owner, world);
        }

        public LightningBallEntity(double x, double y, double z, World world) {
            super(Minja.LightningBallEntityType, x, y, z, world);
        }


        @Override
        protected Item getDefaultItem() {
            return Minja.LIGHTNINGBALL;
        }

        @Environment(EnvType.CLIENT)
        private ParticleEffect getParticleParameters() {
            ItemStack itemStack = this.getItem();
            return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.FLASH : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
        }

        @Environment(EnvType.CLIENT)
        public void handleStatus(byte status) {
            if (status == 3) {
                ParticleEffect particleEffect = this.getParticleParameters();

                for(int i = 0; i < 8; ++i) {
                    this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
                }
            }

        }

    /**
     * Method called automatically by the game when LightningBallEntity hit an entity
     * A lightning bolt is summoned at its position
     *
     * @param entityHitResult Entity hit
     */
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        // Trigger LightningBallEntity effect
        effect(entity.getPos());
    }

    /**
     * Method called automatically by the game when LightningBallEntity hit a block
     * A lightning bolt is summoned at its position
     *
     * @param hitResult Block hit
     */
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        // Trigger LightningBallEntity effect
        effect(hitResult.getPos());
        // Deleting LightningBallEntity
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.kill();
        }
    }

    /**
     * Method called automatically by the game when LightningBallEntity go into water
     * A lightning bolt is summoned at its position
     *
     */
    protected void onSwimmingStart() {
        super.onSwimmingStart();
        // Trigger LightningBallEntity effect
        effect(this.getPos());
        // Deleting LightningBallEntity
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.kill();
        }
    }

    /**
     * Method generalising the effect of the spell, here, spawning the lightning ball
     *
     * @param pos Coordinates where the lightning bolt must be summoned
     */
    private void effect(Vec3d pos) {
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightning.setPosition(pos);
        world.spawnEntity(lightning);
    }
}