package net.fabricmc.minja.events;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.minja.enumerations.MouseButton;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class NetworkEvent {

    public static Identifier MOUSE_TRIGGER_RELEASED = new Identifier("mouse", "released");

    public static void triggerMouseReleased(MouseButton button) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(button.toString());
        ClientPlayNetworking.send(NetworkEvent.MOUSE_TRIGGER_RELEASED, buf);
    }
    public static Identifier UPDATE_SPELL_INDEX = new Identifier("spells", "active_spell_index");

    public static void updateSpellIndex(int spellIndex) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(spellIndex);
        ClientPlayNetworking.send(NetworkEvent.UPDATE_SPELL_INDEX, buf);
    }

}
