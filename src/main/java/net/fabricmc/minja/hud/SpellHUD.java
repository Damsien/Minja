package net.fabricmc.minja.hud;


import net.fabricmc.minja.Minja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;


import java.awt.*;
import java.util.ArrayList;

public class SpellHUD {

    private MinecraftClient minecraft;
    private WorldRenderer worldRenderer;
    private TextRenderer fontRenderer;

    private ArrayList<Spell> spells;

    private static boolean visible;

    private int width;
    private int height;
    public SpellHUD() {
        spells = new ArrayList<>();
        spells.add(null);
        spells.add(null);
        spells.add(null);
        spells.add(null);
        spells.add(null);
        spells.add(null);
        spells.add(null);
        spells.add(null);


        minecraft = MinecraftClient.getInstance();
        worldRenderer = minecraft.worldRenderer;
        fontRenderer = minecraft.textRenderer;

        visible = false;
        initialize();
    }

    public void initialize() {
        Window mainWindow = MinecraftClient.getInstance().getWindow();
        width = mainWindow.getWidth() / 4;
        height = mainWindow.getHeight() / 4;
    }

    public static void setVisible(boolean visible) {
        SpellHUD.visible = visible;
    }

    public void onRenderGameOverlayPost(MatrixStack stack, float partialTicks) {

        if(!visible) return;

        double length = spells.size();

        double H = (height * 0.8);
        double angle = 360 / length;

        for(int i=0 ; i < length ; i++) {

            Point p = new Point(H,  i * angle);
            fontRenderer.draw(stack, ""+(i), (float)(width + Math.round(p.x)), (float)(height + Math.round(p.y)), Color.GREEN.getRGB());

        }

    }

    public void setVisibility(boolean b) {
        this.visible = b;
    }

    public void afterRenderStatusEffects(MatrixStack stack, float partialTicks) {
    }


    class Point {

        private double x;
        private double y;

        Point(double r, double angle) {
            double theta = (angle / 180) * Math.PI;
            this.x = r * Math.cos(theta);
            this.y = r * Math.sin(theta);
        }

    }


}