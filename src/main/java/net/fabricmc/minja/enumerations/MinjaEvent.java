package net.fabricmc.minja.enumerations;

/**
 *  State of a cancellable event
 *
 * @author      Tom Froment
 */
public enum MinjaEvent {

    /**
     *  Default value if no listener has been redefined. Act as a success
     */
    UNDEFINED,

    /**
     * Succeed an event
     */
    SUCCEED,

    /**
     * Cancel an event
     */
    CANCELED
}
