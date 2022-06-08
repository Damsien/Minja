package net.fabricmc.minja.clocks;

import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.player.PlayerMinja;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;

public class ManaClock extends Clock {

    private static ManaClock instance = null;

    private ClientPlayNetworkHandler networkHandler;

    private static final Map<String, PlayerMinja> players = new HashMap<String, PlayerMinja>();

    private ManaClock(long timer)
    {
        super(timer);
    }

    public static ManaClock getInstance(long timer, PlayerMinja player) {
        if (instance == null) {
            instance = new ManaClock(timer);
        }

        if (!players.containsKey(ClientPlayerEntity.class.toString())
                && ClientPlayerEntity.class.toString().equals(player.getClass().toString())) {
            players.put(ClientPlayerEntity.class.toString(), player);
        }

        if (!players.containsKey(ServerPlayerEntity.class.toString())
                && ServerPlayerEntity.class.toString().equals(player.getClass().toString())) {
            players.put(ServerPlayerEntity.class.toString(), player);
        }

        return instance;
    }

    @Override
    public void execute() {
        if (MinecraftClient.getInstance().getNetworkHandler() != null && networkHandler == null) {
            networkHandler = MinecraftClient.getInstance().getNetworkHandler();
            NetworkEvent.runManaRegeneration();
        }
        if(networkHandler != null) {
            for(PlayerMinja player : players.values()) {
                player.addMana(5);
            }
            this.start();
        }
    }

    @Override
    public void stop() {
        players.clear();
        instance = null;
        super.stop();
    }
}
