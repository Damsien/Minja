package tests.mouse;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.objects.MinjaItem;
import net.fabricmc.minja.util.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ClientServerCallFlow extends MinjaItem {
    private static ClientServerCallFlow RHUNE;

    public ClientServerCallFlow(Settings settings) {
        super(settings);
        RHUNE = this;
    }

    public static Item getRune0(){
        return RHUNE;
    }

    @Override public MinjaEvent onLeftClickClientPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        Messenger.sendMessage(playerEntity, "§2Left click §aPRESSED §2from §6CLIENT");
        return MinjaEvent.SUCCEED;
    }
    @Override public MinjaEvent onLeftClickServerPressed(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        Messenger.sendMessage(playerEntity, "§2Left click §aPRESSED §2from §dSERVER");
        return MinjaEvent.SUCCEED;
    }


    @Override public MinjaEvent onLeftClickClientMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        Messenger.sendMessage(playerEntity, "§3Left click §bMAINTAINED §3from §6CLIENT");
        return MinjaEvent.SUCCEED;
    }

    @Override public MinjaEvent onLeftClickServerMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        Messenger.sendMessage(playerEntity, "§3Left click §bMAINTAINED §3from §dSERVER");
        return MinjaEvent.SUCCEED;
    }


    @Override public MinjaEvent onLeftClickClientReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        Messenger.sendMessage(playerEntity, "§4Left click §cRELEASED §4from §6CLIENT");
        return MinjaEvent.SUCCEED;
    }

    @Override public MinjaEvent onLeftClickServerReleased(World world, PlayerEntity playerEntity, Hand hand, boolean otherClickSelected) {
        Messenger.sendMessage(playerEntity, "§4Left click §cRELEASED §4from §6SERVER");
        return MinjaEvent.SUCCEED;
    }

}
