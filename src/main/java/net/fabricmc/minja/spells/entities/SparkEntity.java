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

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        effectOnEntity(entity);
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        // Deleting entit
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.kill();
        }
    }

    protected void onSwimmingStart() {
        super.onSwimmingStart();
        // Deleting entity
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.kill();
        }
    }

    private void effectOnEntity(Entity entity) {
        entity.setOnFireFor(1);
        if (entity instanceof AnimalEntity) {
            entity.kill();
        }
    }
}