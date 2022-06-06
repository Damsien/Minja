package net.fabricmc.minja.clocks.callflow;

public abstract class LeftClickCallFlow<EventCallback> extends GenericCallFlow<EventCallback> {

    @Override
    public void tick() {

        if(!isFirstTimeClicked) {

            if(checkEvent()) {
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
