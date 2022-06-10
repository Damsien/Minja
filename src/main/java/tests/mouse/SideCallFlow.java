package tests.mouse;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.objects.MinjaItem;
import net.fabricmc.minja.util.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SideCallFlow extends MinjaItem {

    private static SideCallFlow RHUNE;

    public SideCallFlow(Item.Settings settings) {
        super(settings);
        RHUNE = this;
    }

    public static Item getRune1(){
        return RHUNE;
    }

    @Override public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        String sender = side == Side.CLIENT ? "§6CLIENT" : "§dSERVER";
        Messenger.sendMessage(playerEntity, "§2Left click §aPRESSED §2from " + sender);
        return MinjaEvent.SUCCEED;
    }

    @Override public MinjaEvent onLeftClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        String sender = side == Side.CLIENT ? "§6CLIENT" : "§dSERVER";
        Messenger.sendMessage(playerEntity, "§3Left click §bMAINTAINED §3from " + sender);
        return MinjaEvent.SUCCEED;
    }


    @Override public MinjaEvent onLeftClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected, Side side) {
        String sender = side == Side.CLIENT ? "§6CLIENT" : "§dSERVER";
        Messenger.sendMessage(playerEntity, "§4Left click §cRELEASED §4from " + sender);
        return MinjaEvent.SUCCEED;
    }


}
