package net.fabricmc.minja.clocks;

import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.mixin.PlayerEntityMixin;
import net.minecraft.entity.player.PlayerEntity;

public class ManaClock extends Clock {

    PlayerMinja player;

    public ManaClock(long timer, PlayerMinja player) {
        super(timer);
        this.player = player;
    }

    @Override
    public void execute() {
        System.out.println("CLock : " + player.getClass());
        player.addMana(5);
        this.start();
    }
}
