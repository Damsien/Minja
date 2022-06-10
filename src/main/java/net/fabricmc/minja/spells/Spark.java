package net.fabricmc.minja.spells;

import net.fabricmc.minja.spells.items.SparkItem;
import net.fabricmc.minja.textures.SparkTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class Spark extends SpellProjectile {

    public Spark() {
        super("Spark", 5,new SparkTexture(), new Identifier("spells:textures/item/spark.png"), "Basic");
    }

    @Override
    public void cast(LivingEntity player) {

        SparkItem.getSpark().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
