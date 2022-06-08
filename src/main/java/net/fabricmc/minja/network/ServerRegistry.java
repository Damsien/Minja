package net.fabricmc.minja.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.minja.Minja;
import net.fabricmc.minja.mixin.PlayerEntityMixin;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Registry for all global functions
 *
 * @author      Tom Froment
 */
public class ServerRegistry {

    private static Map<RetentionPosition, MagmaCubeEntity>  retentionHighlightedBlocks = new HashMap<>();

    /**
     * Register all the global functions
     *
     * @author      Tom Froment
     */
    public static void registerAllEvents() {

        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.MANA_REGENERATION, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop
            buf.clear();

            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                ((PlayerMinja)player).runManaRegeneration();

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.UPDATE_SPELL_INDEX, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop
            int target = buf.readInt();
            buf.clear();

            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                ((PlayerMinja)player).setActiveSpell(target);

            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.SPAWN_ENTITY, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop
            String id = buf.readString();
            BlockPos position = buf.readBlockPos();
            buf.clear();

            client.execute(() -> {
                // Everything in this lambda is run on the render thread

                LivingEntity entity = (LivingEntity) EntityType.get(id).get().create(player.getWorld());
                entity.updatePosition(position.getX(),position.getY(),position.getZ());
                player.getWorld().spawnEntity(entity);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.HIGHLIGHT_BLOCK, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop
            BlockPos position = buf.readBlockPos();
            buf.clear();

            client.execute(() -> {
                // Everything in this lambda is run on the render thread

                EntityType<MagmaCubeEntity> entityType = EntityType.MAGMA_CUBE;

                NbtCompound nbtProperties = new NbtCompound();
                nbtProperties.put("Size", NbtInt.of(1));
                nbtProperties.put("Silent", NbtByte.of(true));
                nbtProperties.put("Invulnerable", NbtByte.of(true));
                nbtProperties.put("NoAI", NbtByte.of(true));
                nbtProperties.put("PersistanceRequired", NbtByte.of(true));

                MagmaCubeEntity entity = entityType.create(player.world);
                entity.updatePosition(0, 0, 0);

                entity.readNbt(nbtProperties);

                entity.setStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 9999),player);

                entity.setGlowing(true);

                entity.updatePosition(position.getX(), position.getY(), position.getZ());

                player.getWorld().spawnEntity(entity);

                retentionHighlightedBlocks.put(new RetentionPosition(position), entity);

            });
        });


        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.UNHIGHLIGHT_ALL_BLOCKS, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop

            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                for(RetentionPosition key : retentionHighlightedBlocks.keySet()) {
                    MagmaCubeEntity block = retentionHighlightedBlocks.get(key);
                    block.remove(Entity.RemovalReason.KILLED);
                    retentionHighlightedBlocks.remove(key);
                }
            });
        });


    }


    /**
     * Representation of the position of a block to compare two objects BlockPos
     *
     * @author      Tom Froment
     */
    static private class RetentionPosition {
        int x;
        int y;
        int z;

        public RetentionPosition(BlockPos position) {
            x = position.getX();
            y = position.getY();
            z = position.getZ();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RetentionPosition that = (RetentionPosition) o;
            return x == that.x && y == that.y && z == that.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
