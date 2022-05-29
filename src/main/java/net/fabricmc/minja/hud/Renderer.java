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

    public static void draw(MatrixStack stack, Identifier texture, double x, double y, int width, int height) {
        RenderSystem.setShaderTexture(0, texture);
        new Renderer().drawTexture(stack, (int)x, (int)y, 0, 0, width, height);
    }

    public static void draw(MatrixStack stack, Spell spell, double x, double y) {
        Texture icon = spell.getIcon();
        draw(stack, icon.getIdentifier(),x,y,icon.getWidth(),icon.getHeight());
    }

}
