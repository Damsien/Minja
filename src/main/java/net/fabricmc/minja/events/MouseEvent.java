package net.fabricmc.minja.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface MouseEvent {

    public MinjaEvent onLeftClickPressed(Hand hand, boolean playerFromServer);

    public MinjaEvent onLeftClickMaintained(Hand hand, boolean playerFromServer);

    public MinjaEvent onLeftClickReleased(Hand hand, boolean playerFromServer);

    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand);

    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand);

    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand);


}
