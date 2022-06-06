package net.fabricmc.minja.spells.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.minja.Minja;
import net.fabricmc.minja.network.NetworkEvent;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.property.Property;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulSparkEntity extends ThrownItemEntity {

    private final int MAX_HEIGHT = 5;

    public SoulSparkEntity(EntityType<SoulSparkEntity> sparkEntityEntityType, World world) {
        super(sparkEntityEntityType, world);
    }

    public SoulSparkEntity(LivingEntity owner, World world) {
        super(Minja.SoulSparkEntityType, owner, world);
    }

    public SoulSparkEntity(double x, double y, double z, World world) {
        super(Minja.SoulSparkEntityType, x, y, z, world);
    }



    @Override
    public void tick() {
        super.tick();

        if(getOwner() == null) return;

        // Get the world and position of the current block
        if(!world.isClient) return;
        BlockPos position = getBlockPos();

        System.out.println("Properties :");
        for(Property p : world.getBlockState(position).getProperties()) {
            System.out.println(p.getName());
        }

        int i=0;
        while(i<MAX_HEIGHT) {

            // Get current block
            BlockPos iterator_position = position.down(i);
            BlockState block = world.getBlockState(iterator_position);
            BlockEntity entity = world.getBlockEntity(iterator_position);

            // Get Fire Block
            SoulFireBlock fire = (SoulFireBlock) Blocks.SOUL_FIRE;

            // Check if there is a solid block
            if(!block.isAir() && block.isFullCube(world, iterator_position)) {

                // Get block on top of the current iteration
                BlockPos upperBlockPos = position.down(i-1);
                BlockState upperBlockState = world.getBlockState(upperBlockPos);

                // Check if there if it's a free block
                if(upperBlockState.isAir()) {

                    // Spawn fire
                    world.setBlockState(upperBlockPos, fire.getDefaultState());

                    // make spawn an entity

                    return;
                }

            }
            i++;
        }
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

    @Override
    protected Item getDefaultItem() {
        return Minja.SOUL_SPARK;
    }
}
