package net.fabricmc.minja.enumerations;

public enum MouseButton {
    RIGHT("RIGHT"),
    LEFT("LEFT");

    private final String text;

    /**
     * @param text
     */
    MouseButton(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}