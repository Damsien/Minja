package net.fabricmc.minja.textures;

import net.minecraft.util.Identifier;

public class Texture {

    private Identifier id;
    private int width;
    private int height;

    public Texture(Identifier id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public Identifier getIdentifier() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
