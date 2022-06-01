package net.fabricmc.minja.textures;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
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
