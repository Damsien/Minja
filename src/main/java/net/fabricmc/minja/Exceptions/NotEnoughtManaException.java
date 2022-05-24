package net.fabricmc.minja.Exceptions;

public class NotEnoughtManaException extends Exception {

    public NotEnoughtManaException(String msg, int cost, int manaAvailable) {
        super(msg + " - (Cost: " + cost + " - Mana available: " + manaAvailable + ")");
    }

}
