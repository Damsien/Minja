package net.fabricmc.minja.textures;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Representation of a texture
 *
 * @author Tom Froment
 */
public abstract class Texture {

    private Identifier id;
    private int width;
    private int height;

    private int u;
    private int v;

    public Texture(Identifier id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public Texture(Identifier id, int width, int height, int u, int v) {
        this(id,width,height);
        this.u = u;
        this.v = v;
    }

    /**
     * Get the ID of the texture
     *
     * @return ID
     */
    public Identifier getIdentifier() {
        return id;
    }

    /**
     * Get the width of the texture
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the texture
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the left position in the texture image
     *
     * @return left padding
     */
    public int getU() { return u; }

    /**
     * Get the top position in the texture image
     *
     * @return top padding
     */
    public int getV() { return v; }
}
