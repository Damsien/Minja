package net.fabricmc.minja.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.minja.enumerations.MouseButton;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Send data packet to the "Server" to keep Client and Server Up To Date
 *
 * @author      Tom Froment
 */
public class NetworkEvent {

    /** Identifier for triggering a mouse release (or registering the callback) */
    public static Identifier MOUSE_TRIGGER_RELEASED = new Identifier("mouse", "released");

    /**
     *  Trigger the release of the mouse for the Server.
     *
     * @param button Button released
     */
    public static void triggerMouseReleased(MouseButton button) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(button.toString());
        ClientPlayNetworking.send(NetworkEvent.MOUSE_TRIGGER_RELEASED, buf);
    }



    /** Identifier for updating the index of the spell (or registering the callback) */
    public static Identifier UPDATE_SPELL_INDEX = new Identifier("spells", "active_spell_index");

    /**
     *  Updating the index of the active spell for the Server Player.
     *
     * @param spellIndex index of the active spell
     */
    public static void updateSpellIndex(int spellIndex) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(spellIndex);
        ClientPlayNetworking.send(NetworkEvent.UPDATE_SPELL_INDEX, buf);
    }


    /** Identifier for making spawn an entity (or registering the callback) */
    public static  Identifier SPAWN_ENTITY = new Identifier("entity", "spawn");

    /**
     *  Making spawn an entity at a certain position.
     *
     * @param entityType the type of the entity
     * @param position the position of the mob
     */
    public static void spawnEntity(EntityType<?> entityType, BlockPos position) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(entityType.getUntranslatedName());
        buf.writeBlockPos(position);
        ClientPlayNetworking.send(NetworkEvent.SPAWN_ENTITY, buf);
    }

    /** Identifier for "highlighting" a block (or registering the callback) */
    public static Identifier HIGHLIGHT_BLOCK = new Identifier("block", "highlight");

    /**
     * Highlight a block for a player
     *
     * @param position the position of the block
     */
    public static void hightlightBlock(BlockPos position) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(position);
        ClientPlayNetworking.send(NetworkEvent.HIGHLIGHT_BLOCK, buf);
    }

    /** Identifier for unregistering all lighted blocks (or registering the callback) */
    public static Identifier UNHIGHLIGHT_ALL_BLOCKS = new Identifier("block", "unhighlight_all");

    /**
     * Unhighlight (and unregister) all the glowing blocks
     */
    public static void unhightlightAllBlocks() {
        ClientPlayNetworking.send(NetworkEvent.UNHIGHLIGHT_ALL_BLOCKS, PacketByteBufs.empty());
    }

}
