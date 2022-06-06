package net.fabricmc.minja.math;

import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.exceptions.InvalidContextException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
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
     * @return a colinear vector
     */
    public static Vec3d colinearWithCameraPosition(PlayerEntity player) {

        // Check if we correctly get the context
        //if(MinecraftClient.getInstance() == null) throw new InvalidContextException(Side.SERVER);

        MinecraftClient minecraft = MinecraftClient.getInstance();

        Vec3d A =  minecraft.crosshairTarget.getPos();
        Vec3d O = minecraft.player.getPos().add(0,1.620,0); // Height of the eyes

        Vec3d v = A.subtract(O);

        //String vector = "Vector v (" + v.x + ", " + v.y + ", " + v.z + ")";

        //player.sendMessage(new LiteralText(vector), false);

        return A.add(v).normalize();

    }


}
