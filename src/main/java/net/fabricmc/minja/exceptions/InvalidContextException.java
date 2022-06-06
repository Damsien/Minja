package net.fabricmc.minja.exceptions;

import net.fabricmc.minja.enumerations.Side;

public class InvalidContextException extends RuntimeException {

    public InvalidContextException(Side side) {
        super("Method cannot be executed in " + side.toString() + " environment");
    }

}
