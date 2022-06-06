package net.fabricmc.minja.mouse.context;

import net.fabricmc.minja.enumerations.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Context of a Mouse Event
 */
public class Context {

    private World world;

    private PlayerEntity user;

    private Hand hand;

    private Side side;

    public Context(World world, PlayerEntity user, Hand hand, Side side) {
        this.world = world;
        this.user = user;
        this.hand = hand;
        this.side = side;
    }

    /**
     * Get the world in which the context takes place
     *
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the user in which the context takes place
     *
     * @return
     */
    public PlayerEntity getUser() {
        return user;
    }

    /**
     * Get the hand in which the context takes place
     *
     * @return
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Get the side in which the context takes place
     *
     * @return
     */
    public Side getSide() {
        return side;
    }
}
