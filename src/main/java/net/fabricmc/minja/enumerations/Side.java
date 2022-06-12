package net.fabricmc.minja.enumerations;

/**
 *  Side of an event
 *
 */
public enum Side {

    CLIENT("CLIENT"),

    SERVER("SERVER");

    private final String text;

    /**
     * @param text
     */
    Side(final String text) {
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
