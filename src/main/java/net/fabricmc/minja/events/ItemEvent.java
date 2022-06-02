package net.fabricmc.minja.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ItemEvent {

    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand);

    public MinjaEvent onInteract(Hand hand, boolean fromServerPlayer);



}
