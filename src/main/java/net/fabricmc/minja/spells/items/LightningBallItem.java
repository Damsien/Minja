package net.fabricmc.minja.spells.items;

import net.fabricmc.minja.spells.entities.LightningBallEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LightningBallItem extends Item{

    private static LightningBallItem LIGHTNINGBALL;
    public LightningBallItem(Item.Settings settings) {
        super(settings);
        LIGHTNINGBALL = this;
    }

    public static Item getLightningBall(){
        return LIGHTNINGBALL;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // LightningBall sound effect
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.NEUTRAL, 0.5F, 1F);
        // LightningBallEntity parameters
        if (!world.isClient) {
            LightningBallEntity lightningBallEntity = new LightningBallEntity(user, world);
            lightningBallEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 0F);
            lightningBallEntity.setNoGravity(true);
            world.spawnEntity(lightningBallEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }
}