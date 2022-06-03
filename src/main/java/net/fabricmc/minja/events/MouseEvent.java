package net.fabricmc.minja.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface MouseEvent {

    public MinjaEvent onLeftClickPressed(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side);

    public MinjaEvent onLeftClickMaintained(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side);

    public MinjaEvent onLeftClickReleased(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side);

    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, Side side);

    public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, Side side);

    public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, Side side);


}
