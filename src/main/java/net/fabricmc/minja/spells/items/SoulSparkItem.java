package net.fabricmc.minja.spells.items;

import net.fabricmc.minja.spells.entities.SoulSparkEntity;
import net.fabricmc.minja.spells.entities.SparkEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class SoulSparkItem extends Item {

    private static SoulSparkItem SOUL_SPARK;
    public SoulSparkItem(Item.Settings settings) {

        super(settings);
        SOUL_SPARK = this;
    }

    public static Item getSpark(){
        return SOUL_SPARK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Spark sound effect
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 1F);
        // SparkEntity parameters
        if (!world.isClient) {
            SoulSparkEntity sparkEntity = new SoulSparkEntity(user, world);
            sparkEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 0F);
            sparkEntity.setNoGravity(true);
            world.spawnEntity(sparkEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

}
