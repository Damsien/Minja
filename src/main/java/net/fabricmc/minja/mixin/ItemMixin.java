package net.fabricmc.minja.mixin;

import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.events.ItemEvent;
import net.fabricmc.minja.events.MinjaEvent;
import net.fabricmc.minja.events.MixinItemEvent;
import net.fabricmc.minja.events.MouseEvent;
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
    private boolean rightClickFirstTime = true;
    private boolean rightClickPressed;


    private long lastLeftClickEvent;
    private boolean leftClickFirstTime = true;

    private boolean leftClickPressed;

    private final int TIMER = 200;

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){

        TypedActionResult<ItemStack> state, state2;

        state = onUse(world, user, hand);
        state2 = rightClickCallFlow(world, user, hand);

        cir.setReturnValue(state2 != null ? state2 : state);

    }

    @Override
    final public boolean interact(Hand hand, boolean fromServerPlayer) {

        MinjaEvent succeed1, succeed2;

        if(rightClickPressed) return true;
        succeed1 =  onInteract(hand, fromServerPlayer);
        succeed2 = leftClickCallFlow(hand, fromServerPlayer);

        return succeed1 != MinjaEvent.UNDEFINED ? succeed1 != MinjaEvent.CANCELED : succeed2 != MinjaEvent.CANCELED;

    }




    private TypedActionResult<ItemStack> rightClickCallFlow(World world, PlayerEntity user, Hand hand) {

        TypedActionResult state = null;

        rightClickPressed = true;

        //Mettre ici l'ouverture de l'HUD
        lastRightClickEvent = new Date().getTime();

        if(rightClickFirstTime) {
            rightClickFirstTime = false;
            state = onRightClickPressed(world, user, hand);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastRightClickEvent > TIMER-10) {
                        onRightClickReleased(world, user, hand);
                        rightClickPressed = false;
                        rightClickFirstTime = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();
        }

        else {
            state = onRightClickMaintained(world, user, hand);
        }

        if(state == null) state = TypedActionResult.success(user.getStackInHand(hand));

        return state;

    }

    private MinjaEvent leftClickCallFlow(Hand hand, boolean fromServerPlayer) {

        MinjaEvent state;

        rightClickPressed = true;

        //Mettre ici l'ouverture de l'HUD
        lastLeftClickEvent = new Date().getTime();

        if(leftClickFirstTime) {
            leftClickFirstTime = false;
            state = onLeftClickPressed(hand, fromServerPlayer);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastLeftClickEvent > TIMER-10) {
                        onLeftClickReleased(hand, fromServerPlayer);
                        rightClickPressed = false;
                        leftClickFirstTime = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();
        }

        else  state = onLeftClickMaintained(hand, fromServerPlayer);

        return state;

    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public MinjaEvent onInteract(Hand hand, boolean fromServerPlayer) {
        return MinjaEvent.UNDEFINED;
    }


    @Override
    public MinjaEvent onLeftClickPressed(Hand hand, boolean fromServerPlayer) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickMaintained(Hand hand, boolean playerFromServer) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickReleased(Hand hand, boolean playerFromServer) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand) {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand) {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand) {
        return null;
    }


}
