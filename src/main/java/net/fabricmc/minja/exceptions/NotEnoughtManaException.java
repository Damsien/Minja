package net.fabricmc.minja.exceptions;

public class NotEnoughtManaException extends Exception {

    public NotEnoughtManaException(String msg, int cost, int manaAvailable) {
        super(msg + " - (Cost: " + cost + " - Mana available: " + manaAvailable + ")");
    }

}
