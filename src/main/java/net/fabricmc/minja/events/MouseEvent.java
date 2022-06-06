package net.fabricmc.minja.events;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.enumerations.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 *  Events related to the mouse
 *
 *  @author      Tom Froment
 */
public interface MouseEvent {

    // -- LEFT CLICK CALLFLOW

    /**
     *
     * Event triggered once the <strong>left</strong> click is <strong>pressed</strong>.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the action
     * @param hand Hand holding the item triggering the action
     * @param otherClickSelected If the right click is active
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side);

    /**
     *
     * Event triggered once the <strong>left</strong> click is <strong>maintained</strong>.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the action
     * @param hand Hand holding the item triggering the action
     * @param otherClickSelected If the right click is active
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public MinjaEvent onLeftClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side);

    /**
     *
     * Event triggered once the <strong>left</strong> click is <strong>released</strong>.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the action
     * @param hand Hand holding the item triggering the action
     * @param otherClickSelected If the right click is active
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public MinjaEvent onLeftClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side);










    // -- RIGHT CLICK CALLFLOW

    /**
     *
     * Event triggered once the <strong>right</strong> click is <strong>pressed</strong>.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the action
     * @param hand Hand holding the item triggering the action
     * @param otherClickSelected If the right click is active
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side);

    /**
     *
     * Event triggered once the <strong>right</strong> click is <strong>maintained</strong>.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the action
     * @param hand Hand holding the item triggering the action
     * @param otherClickSelected If the right click is active
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side);

    /**
     *
     * Event triggered once the <strong>right</strong> click is <strong>released</strong>.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the action
     * @param hand Hand holding the item triggering the action
     * @param otherClickSelected If the right click is active
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side);


}
