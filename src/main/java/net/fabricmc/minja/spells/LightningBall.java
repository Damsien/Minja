package net.fabricmc.minja.spells;

import net.fabricmc.minja.spells.items.LightningBallItem;
import net.fabricmc.minja.textures.LightingBallTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class LightningBall extends SpellProjectile {

    public LightningBall() {
        super("Lightning Ball", 10, new LightingBallTexture(), "Basic");
    }

    @Override
    public void cast(LivingEntity player) {
        LightningBallItem.getLightningBall().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
