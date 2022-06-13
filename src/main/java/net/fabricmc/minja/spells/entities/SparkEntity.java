package net.fabricmc.minja.spells.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.minja.Minja;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

/**
 * SparkEntity is an entity shortly burning any entity, if the entity is an animal it died
 *
 */
public class SparkEntity extends ThrownItemEntity {


    public SparkEntity(EntityType<SparkEntity> sparkEntityEntityType, World world) {
        super(sparkEntityEntityType, world);
    }

    public SparkEntity(LivingEntity owner, World world) {
        super(Minja.SparkEntityType, owner, world);
    }

    public SparkEntity(double x, double y, double z, World world) {
        super(Minja.SparkEntityType, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Minja.SPARK;
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.FLAME : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
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
     * Method called automatically by the game when SparkEntity hit an entity
     * Burns the entity and kills it if it's an animal
     *
     * @param entityHitResult Entity hit
     */
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        // Trigger SparkEntity effect
        effectOnEntity(entity);
    }

    /**
     * Method called automatically by the game when SparkEntity hit a block
     * Spark vanish
     *
     * @param hitResult Block or entity hit
     */
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        // Deleting SparkEntity
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.kill();
        }
    }

    /**
     * Method called automatically by the game when SparkEntity go into water
     * Spark vanish
     *
     */
    protected void onSwimmingStart() {
        super.onSwimmingStart();
        // Deleting SparkEntity
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.kill();
        }
    }

    /**
     * Method generalising the effect of the spell on an entity, here, burning the entity and killing it if it's an animal
     *
     * @param entity Entity hit
     */
    private void effectOnEntity(Entity entity) {
        entity.setOnFireFor(1);
        if (entity instanceof AnimalEntity) {
            entity.kill();
        }
    }
}