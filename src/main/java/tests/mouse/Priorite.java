package tests.mouse;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.objects.MinjaItem;
import net.fabricmc.minja.util.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Priorite extends MinjaItem  {

    private static Priorite RHUNE;

    public Priorite(Item.Settings settings) {
        super(settings);
        RHUNE = this;
    }

    public static Item getRune2(){
        return RHUNE;
    }

    @Override public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        String sender = side == Side.CLIENT ? "§6CLIENT" : "§dSERVER";
        Messenger.sendMessage(playerEntity, "§3Comportement attendu :  Event annulé");
        return MinjaEvent.SUCCEED;
    }


    @Override
    public MinjaEvent onLeftClickClientPressed(World world, net.minecraft.entity.player.PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        return MinjaEvent.CANCELED;
    }



    @Override
    public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        Messenger.sendMessage(playerEntity, "§3Comportement attendu :  Event déclenché");
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }


}
