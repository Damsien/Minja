package net.fabricmc.minja.hud;


import net.fabricmc.minja.Minja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;


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

        PointPolaire M = new PointCartesien(minecraft.mouse.getX(),minecraft.mouse.getY()).CardToPolar();
        double valeurCourante = M.theta % (2*Math.PI);
        if (valeurCourante<0) valeurCourante += 2*Math.PI;


        double length = spells.size();

        double H = (height * 0.7);
        double angle = 360 / length;
        double rad = Math.toRadians(angle);

        System.out.println("Angle (deg) : " + Math.toDegrees(M.theta));
        System.out.println("Angle (rad) : " + M.theta);

        for(int i=0 ; i < length ; i++) {

            PointPolaire pp = new PointPolaire(H,  i * rad);
            System.out.println("Angle du point " + i + " : " + pp.theta);
            PointCartesien p = pp.PolarToCard();

            // Check mouse
            double borneInf = (pp.theta - rad / 2) % (2*Math.PI);
            if (borneInf<0) borneInf += 2*Math.PI;

            double borneSup = (pp.theta + rad / 2) % (2*Math.PI);
            if (borneSup<0) borneSup += 2*Math.PI;


            System.out.println("Borne inférieure de  " + i + " : " + borneInf);
            System.out.println("Borne supérieure de  " + i + " : " + borneSup);
            System.out.println("Valeur courante de  " + "souris" + " : " + valeurCourante);

            boolean isInInterval = i != 0   ? borneInf < valeurCourante && valeurCourante < borneSup
                                            : (borneInf < valeurCourante && valeurCourante < 2*Math.PI) || 0 < valeurCourante && valeurCourante < borneSup;

            if(isInInterval)
                fontRenderer.draw(stack, ""+(i), (float)(width + Math.round(p.x)), (float)(height + Math.round(p.y)), Color.BLUE.getRGB());
            else
                fontRenderer.draw(stack, ""+(i), (float)(width + Math.round(p.x)), (float)(height + Math.round(p.y)), Color.GREEN.getRGB());

        }

    }

    public void setVisibility(boolean b) {
        this.visible = b;
    }

    public static void toggleVisibility() {
        System.out.println("Petit message de test");
        visible = !visible;
    }

    public void afterRenderStatusEffects(MatrixStack stack, float partialTicks) {
    }


    class PointPolaire {

        private double r;
        private double theta;


        PointPolaire(double r, double theta) {
            this.r = r;
            this.theta = theta;
        }

        public PointCartesien PolarToCard() {
            double x = r * Math.cos(theta);
            double y = r * Math.sin(theta);
            return new PointCartesien(x,y);
        }

    }

    class PointCartesien {

        double x;

        double y;

        public PointCartesien(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public PointPolaire CardToPolar() {
            double r     = Math.sqrt(x*x + y*y);
            double theta = Math.atan2(y, x);

            return new PointPolaire(r,theta);
        }
    }


}