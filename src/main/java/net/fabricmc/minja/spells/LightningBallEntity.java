package net.fabricmc.minja.spells;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.minja.Minja;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class LightningBallEntity extends ThrownItemEntity {// ThrownEntity {

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
        private ParticleEffect getParticleParameters() { // Not entirely sure, but probably has do to with the snowball's particles. (OPTIONAL)
            ItemStack itemStack = this.getItem();
            return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.FLASH : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
        }

        @Environment(EnvType.CLIENT)
        public void handleStatus(byte status) { // Also not entirely sure, but probably also has to do with the particles. This method (as well as the previous one) are optional, so if you don't understand, don't include this one.
            if (status == 3) {
                ParticleEffect particleEffect = this.getParticleParameters();

                for(int i = 0; i < 8; ++i) {
                    this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
                }
            }

        }

    protected void onEntityHit(EntityHitResult entityHitResult) { // called on entity hit.
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world); // Create the lightning bolt
        lightning.setPosition(entity.getPos()); // Set its position
        world.spawnEntity(lightning); // Spawn the lightning entity
    }

    protected void onCollision(HitResult hitResult) { // called on collision with a block
        super.onCollision(hitResult);
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world); // Create the lightning bolt
        lightning.setPosition(hitResult.getPos()); // Set its position
        world.spawnEntity(lightning); // Spawn the lightning entity
        if (!this.world.isClient) { // checks if the world is client
            this.world.sendEntityStatus(this, (byte)3); // particle?
            this.kill(); // kills the projectile
        }
    }

    protected void onSwimmingStart() { // called when entity is in water
        super.onSwimmingStart();
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world); // Create the lightning bolt
        lightning.setPosition(this.getPos()); // Set its position
        world.spawnEntity(lightning); // Spawn the lightning entity
        if (!this.world.isClient) { // checks if the world is client
            this.world.sendEntityStatus(this, (byte)3); // particle?
            this.kill(); // kills the projectile
        }
    }
}