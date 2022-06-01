package net.fabricmc.minja.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.minja.spells.Spell;
import net.fabricmc.minja.textures.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.awt.*;

public class Renderer extends DrawableHelper {

    private static MinecraftClient minecraft = MinecraftClient.getInstance();
    private static TextRenderer fontRenderer = minecraft.textRenderer;
    private static ItemRenderer itemRenderer = minecraft.getItemRenderer();

    public static void draw(MatrixStack stack, int text, double x, double y, Color color) {
        Renderer.draw(stack, ""+text, x, y, color);
    }

    public static void draw(MatrixStack stack, String text, double x, double y, Color color) {
        fontRenderer.draw(stack, text, (float)x, (float)y, color.getRGB());
    }

    public static void draw(Item item, double x, double y) {
        itemRenderer.renderGuiItemIcon(item.getDefaultStack(), (int)x, (int)y);
    }

    public static void draw(MatrixStack stack, Identifier texture, double x, double y, int width, int height, int u, int v) {
        RenderSystem.setShaderTexture(0, texture);
        new Renderer().drawTexture(stack, (int)x, (int)y, u, v, width, height);
    }

    public static void draw(MatrixStack stack, Texture texture, double x, double y) {
        RenderSystem.setShaderTexture(0, texture.getIdentifier());
        int w = texture.getWidth();
        int h = texture.getHeight();
        int u = texture.getU();
        int v = texture.getV();
        new Renderer().drawTexture(stack, (int)(x-w/2), (int)(y-h/2), u, v, w, h);
    }

    public static void draw(MatrixStack stack, Spell spell, double x, double y) {
        draw(stack, spell.getIcon(),x,y);
    }

}
