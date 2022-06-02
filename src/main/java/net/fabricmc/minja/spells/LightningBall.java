package net.fabricmc.minja.spells;

import net.fabricmc.minja.spells.items.LightningBallItem;
import net.fabricmc.minja.textures.LightningBallTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class LightningBall extends SpellProjectile {

    public LightningBall() {
        super("Lightning Ball", 10, new LightningBallTexture(), "Basic");
    }

    @Override
    public void cast(LivingEntity player) {
        LightningBallItem.getLightningBall().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
