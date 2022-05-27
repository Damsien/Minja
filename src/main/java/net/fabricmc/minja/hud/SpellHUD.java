package net.fabricmc.minja.hud;


import net.fabricmc.minja.Minja;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;


import java.awt.*;
import java.util.ArrayList;

public class SpellHUD {

    private MinecraftClient minecraft;
    private WorldRenderer worldRenderer;
    private TextRenderer fontRenderer;
    private PlayerMinja player;

    private static PointCartesien centre;

    private int currentIndex;

    private static boolean visible;
    private boolean isCenterSet;

    private int width;
    private int height;
    public SpellHUD() {

        minecraft = MinecraftClient.getInstance();
        worldRenderer = minecraft.worldRenderer;
        fontRenderer = minecraft.textRenderer;
        player = (PlayerMinja) minecraft.player;
        centre = new PointCartesien(minecraft.mouse.getX(),minecraft.mouse.getY());

        visible = false;
        isCenterSet = false;
        initialize();
    }

    public void initialize() {
        Window mainWindow = MinecraftClient.getInstance().getWindow();
        width = mainWindow.getWidth() / 4;
        height = mainWindow.getHeight() / 4;
    }

    public void updateCenter() {
        centre = new PointCartesien(minecraft.mouse.getX(), minecraft.mouse.getY());
    }

    public static void setVisible(boolean visible) {
        SpellHUD.visible = visible;
    }

    public void onRenderGameOverlayPost(MatrixStack stack, float partialTicks) {

        final double LINE = (height * 0.7); // length between the center and each spells
        final double SAFE_RANGE = (height * 0.3); // length between the center and the crosshair in which no selection will be done

        // Do not display the HUD if it has not been set to visible
        if(!visible) {
            // If the center has previously been set, it means that the hud has just been closed. Reset data!
            if(isCenterSet) { isCenterSet = false; player.setActiveSpell(currentIndex);}
            return;
        }

        // Initialize the relative center
        if(!isCenterSet) {
            currentIndex = player.getActiveSpellIndex();
            this.updateCenter();
            isCenterSet = true;
        }


        // Get the position of the mouse
        PointCartesien mousePos = new PointCartesien(minecraft.mouse.getX(), minecraft.mouse.getY());

        // Calculate the difference between the relative center (centre) and the actual position of the mouse
        PointCartesien delta = new PointCartesien(mousePos.x - centre.x, mousePos.y - centre.y);


        // Get the origin of the window
        PointCartesien crosshair = new PointCartesien(0,0);

        // Create a point with the same difference from the origin
        PointCartesien Mc = new PointCartesien(crosshair.x + delta.x, crosshair.y + delta.y);

        /** DRAWINGS FOR DEBUG ** /
        fontRenderer.draw(stack, "C", (float) (centre.x), (float) (centre.y), Color.RED.getRGB());
        fontRenderer.draw(stack, "M", (float) (mousePos.x), (float) (mousePos.y), Color.RED.getRGB());

        fontRenderer.draw(stack, "C'", (float) (crosshair.x), (float) (crosshair.y), Color.ORANGE.getRGB());
        fontRenderer.draw(stack, "M'", (float) (Mc.x), (float) (Mc.y), Color.ORANGE.getRGB());

        minecraft.player.sendMessage(new LiteralText("Angle : " + M.theta),true);
         **/

        // Get polar info for our new points
        PointPolaire M = Mc.CardToPolar();
        double thetaModulo = modulo(M.theta, 2*Math.PI);


        // Get number of spells available for the users
        double length = player.getSpells().size();

        // Get the angle in radius of each wheel's node
        double rad = Math.toRadians(360 / length);

        // Check if the mouse is in the safe range
        boolean isInSafeRange = M.r > SAFE_RANGE;

        // Draw the wheel
        for(int i=0 ; i < length ; i++) {

            // Create a new point at the origin and get its info
            PointPolaire pp = new PointPolaire(LINE,  i * rad);
            PointCartesien p = pp.PolarToCard();

            // In case we are in the safe range :
            if(!isInSafeRange) {

                // Check if the current spell drawn is the active one
                if(i == currentIndex)   draw(stack, i, width+p.x, height +p.y, Color.BLUE);
                else                    draw(stack, i, width+p.x, height +p.y, Color.GREEN);


            // In case we are in the selection zone :
            } else {

                // Calculate the range of selection of the iterating spell
                double borneInf = modulo(pp.theta - rad / 2, 2*Math.PI);
                double borneSup = modulo(pp.theta + rad / 2, 2*Math.PI);

                // Check if the mouse is in the range of the iterating spell
                boolean isInInterval = i != 0   ? borneInf < thetaModulo && thetaModulo < borneSup                                                   // Classic case
                                                : (borneInf < thetaModulo && thetaModulo < 2*Math.PI) || 0 < thetaModulo && thetaModulo < borneSup; //  Case 0 : borneSup < borneInf

                // If this is the case :
                if(isInInterval) {
                    // In case we have changed of interval, trigger a sound and update status
                    if(currentIndex != i) {
                        currentIndex = i;
                        minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK,1.0F, 1.0F);
                    }

                    draw(stack, i, width+p.x, height +p.y, Color.BLUE);
                }
                else
                    draw(stack, i, width+p.x, height +p.y, Color.GREEN);

            }
        }

    }

    private double modulo(double r, double mod) {
        double result = r % (mod);
        if (result<0) result += mod;
        return result;
    }

    private void draw(MatrixStack stack, int text, double x, double y, Color color) {
        draw(stack, ""+text, x, y, color);
    }


    private void draw(MatrixStack stack, String text, double x, double y, Color color) {
        fontRenderer.draw(stack, text, (float)x, (float)y, color.getRGB());
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

        public void translater(PointCartesien p) {
            this.x += p.x;
            this.y += p.y;
        }
    }


}