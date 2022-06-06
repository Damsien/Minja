package net.fabricmc.minja.exceptions;

public class NotEnoughManaException extends Exception {

    public NotEnoughManaException(String msg, int cost, int manaAvailable) {
        super(msg + " - (Cost: " + cost + " - Mana available: " + manaAvailable + ")");
    }

}
