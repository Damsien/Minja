package net.fabricmc.minja.events;

import net.minecraft.util.Hand;

public interface MixinItemEvent {

    /**
     *  Mixin usage only :
     *  Entrypoint for left click event (such as use event)
     *
     *  This method should not be Overrided in other class than Mixin or for preventing its usage
     *
     * @return succeed : Indicate if the event must be canceled or not
     */
    public boolean interact(Hand hand, boolean fromServerPlayer);
}
