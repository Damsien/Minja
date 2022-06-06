package net.fabricmc.minja.events;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.enumerations.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *  Events related to a Mouse
 *
 *  @author      Tom Froment
 */
public interface PlayerEvent {


    /**
     *  <strong>Mixin usage only :</strong> <br>
     *  Entrypoint for left click event (such as use event) <br><br>
     *
     *  This method should not be Override in other class than Mixin or for preventing its usage. <br><br>
     *
     *  @param hand Hand holding the item
     *  @param fromServerPlayer
     *
     * @return succeed : Indicate if the event must be canceled or not
     */
    public boolean onSwingItem(Hand hand, boolean fromServerPlayer);



}
