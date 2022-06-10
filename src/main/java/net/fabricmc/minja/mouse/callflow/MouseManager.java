package net.fabricmc.minja.mouse.callflow;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.minja.enumerations.MouseButton;
import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.mouse.context.Context;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.objects.MinjaItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 *  Mouse click manager that centralizes all the functional part into an Item
 *
 *
 * @author      Tom Froment
 */
public class MouseManager {

    // Get the caller
    private MinjaItem item;

    // For handling the callflow
    private ClientRightClickCallFlow rightClickClient = new ClientRightClickCallFlow();
    private ServerRightClickCallFlow rightClickServer = new ServerRightClickCallFlow();

    private ClientLeftClickCallFlow  leftClickClient = new ClientLeftClickCallFlow();
    private ServerLeftClickCallFlow  leftClickServer = new ServerLeftClickCallFlow();


    // To inform the receiver of the state of the other click
    public boolean isRightClickEnabledOnClient = false;
    public boolean isRightClickEnabledOnServer = false;

    public boolean isLeftClickEnabledOnClient = false;
    public boolean isLeftClickEnabledOnServer = false;

    // Event parameters

    // Constructor

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


    /**
     * Trigger the current action of the right click according to its caller
     *
     * @param w World in which the action took place
     * @param u User who has triggered the action
     * @param h Hand which is holding the item
     * @param s Side which has called the method
     *
     * @return the success of the event
     */
    public TypedActionResult<ItemStack> triggerRightClickAction(World w, PlayerEntity u, Hand h, Side s) {

        // Get the targeted callFlow
        GenericCallFlow<TypedActionResult<ItemStack>> callFlow = (s == Side.CLIENT) ?  rightClickClient : rightClickServer;

        // Update the context before triggering the action
        callFlow.changeContext(new Context(w, u, h, s));

        // Trigger the current action
        return callFlow.triggerCurrentAction();
    }

    /**
     * Trigger the current action of the left click according to its caller
     *
     * @param w World in which the action took place
     * @param u User who has triggered the action
     * @param h Hand which is holding the item
     * @param s Side which has called the method
     *
     * @return the success of the event
     */
    public MinjaEvent triggerLeftClickAction(World w, PlayerEntity u, Hand h, Side s) {

        // Get the targeted callFlow
        GenericCallFlow<MinjaEvent> callFlow = (s == Side.CLIENT) ?  leftClickClient : leftClickServer;

        // Update the context before triggering the action
        callFlow.changeContext(new Context(w, u, h, s));

        // Trigger the current action
        return callFlow.triggerCurrentAction();
    }

    /**
     * Send the tick signal emit by the {@link MinjaItem#tick(World) MinjaItem} to the {@link GenericCallFlow#tick() CallFlow manager}.
     *
     * @param side Side which is emitting the tick
     */
    public void tick(Side side) {
        switch(side) {
            case CLIENT -> { leftClickClient.tick(); rightClickClient.tick();}
            case SERVER -> { leftClickServer.tick(); rightClickServer.tick();}
        }
    }


    /**
     *  CallFlow simulating the <strong>right click</strong> on <strong>Client</strong> side
     */
    class ClientRightClickCallFlow extends GenericCallFlow<TypedActionResult<ItemStack>> {

        @Override public TypedActionResult<ItemStack> triggerPressed() {
            isRightClickEnabledOnClient = true;
            Context c = this.getContext();


            TypedActionResult<ItemStack> event1 = item.onRightClickPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            TypedActionResult<ItemStack> event2 = item.onRightClickClientPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != null ? event2 : event1;
        }

        @Override public TypedActionResult<ItemStack> triggerMaintained() {
            Context c = this.getContext();

            TypedActionResult<ItemStack> event1 = item.onRightClickMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            TypedActionResult<ItemStack> event2 = item.onRightClickClientMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != null ? event2 : event1;
        }

        @Override public TypedActionResult<ItemStack> triggerReleased() {
            isRightClickEnabledOnClient = false;
            isRightClickEnabledOnServer = false;
            NetworkEvent.triggerMouseReleased(MouseButton.RIGHT);
            Context c = this.getContext();

            TypedActionResult<ItemStack> event1 = item.onRightClickReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            TypedActionResult<ItemStack> event2 = item.onRightClickClientReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != null ? event2 : event1;
        }



