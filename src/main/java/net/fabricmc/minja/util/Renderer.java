package net.fabricmc.minja.util;

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

/**
 * Tool for drawing objects of different nature in a HUD
 *
 * @author     Tom Froment
 */
public class Renderer extends DrawableHelper {

    private static MinecraftClient minecraft = MinecraftClient.getInstance();
    private static TextRenderer fontRenderer = minecraft.textRenderer;
    private static ItemRenderer itemRenderer = minecraft.getItemRenderer();

    /**
     * Draw an integer in a matrix.
     *
     * @param stack The current matrix
     * @param text  The integer to write
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param color color of the text
     */
    public static void draw(MatrixStack stack, int text, double x, double y, Color color) {
        Renderer.draw(stack, ""+text, x, y, color);
    }

    /**
     * Draw a text in a matrix.
     *
     * @param stack The current matrix
     * @param text  The text to write
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param color color of the text
     */
    public static void draw(MatrixStack stack, String text, double x, double y, Color color) {
        fontRenderer.draw(stack, text, (float)x, (float)y, color.getRGB());
    }

    /**
     * Draw an Item.
     *
     * @param item  The Item to draw
     * @param x     X coordinate
     * @param y     Y coordinate
     */
    public static void draw(Item item, double x, double y) {
        itemRenderer.renderGuiItemIcon(item.getDefaultStack(), (int)x, (int)y);
    }

    /**
     * Draw a texture in a matrix.
     *
     * @param stack The current matrix
     * @param texture  The ID of the texture
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param u     top coordinate of the image
     * @param v     left coordinate of the image
     */
    public static void draw(MatrixStack stack, Identifier texture, double x, double y, int width, int height, int u, int v) {
        RenderSystem.setShaderTexture(0, texture);
        new Renderer().drawTexture(stack, (int)x, (int)y, u, v, width, height);
    }

    /**
     * Draw a texture in a matrix.
     *
     * @param stack The current matrix
     * @param texture  The texture to draw
     * @param x     X coordinate
     * @param y     Y coordinate
     */
    public static void draw(MatrixStack stack, Texture texture, double x, double y) {
        RenderSystem.setShaderTexture(0, texture.getIdentifier());
        int w = texture.getWidth();
        int h = texture.getHeight();
        int u = texture.getU();
        int v = texture.getV();
        new Renderer().drawTexture(stack, (int)(x-w/2), (int)(y-h/2), u, v, w, h);
    }

    /**
     * Draw a Spell icon in a matrix.
     *
     * @param stack The current matrix
     * @param spell  The spell to drawn
     * @param x     X coordinate
     * @param y     Y coordinate
     */
    public static void draw(MatrixStack stack, Spell spell, double x, double y) {
        draw(stack, spell.getTexture(),x,y);
    }

}
