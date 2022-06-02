package net.fabricmc.minja.mixin;

import net.fabricmc.minja.Minja;
import net.fabricmc.minja.events.MouseEvent;
import net.fabricmc.minja.events.PlayerEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;Z)V", at = @At("HEAD"))
    public void swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo cir) {

        if((Object) (this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) (this);
            ((PlayerEvent)player).onSwingItem(hand, fromServerPlayer, cir);
        }

    }

    @Inject(method =    "onEquipStack(Lnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
    protected void onEquipStack(ItemStack stack, CallbackInfo cir) {

    }


}
