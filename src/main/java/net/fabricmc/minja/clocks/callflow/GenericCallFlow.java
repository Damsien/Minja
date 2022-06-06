package net.fabricmc.minja.clocks.callflow;

import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.enumerations.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public abstract class GenericCallFlow<EventCallback> {

    protected boolean isFirstTimeClicked = true;


    private Side side;

    private World world;

    private PlayerEntity user;

    private Hand hand;


    public void updateAttribute(World w, PlayerEntity u, Hand h, Side s) {
        world = w;
        user = u;
        hand = h;
        side = s;
    }



    public EventCallback triggerCurrentAction() {

        head();

        if (isFirstTimeClicked) {
            afterPressed();
            isFirstTimeClicked = false;
            return triggerPressed();
        }

        else {
            afterMaintained();
            return triggerMaintained();
        }

    }

    public void head() {}

    public void afterReleased() {}

    public void afterPressed() {}

    public void afterMaintained() {}

    public abstract EventCallback triggerReleased();

    public abstract EventCallback triggerPressed();

    public abstract EventCallback triggerMaintained();

    public Hand getHand() {
        return hand;
    }

    public Side getSide() {
        return side;
    }

    public PlayerEntity getUser() {
        return user;
    }

    public World getWorld() {
        return world;
    }

    public abstract boolean checkEvent();

    public void tick() {


        if (!isFirstTimeClicked && checkEvent())  {
            triggerReleased();
            afterReleased();
            isFirstTimeClicked = true;
        }
    }


}
