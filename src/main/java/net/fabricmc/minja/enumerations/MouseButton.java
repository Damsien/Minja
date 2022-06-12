package net.fabricmc.minja.enumerations;

/**
 *  Identifiable buttons
 *
 */
public enum MouseButton {
    /**
     * Right button of the mouse
     */
    RIGHT("RIGHT"),

    /**
     * Left button of the mouse
     */
    LEFT("LEFT");

    private final String text;

    /**
     * Associate a label to the mouse button
     * @param text label
     */
    MouseButton(final String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return text;
    }
}