package net.fabricmc.minja.events;

import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerEvent {

    public void onSwingItem(Hand hand, boolean fromServerPlayer, CallbackInfo cir);



}
