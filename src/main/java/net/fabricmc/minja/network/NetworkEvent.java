package net.fabricmc.minja.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.minja.enumerations.MouseButton;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

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

    public static  Identifier SPAWN_ENTITY = new Identifier("entity", "spawn");

    public static void spawnEntity(EntityType<?> entityType, BlockPos position) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(entityType.getUntranslatedName());
        buf.writeBlockPos(position);
        ClientPlayNetworking.send(NetworkEvent.SPAWN_ENTITY, buf);
    }

    public static Identifier HIGHLIGHT_BLOCK = new Identifier("block", "highlight");

    public static void hightlightBlock(BlockPos position) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(position);
        ClientPlayNetworking.send(NetworkEvent.HIGHLIGHT_BLOCK, buf);
    }

    public static Identifier UNHIGHLIGHT_ALL_BLOCKS = new Identifier("block", "unhighlight_all");

    public static void unhightlightAllBlocks() {
        ClientPlayNetworking.send(NetworkEvent.UNHIGHLIGHT_ALL_BLOCKS, PacketByteBufs.empty());
    }

}
