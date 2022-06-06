package net.fabricmc.minja.clocks.callflow;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.minja.enumerations.MouseButton;
import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.objects.MinjaItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class MouseManager {

    // Get the caller
    private MinjaItem item;

    // For handling the callflow
    private ClientRightClickCallFlow rightClickClient = new ClientRightClickCallFlow();
    private ServerRightClickCallFlow rightClickServer = new ServerRightClickCallFlow();

    private ClientLeftClickCallFlow  leftClickClient = new ClientLeftClickCallFlow();
    private ServerLeftClickCallFlow  leftClickServer = new ServerLeftClickCallFlow();


    // Preventing animation of right click triggering the left click too
    public boolean isRightClickEnabledOnClient = false;
    public boolean isRightClickEnabledOnServer = false;

    public boolean isLeftClickEnabledOnClient = false;
    public boolean isLeftClickEnabledOnServer = false;

    public MouseManager(MinjaItem item) {

        this.item = item;
        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.MOUSE_TRIGGER_RELEASED, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop
            String target = buf.readString();

            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                switch (target) {
                    case "RIGHT" -> isRightClickEnabledOnServer = false;
                    case "LEFT" -> isLeftClickEnabledOnServer = false;
                }

            });
        });
    }


    public TypedActionResult<ItemStack> getRightClick(World w, PlayerEntity u, Hand h, Side s) {

        GenericCallFlow<TypedActionResult<ItemStack>> callFlow = (s == Side.CLIENT) ?  rightClickClient : rightClickServer;
        callFlow.updateAttribute(w, u, h, s);
        return callFlow.triggerCurrentAction();
    }

    public MinjaEvent getLeftClick(World w, PlayerEntity u, Hand h, Side s) {

        GenericCallFlow<MinjaEvent> callFlow = (s == Side.CLIENT) ?  leftClickClient : leftClickServer;
        callFlow.updateAttribute(w, u, h, s);
        return callFlow.triggerCurrentAction();
    }

    public void tick(Side side) {
        switch(side) {
            case CLIENT -> { leftClickClient.tick(); rightClickClient.tick();}
            case SERVER -> { leftClickServer.tick(); rightClickServer.tick();}
        }
    }



    // Right Click - Client
    class ClientRightClickCallFlow extends GenericCallFlow<TypedActionResult<ItemStack>> {

        @Override public TypedActionResult<ItemStack> triggerReleased() {
            isRightClickEnabledOnClient = false;

            NetworkEvent.triggerMouseReleased(MouseButton.RIGHT);
            return item.onRightClickReleased(this.getWorld(), this.getUser(), this.getHand(), isLeftClickEnabledOnClient, this.getSide());
        }

        @Override public TypedActionResult<ItemStack> triggerPressed() {
            isRightClickEnabledOnClient = true;
            return item.onRightClickPressed(this.getWorld(), this.getUser(), this.getHand(), isLeftClickEnabledOnClient, this.getSide());
        }

        @Override public TypedActionResult<ItemStack> triggerMaintained() {
            return item.onRightClickMaintained(this.getWorld(), this.getUser(), this.getHand(), isLeftClickEnabledOnClient, this.getSide());
        }

        @Override public boolean checkEvent() {
            return !MinecraftClient.getInstance().mouse.wasRightButtonClicked();
        }
    }

    // Left Click - Client

    class ClientLeftClickCallFlow extends LeftClickCallFlow<MinjaEvent> {

        @Override public MinjaEvent triggerReleased() {
            isLeftClickEnabledOnClient = false;
            NetworkEvent.triggerMouseReleased(MouseButton.LEFT);
            return item.onLeftClickReleased(this.getWorld(), this.getUser(), this.getHand(), isRightClickEnabledOnClient, this.getSide());
        }

        @Override public MinjaEvent triggerPressed() {
            isLeftClickEnabledOnClient = true;
            return item.onLeftClickPressed(this.getWorld(), this.getUser(), this.getHand(), isRightClickEnabledOnClient, this.getSide());
        }

        @Override public MinjaEvent triggerMaintained() {
            return item.onLeftClickMaintained(this.getWorld(), this.getUser(), this.getHand(), isRightClickEnabledOnClient, this.getSide());
        }

        @Override public boolean checkEvent() {
            return !MinecraftClient.getInstance().mouse.wasLeftButtonClicked();
        }

    }


    // Right Click - Server
    class ServerRightClickCallFlow extends GenericCallFlow<TypedActionResult<ItemStack>> {

            @Override public TypedActionResult<ItemStack> triggerReleased() {
                return item.onRightClickReleased(this.getWorld(), this.getUser(), this.getHand(), isLeftClickEnabledOnServer, this.getSide());
            }

            @Override public TypedActionResult<ItemStack> triggerPressed() {
                isRightClickEnabledOnServer = true;
                return item.onRightClickPressed(this.getWorld(), this.getUser(), this.getHand(), isLeftClickEnabledOnServer, this.getSide());
            }

            @Override public TypedActionResult<ItemStack> triggerMaintained() {
                return item.onRightClickMaintained(this.getWorld(), this.getUser(), this.getHand(), isLeftClickEnabledOnServer, this.getSide());
            }

        @Override public boolean checkEvent() {
            return !isRightClickEnabledOnServer;
        }


    }

    // Left Click - Server
    class ServerLeftClickCallFlow extends LeftClickCallFlow<MinjaEvent> {

        @Override public MinjaEvent triggerReleased() {
            return item.onLeftClickReleased(this.getWorld(), this.getUser(), this.getHand(), isRightClickEnabledOnServer, this.getSide());
        }

        @Override public MinjaEvent triggerPressed() {
            isLeftClickEnabledOnServer = true;
            return item.onLeftClickPressed(this.getWorld(), this.getUser(), this.getHand(), isRightClickEnabledOnServer, this.getSide());
        }

        @Override public MinjaEvent triggerMaintained() {
            return item.onLeftClickMaintained(this.getWorld(), this.getUser(), this.getHand(), isRightClickEnabledOnServer, this.getSide());
        }

        @Override public boolean checkEvent() {
            return !isLeftClickEnabledOnServer;
        }

    }




}
