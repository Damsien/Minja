package net.fabricmc.minja.events;

import net.fabricmc.minja.enumerations.Side;

/**
 *  Events related to tick system
 *
 */
public interface Tickable {


    /**
     *  Events triggered once a tick has been emitted
     *
     */
    void onTick(Side side);

}
