package net.fabricmc.minja.mixin;

import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.events.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Date;

@Mixin(Item.class)
public class ItemMixin implements MouseEvent, ItemEvent, MixinItemEvent {
    private long lastRightClickEvent;
    private boolean rightClickFirstTimeServer = true;

    private boolean rightClickFirstTimeClient = true;
    private boolean rightClickPressed;


    private long lastLeftClickEvent;
    private boolean leftClickFirstTimeServer = true;
    private boolean leftClickFirstTimeClient = true;

    private boolean leftClickPressed;

    private final int TIMER = 200;

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){

        Side side = world.isClient ? Side.CLIENT : Side.SERVER;

        TypedActionResult<ItemStack> state, state2;

        state = onUse(world, user, hand, side);
        state2 = rightClickCallFlow(world, user, hand, side);

        cir.setReturnValue(state2 != null ? state2 : state);

    }

    @Override
    final public boolean interact(World world, PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer) {

        MinjaEvent succeed1, succeed2;

        Side side = world.isClient ? Side.CLIENT : Side.SERVER;

        if(rightClickPressed) return true;
        succeed1 =  onInteract(playerEntity, hand, fromServerPlayer, side);
        succeed2 = leftClickCallFlow(playerEntity, hand, fromServerPlayer, side);

        return succeed1 != MinjaEvent.UNDEFINED ? succeed1 != MinjaEvent.CANCELED : succeed2 != MinjaEvent.CANCELED;

    }




    private TypedActionResult<ItemStack> rightClickCallFlow(World world, PlayerEntity user, Hand hand, Side side) {

        TypedActionResult state = null;

        rightClickPressed = true;

        //Mettre ici l'ouverture de l'HUD
        lastRightClickEvent = new Date().getTime();

        if(rightClickFirstTimeServer && side == Side.SERVER) {
            rightClickFirstTimeServer = false;
            state = onRightClickPressed(world, user, hand, side);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastRightClickEvent > TIMER-10) {
                        onRightClickReleased(world, user, hand, side);
                        rightClickPressed = false;
                        rightClickFirstTimeServer = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();
        }
        else if(rightClickFirstTimeClient && side == Side.CLIENT){
            rightClickFirstTimeClient = false;
            state = onRightClickPressed(world, user, hand, side);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastRightClickEvent > TIMER-10) {
                        onRightClickReleased(world, user, hand, side);
                        rightClickPressed = false;
                        rightClickFirstTimeClient = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();

        }
        else {
            state = onRightClickMaintained(world, user, hand, side);
        }

        if(state == null) state = TypedActionResult.success(user.getStackInHand(hand));

        return state;

    }

    private MinjaEvent leftClickCallFlow(PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer, Side side) {

        MinjaEvent state;

        leftClickPressed = true;

        //Mettre ici l'ouverture de l'HUD
        lastLeftClickEvent = new Date().getTime();

        if(leftClickFirstTimeServer && side == Side.SERVER) {
            leftClickFirstTimeServer = false;
            state = onLeftClickPressed(playerEntity, hand, fromServerPlayer, side);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastLeftClickEvent > TIMER-10) {
                        onLeftClickReleased(playerEntity, hand, fromServerPlayer,side);
                        leftClickPressed = false;
                        leftClickFirstTimeServer = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();
        }
        else if(leftClickFirstTimeClient && side == Side.CLIENT) {
            leftClickFirstTimeClient = false;
            state = onLeftClickPressed(playerEntity, hand, fromServerPlayer, side);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastLeftClickEvent > TIMER-10) {
                        onLeftClickReleased(playerEntity, hand, fromServerPlayer,side);
                        leftClickPressed = false;
                        leftClickFirstTimeClient = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();

        }

        else  state = onLeftClickMaintained(playerEntity, hand, fromServerPlayer, side);

        return state;

    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side) {
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public MinjaEvent onInteract(PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer, Side side) {
        return MinjaEvent.UNDEFINED;
    }


    @Override
    public MinjaEvent onLeftClickPressed(PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickMaintained(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickReleased(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, Side side) {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, Side side) {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, Side side) {
        return null;
    }


}
