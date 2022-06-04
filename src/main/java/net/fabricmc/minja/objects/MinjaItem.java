package net.fabricmc.minja.objects;

import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.clocks.callflow.CallFlow;
import net.fabricmc.minja.clocks.callflow.MouseManager;
import net.fabricmc.minja.events.ItemEvent;
import net.fabricmc.minja.events.MinjaEvent;
import net.fabricmc.minja.events.Side;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Date;

/**
 * Classe abstraite permettant l'usage du @Override sur les méthodes injectées dans ItemMixin (dont le compilateur
 * n'a pas connaissance) sans pour autant avoir à redéfinir chacune de ces méthodes. (équivalent d'un MouseAdapter en Swing)
 *
 */
public abstract class MinjaItem extends Item implements ItemEvent {

    private MouseManager manager = new MouseManager(this);

    public MinjaItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){

        // Set two use : one for default event, the other one for new events
        TypedActionResult<ItemStack> state1, state2 = null;

        // Get who is client of this event
        Side side = world.isClient ? Side.CLIENT : Side.SERVER;

        // Send a first (default) event to the user
        state1 = onUse(world, user, hand, side);

        // Get the current action on the mouse
        state2 = manager.getRightClick(world, user, hand, side);

        // Return the success of the event. Priority is made on the new methods in case both are overridden
        return state2 != null ? state2 : state1;

    }

    @Override
    final public boolean interact(World world, PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer) {

        // Set two use : one for default event, the other one for new events
        MinjaEvent succeed1, succeed2 = MinjaEvent.UNDEFINED;

        // Get who is client of this event
        Side side = world.isClient ? Side.CLIENT : Side.SERVER;

        // Send a first (default) event to the user
        succeed1 = onInteract(world, playerEntity, hand, side);

        // Get the current action on the mouse
        succeed2 = manager.getLeftClick(world, playerEntity, hand, side);

        // Return the success of the event. Priority is made on the new methods in case both are overridden
        return succeed1 != MinjaEvent.UNDEFINED ? succeed1 != MinjaEvent.CANCELED : succeed2 != MinjaEvent.CANCELED;

    }



    public MinjaEvent onInteract(World world, PlayerEntity playerEntity, Hand hand, Side side) {
        return MinjaEvent.UNDEFINED;
    }


    public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    public MinjaEvent onLeftClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    public MinjaEvent onLeftClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side) {
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return null;
    }

    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return null;
    }

    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        return null;
    }



}
