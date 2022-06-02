package net.fabricmc.minja.objects;

import net.fabricmc.minja.events.ItemEvent;
import net.fabricmc.minja.events.MinjaEvent;
import net.fabricmc.minja.events.MouseEvent;
import net.fabricmc.minja.events.Side;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Classe abstraite permettant l'usage du @Override sur les méthodes injectées dans ItemMixin (dont le compilateur
 * n'a pas connaissance) sans pour autant avoir à redéfinir chacune de ces méthodes. (équivalent d'un MouseAdapter en Swing)
 *
 */
public abstract class MinjaItem extends Item implements MouseEvent, ItemEvent {

    public MinjaItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, Side side) {return null;};

    @Override
    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, Side side) {return null;};

    @Override
    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, Side side) {return null;};

    @Override
    public MinjaEvent onInteract(PlayerEntity playerEntity, Hand hand, boolean fromServerPlayer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side) {return TypedActionResult.success(user.getStackInHand(hand));}

    @Override
    public MinjaEvent onLeftClickMaintained(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickPressed(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

    @Override
    public MinjaEvent onLeftClickReleased(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side) {
        return MinjaEvent.UNDEFINED;
    }

}
