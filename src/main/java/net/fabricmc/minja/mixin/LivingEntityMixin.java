package net.fabricmc.minja.mixin;

import net.fabricmc.minja.events.PlayerEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injection to the class LivingEntity
 *
 * @author      Tom Froment
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     * Injection of the swingHand method. <br>
     *
     * This event is originally triggered when an entity is interacting with its hand. <br>
     * For a player, it is equivalent to press the left click button of its mouse. <br>
     * However, overwriting it to the child class could break the compatibility with other mods. <br>
     * That's why injection is made to the parent class providing the method.
     *
     * @param hand Hand of the entity
     * @param fromServerPlayer
     * @param cir success of the event
     */
    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"), cancellable = true)
    public void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo cir) {

        if((Object) (this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) (this);
            boolean succeed = ((PlayerEvent)player).onSwingItem(hand, fromServerPlayer);
            if(!succeed) cir.cancel();
        }

    }


}
