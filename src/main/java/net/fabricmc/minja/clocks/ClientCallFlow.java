package net.fabricmc.minja.clocks;

import net.fabricmc.minja.events.MinjaEvent;
import net.fabricmc.minja.events.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.Date;

public class ClientCallFlow {
    /*

    private boolean leftClickFirstTimeServer = true;
    private boolean leftClickFirstTimeClient = true;


    private CallFlow clickCallFlow(PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer, Side side) {

        MinjaEvent state;

        if (leftClickFirstTimeServer && side == Side.SERVER) {
            leftClickFirstTimeServer = false;
            state = onLeftClickPressed(playerEntity, hand, fromServerPlayer, side);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if (!MinecraftClient.getInstance().mouse.wasLeftButtonClicked()) {
                        onLeftClickReleased(playerEntity, hand, fromServerPlayer, side);
                        leftClickPressed = false;
                        leftClickFirstTimeServer = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();
        } else if (leftClickFirstTimeClient && side == Side.CLIENT) {
            leftClickFirstTimeClient = false;
            state = onLeftClickPressed(playerEntity, hand, fromServerPlayer, side);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if (new Date().getTime() - lastLeftClickEvent > TIMER - 10) {
                        onLeftClickReleased(playerEntity, hand, fromServerPlayer, side);
                        leftClickPressed = false;
                        leftClickFirstTimeClient = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();

        }

        return state;
    }
    */



}
