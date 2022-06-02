package net.fabricmc.minja.events;

import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface PlayerEvent {

    public boolean onSwingItem(Hand hand, boolean fromServerPlayer);



}
