package net.fabricmc.minja.spells.items;

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

/**
 * SparkItem is an item hidden from player, used to summon the SparkEntity
 *
 */
public class SparkItem extends Item {

    private static SparkItem SPARK;
    public SparkItem(Item.Settings settings) {

        super(settings);
        SPARK = this;
    }

    public static Item getSpark(){
        return SPARK;
    }

    /**
     * Method called to summon the SparkEntity when Spark spell is cast by a wand
     *
     * @param world Current loaded world
     * @param user Player using the wand
     * @param hand Hand used by the player
     */
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Spark sound effect
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 1F);
        // SparkEntity parameters & spawn
        if (!world.isClient) {
            SparkEntity sparkEntity = new SparkEntity(user, world);
            sparkEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2F, 0F);
            sparkEntity.setNoGravity(true);
            world.spawnEntity(sparkEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }
}
