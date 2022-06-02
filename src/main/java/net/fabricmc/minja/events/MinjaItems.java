package net.fabricmc.minja.events;

import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.mixin.ItemMixin;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Date;

/**
 * Classe abstraite permettant l'usage du @Override sur les méthodes injectées dans ItemMixin (dont le compilateur
 * n'a pas connaissance) sans pour autant avoir à redéfinir chacune de ces méthodes. (équivalent d'un MouseAdapter en Swing)
 *
 */
public abstract class MinjaItems extends Item implements MouseEvent, ItemEvent {

    public MinjaItems(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand) {return null;};

    @Override
    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand) {return null;};

    @Override
    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand) {return null;};

    @Override
    public MinjaEvent onInteract(Hand hand, boolean fromServerPlayer) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand) {return TypedActionResult.success(user.getStackInHand(hand));}

    @Override
    public MinjaEvent onLeftClickMaintained(Hand hand, boolean playerFromServer) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickPressed(Hand hand, boolean playerFromServer) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickReleased(Hand hand, boolean playerFromServer) {
        return MinjaEvent.UNDEFINED;
    }

}
