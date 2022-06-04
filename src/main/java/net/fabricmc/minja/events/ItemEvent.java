package net.fabricmc.minja.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ItemEvent {

    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side);

    public MinjaEvent onInteract(World world, PlayerEntity playerEntity, Hand hand, Side side);


    /**
     *  Mixin usage only :
     *  Entrypoint for left click event (such as use event)
     *
     *  This method should not be Overrided in other class than Mixin or for preventing its usage
     *
     * @return succeed : Indicate if the event must be canceled or not
     */
    public boolean interact(World world, PlayerEntity player, Hand hand, boolean fromServerPlayer);

}
