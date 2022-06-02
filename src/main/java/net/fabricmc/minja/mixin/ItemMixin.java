package net.fabricmc.minja.mixin;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin implements MouseEvent {

    private boolean rightFirstTime = true;
    private boolean leftFirstTime = true;

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult", at = @At("TAIL"))
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){

    }

    @Override
    public void onLeftClickPressed() {
        Item item = (Item) (Object) (this);
        //MinecraftClient.getInstance().player.sendMessage(new LiteralText("L'objet " + item + "est sélectionné"), false);
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
