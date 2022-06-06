package net.fabricmc.minja.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

/**
 * Tool for send a message to a player
 *
 * @author     Tom Froment
 */
public class Messenger {

    /**
     *  Send a message to a player in the chat
     *
     * @param player Message recipient
     * @param message Message to send
     */
    public static void sendMessage(PlayerEntity player, String message) {
        sendMessage(player, message, false);
    }

    /**
     *  Send a message to a player
     *
     * @param player Message recipient
     * @param message Message to send
     * @param inBar Display the message in the action bar (true) or in the chat (false)
     *
     */
    public static void sendMessage(PlayerEntity player, String message, boolean inBar) {
        player.sendMessage(new LiteralText(message), inBar);
    }

}
