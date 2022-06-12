package net.fabricmc.minja.exceptions;

import net.fabricmc.minja.enumerations.Side;

/**
 * Exception raised when a method is called in an incorrect context.
 *
 */
public class InvalidContextException extends RuntimeException {

    public InvalidContextException(Side side) {
        super("Method cannot be executed in " + side.toString() + " environment");
    }

}
