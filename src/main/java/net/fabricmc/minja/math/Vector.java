package net.fabricmc.minja.math;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Additional tool to perform operations on vectors (Vec3d)
 *
 * @author      Tom Froment
 */
public class Vector {

    /**
     * Get a vector representing the direction in which the player is looking
     *
     * @param player the player whose information we want to retrieve
     *
     * @return a collinear and normalized vector
     */
    public static Vec3d collinearWithCameraPosition(PlayerEntity player) {


        MinecraftClient minecraft = MinecraftClient.getInstance();

        Vec3d A =  minecraft.crosshairTarget.getPos();
        Vec3d O = minecraft.player.getPos().add(0,1.620,0); // Height of the eyes

        Vec3d v = A.subtract(O);

        return A.add(v).normalize();

    }


}
