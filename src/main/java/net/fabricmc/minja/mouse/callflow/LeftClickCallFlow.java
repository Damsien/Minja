package net.fabricmc.minja.mouse.callflow;

import net.fabricmc.minja.mouse.context.Context;

/**
 *  Simulates the generic callflow of a left click. <br>
 *
 * @param <EventCallback> Type of return of callback functions
 *
 * @see GenericCallFlow
 *
 * @author      Tom Froment
 */
public abstract class LeftClickCallFlow<EventCallback> extends GenericCallFlow<EventCallback> {


    @Override
    public void tick() {

        if(!isFirstTimeClicked) {

            if(isReleased()) {
                triggerReleased();
                afterReleased();
                isFirstTimeClicked = true;
            } else {
                afterMaintained();
                triggerMaintained();
            }
        }
    }

}
