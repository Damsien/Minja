package net.fabricmc.minja.objects;

import net.fabricmc.minja.events.Tickable;
import net.fabricmc.minja.mouse.callflow.GenericCallFlow;
import net.fabricmc.minja.mouse.callflow.MouseManager;
import net.fabricmc.minja.events.ItemEvent;
import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.events.MouseEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *
 * Representation of an Item in game.
 *
 * API (in the API) to provide a user with new events on an Item in the game (especially mouse clicks). <br><br>
 *
 * This abstract class both implements the event system and provides default methods
 * (in case the user does not want to redefine its methods, like <i>{@link java.awt.event.MouseAdapter MouseAdapter}</i> in Swing)
 *
 *
 */
public abstract class MinjaItem extends Item implements ItemEvent, MouseEvent, Tickable {

    private MouseManager manager = new MouseManager(this);

    public MinjaItem(Settings settings) {
        super(settings);
    }

    /**
     *
     * Triggered when the current item is used. <br>
     * <strong><u>This method is final and shouldn't be called/used in subclasses.</u></strong> <br><br>
     *
     * When a new event is received from the game, this method will receive it and emit
     * new (and more accurate!) events which will be easier to use for a user. <br><br>
     *
     * In case you want the default behaviour of use() method, consider using the <strong>{@link MinjaItem#onUse(World, PlayerEntity, Hand, Side) onUse}</strong> method. <br><br>
     *
     * In case an Item is both Overriding onUse and onRightClickXXX,
     * the priority for the success will be given to  onRightClickXXX.
     *
     * @see MinjaItem#onUse(World, PlayerEntity, Hand, Side)
     * @see MinjaItem#onRightClickPressed(World, PlayerEntity, Hand, boolean, Side)
     * @see MinjaItem#onRightClickMaintained(World, PlayerEntity, Hand, boolean, Side)
     * @see MinjaItem#onRightClickReleased(World, PlayerEntity, Hand, boolean, Side)
     *
     * @param world World in which the event has occurred
     * @param user User who triggered the event
     * @param hand Hand in which the user is holding the item
     *
     * @return the success of the event
     */
    @Override
    final public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){

        // Set two use : one for default event, the other one for new events
        TypedActionResult<ItemStack> state1, state2 = null;

        // Get who is client of this event
        Side side = world.isClient ? Side.CLIENT : Side.SERVER;

        // Send a first (default) event to the user
        state1 = onUse(world, user, hand, side);

        // Get the current action on the mouse
        state2 = manager.triggerRightClickAction(world, user, hand, side);

        // Return the success of the event. Priority is made on the new methods in case both are overridden
        return state2 != null ? state2 : state1;

    }

    /**
     *
     * Triggered when the player swing his hand with the item in hand <br>
     * <strong><u>This method is final and shouldn't be called/used in subclasses.</u></strong> <br><br>
     *
     * When a new event is received from the game, this method will receive it and emit
     * new (and more accurate!) events which will be easier to use for a user. <br><br>
     *
     * There is no interact() method by default, but you can now use <strong>{@link MinjaItem#onInteract(World, PlayerEntity, Hand, Side)}  onInteract}</strong> method to simulate it.
     *
     * @see MinjaItem#onInteract(World, PlayerEntity, Hand, Side)
     * @see MinjaItem#onLeftClickPressed(World, PlayerEntity, Hand, boolean, Side)
     * @see MinjaItem#onLeftClickMaintained(World, PlayerEntity, Hand, boolean, Side)
     * @see MinjaItem#onLeftClickReleased(World, PlayerEntity, Hand, boolean, Side)
     *
     * @param world World in which the event has occurred
     * @param playerEntity User who triggered the event
     * @param hand Hand in which the user is holding the item
     * @param fromServerPlayer
     *
     * @return the success of the event
     */
    @Override
    final public boolean interact(World world, PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer) {

        // Set two use : one for default event, the other one for new events
        MinjaEvent succeed1, succeed2 = MinjaEvent.UNDEFINED;

        // Get who is client of this event
        Side side = world.isClient ? Side.CLIENT : Side.SERVER;

        // Send a first (default) event to the user
        succeed1 = onInteract(world, playerEntity, hand, side);

        // Get the current action on the mouse
        succeed2 = manager.triggerLeftClickAction(world, playerEntity, hand, side);

        // Return the success of the event. Priority is made on the new methods in case both are overridden
        return succeed1 != MinjaEvent.UNDEFINED ? succeed1 != MinjaEvent.CANCELED : succeed2 != MinjaEvent.CANCELED;

    }

    /**
     * Send the tick signal emit by the {@link net.fabricmc.minja.mixin.PlayerEntityMixin#tick(CallbackInfo)}  PlayerEntity} to the {@link MouseManager#tick(Side)}.
     *
     * Unlike entities, items do not have a tick method to update themselves regularly. <br><br>
     *
     * PlayerEntity will therefore distribute its signal to the Minja object it has in its hand. <br>
     * In this way, the event manager will be able to get a regular signal to analyze its current state, without needing a clock.
     *
     * Furthermore, we will take advantage of this to implement this event for the user,
     * with {@link MinjaItem#onUse(World, PlayerEntity, Hand, Side) onUse} in case we wants
     * to perform operations on our item regularly!
     *
     * @param world World in which the tick has been emitted
     */
    public void tick(World world) {
        Side side = world.isClient ? Side.CLIENT : Side.SERVER;
        manager.tick(side);
        onTick(side);
    }

    @Override public MinjaEvent onInteract(World world, PlayerEntity playerEntity, Hand hand, Side side) {
        return MinjaEvent.UNDEFINED;
    }


    @Override  public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override  public MinjaEvent onLeftClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override  public MinjaEvent onLeftClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override  public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side) {
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override  public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return null;
    }

    @Override  public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return null;
    }

    @Override  public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return null;
    }

    @Override public void onTick(Side side) {}






}
