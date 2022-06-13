package net.fabricmc.minja.spells;

import net.fabricmc.minja.spells.items.LightningBallItem;
import net.fabricmc.minja.textures.LightningBallTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

/**
 * LightningBall is a spell summoning LightningBallEntity
 *
 */
public class LightningBall extends SpellProjectile {

    public LightningBall() {
        super("Lightning Ball", 10, new LightningBallTexture(), new Identifier("spells:textures/item/lightningball.png"), "Basic");
    }

    @Override
    public void cast(LivingEntity player) {
        // Summoning the lightning ball projectile with the use of LightningBallItem
        LightningBallItem.getLightningBall().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);
    }
}
