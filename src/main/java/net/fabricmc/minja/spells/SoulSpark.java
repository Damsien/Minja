package net.fabricmc.minja.spells;

import net.fabricmc.minja.math.Vector;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.spells.items.SoulSparkItem;
import net.fabricmc.minja.textures.SoulSparkTexture;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SoulSpark extends SpellProjectile {


    public SoulSpark() {
        super("Soul Spark", 20, new SoulSparkTexture(), "Complex");
    }

    @Override
    public void cast(LivingEntity player) {

        SoulSparkItem.getSpark().use(player.world, (PlayerEntity) player, Hand.MAIN_HAND);

    }

    public void precast(LivingEntity player) {

        final int MAX_LENGTH = 30;
        final int MAX_HEIGHT = 5;

        NetworkEvent.unhightlightAllBlocks();

        Vec3d direction = Vector.collinearWithCameraPosition((PlayerEntity) player);

        Vec3d cameraPosition2 = MinecraftClient.getInstance().crosshairTarget.getPos();

        String vector2 = "Camera B :  (" + cameraPosition2.getX() + ", " + cameraPosition2.getY() + ", " + cameraPosition2.getZ() + ")";


        PlayerEntity p = (PlayerEntity) player;

        //p.sendMessage(new LiteralText(vector2), false);

        int j=0;
        while(j<MAX_LENGTH) {

            Vec3d nextColinearVector = direction.multiply(j);
            BlockPos nextColinearBlock = player.getBlockPos().add(nextColinearVector.x, nextColinearVector.y, nextColinearVector.z);

            String vector = "Vector v (" + direction.getX() + ", " + direction.getY() + ", " + direction.getZ() + ")";

            //.p.sendMessage(new LiteralText(vector), false);

            int i=0;

            while(i<MAX_HEIGHT) {

                // Get current block
                BlockPos iterator_position = nextColinearBlock.down(i);
                BlockState block = p.world.getBlockState(iterator_position);
                BlockEntity entity = p.world.getBlockEntity(iterator_position);

                // Check if there is a solid block
                if(!block.isAir() && block.isFullCube(p.world, iterator_position)) {

                    // Get block on top of the current iteration
                    BlockPos upperBlockPos = iterator_position.up(1);
                    BlockState upperBlockState = p.world.getBlockState(upperBlockPos);

                    // Check if there if it's a free block
                    if(upperBlockState.isAir()) {

                        NetworkEvent.hightlightBlock(iterator_position);

                        break;
                    }

                }
                i++;
            }

            j++;

        }

    }
}