        @Override public boolean isReleased() {
            return !MinecraftClient.getInstance().mouse.wasRightButtonClicked();
        }
    }


    /**
     *  CallFlow simulating the <strong>left click</strong> on <strong>Client</strong> side
     */
    class ClientLeftClickCallFlow extends LeftClickCallFlow<MinjaEvent> {



        @Override public MinjaEvent triggerPressed() {
            isLeftClickEnabledOnClient = true;
            Context c = this.getContext();

            MinjaEvent event1 = item.onLeftClickPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            MinjaEvent event2 = item.onLeftClickClientPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != MinjaEvent.UNDEFINED ? event2 : event1;
        }

        @Override public MinjaEvent triggerMaintained() {
            Context c = this.getContext();

            MinjaEvent event1 = item.onLeftClickMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            MinjaEvent event2 = item.onLeftClickClientMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != MinjaEvent.UNDEFINED ? event2 : event1;
        }

        @Override public MinjaEvent triggerReleased() {
            isLeftClickEnabledOnClient = false;
            isLeftClickEnabledOnServer = false;
            NetworkEvent.triggerMouseReleased(MouseButton.LEFT);
            Context c = this.getContext();

            MinjaEvent event1 = item.onLeftClickReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            MinjaEvent event2 = item.onLeftClickClientReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != MinjaEvent.UNDEFINED ? event2 : event1;
        }



        @Override public boolean isReleased() {
            return !MinecraftClient.getInstance().mouse.wasLeftButtonClicked();
        }

    }


    /**
     *  CallFlow simulating the <strong>right click</strong> on <strong>Server</strong> side
     */
    class ServerRightClickCallFlow extends GenericCallFlow<TypedActionResult<ItemStack>> {

        @Override public TypedActionResult<ItemStack> triggerPressed() {
            isRightClickEnabledOnServer = true;
            Context c = this.getContext();

            TypedActionResult<ItemStack> event1 = item.onRightClickPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            TypedActionResult<ItemStack> event2 = item.onRightClickServerPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != null ? event2 : event1;
        }

        @Override public TypedActionResult<ItemStack> triggerMaintained() {
            Context c = this.getContext();

            TypedActionResult<ItemStack> event1 = item.onRightClickMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            TypedActionResult<ItemStack> event2 = item.onRightClickServerMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != null ? event2 : event1;
        }

        @Override public TypedActionResult<ItemStack> triggerReleased() {
            Context c = this.getContext();

            TypedActionResult<ItemStack> event1 = item.onRightClickReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            TypedActionResult<ItemStack> event2 = item.onRightClickServerReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != null ? event2 : event1;
        }

        @Override public boolean isReleased() {
            return !isRightClickEnabledOnServer;
        }


    }

    /**
     *  CallFlow simulating the <strong>left click</strong> on <strong>Server</strong> side
     */
    class ServerLeftClickCallFlow extends LeftClickCallFlow<MinjaEvent> {

        @Override public MinjaEvent triggerPressed() {
            isLeftClickEnabledOnServer = true;
            Context c = this.getContext();


            MinjaEvent event1 = item.onLeftClickPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            MinjaEvent event2 = item.onLeftClickServerPressed(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != MinjaEvent.UNDEFINED ? event2 : event1;
        }

        @Override public MinjaEvent triggerMaintained() {
            Context c = this.getContext();

            MinjaEvent event1 = item.onLeftClickMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            MinjaEvent event2 = item.onLeftClickServerMaintained(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != MinjaEvent.UNDEFINED ? event2 : event1;
        }

        @Override public MinjaEvent triggerReleased() {
            Context c = this.getContext();

            MinjaEvent event1 = item.onLeftClickReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient, c.getSide());
            MinjaEvent event2 = item.onLeftClickServerReleased(c.getWorld(), c.getUser(), c.getHand(), isRightClickEnabledOnClient);
            return event2 != MinjaEvent.UNDEFINED ? event2 : event1;
        }

        @Override public boolean isReleased() {
            return !isLeftClickEnabledOnServer;
        }

    }




}
