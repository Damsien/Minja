package net.fabricmc.minja;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.minja.events.NetworkEvent;
import net.fabricmc.minja.spells.LightningBall;
import net.fabricmc.minja.spells.Spark;
import net.fabricmc.minja.spells.entities.EntitySpawnPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class MinjaClient implements ClientModInitializer {
    public static final Identifier PacketID = new Identifier("spells", "spawn_packet");
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Minja.LightningBallEntityType, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(Minja.SparkEntityType, FlyingItemEntityRenderer::new);
        receiveEntityPacket();


        // SERVER DATA

        // SERVER INFORMATION
        ServerPlayNetworking.registerGlobalReceiver(NetworkEvent.UPDATE_SPELL_INDEX, (client, player, handler, buf, sender) -> {
            // Read packet data on the event loop
            int target = buf.readInt();

            client.execute(() -> {
                // Everything in this lambda is run on the render thread
                ((PlayerMinja)player).setActiveSpell(target);

            });
        });

    }

    public void receiveEntityPacket() {
        ClientSidePacketRegistry.INSTANCE.register(PacketID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}
