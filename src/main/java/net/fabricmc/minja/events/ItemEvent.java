package net.fabricmc.minja.events;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.enumerations.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 *  Events related to an Item
 *
 *  @author      Tom Froment
 */
public interface ItemEvent {

    /**
     * Event triggered once an Item is used (right click).
     *
     * @param world World in which the event occurred
     * @param user  Owner of the item
     * @param hand  Hand holding the item
     * @param side  Side in which the event occurred
     *
     * @return success of the event
     */
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side);

    /**
     *
     * Event triggered once a Player interact with the current item in his hand.
     *
     * @param world World in which the event occurred
     * @param playerEntity Owner of the item
     * @param hand Hand holding the item
     * @param side Side in which the event occurred
     *
     * @return success of the event
     */
    public MinjaEvent onInteract(World world, PlayerEntity playerEntity, Hand hand, Side side);


    /**
     *  <strong>Mixin usage only :</strong> <br>
     *  Entrypoint for left click event (such as use event) <br><br>
     *
     *  This method should not be Override in other class than Mixin or for preventing its usage <br><br>
     *
     * @param world World in which the event occurred
     * @param player Owner of the item
     * @param hand Hand holding the item
     * @param fromServerPlayer
     *
     * @return succeed : Indicate if the event must be canceled or not
     */
    public boolean interact(World world, PlayerEntity player, Hand hand, boolean fromServerPlayer);

}
