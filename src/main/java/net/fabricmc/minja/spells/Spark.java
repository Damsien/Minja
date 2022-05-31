package net.fabricmc.minja.spells;

import net.fabricmc.minja.spells.items.SparkItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class Spark extends SpellProjectile {

    public Spark() {
        super("Spark", 5, "Basic", null);
    }

    @Override
    public void cast(LivingEntity player) {
        SparkItem.getSpark().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
