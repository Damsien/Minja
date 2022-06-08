package net.fabricmc.minja.hud;


import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.math.CartesianPoint;
import net.fabricmc.minja.math.Operations;
import net.fabricmc.minja.math.PolarPoint;
import net.fabricmc.minja.textures.SpellHUDTexture;
import net.fabricmc.minja.util.Renderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;


/**
 *
 * A spell wheel displayed on the player's screen when he opens his quick spell menu (right-click on a {@link net.fabricmc.minja.objects.Wand Wand}).
 *
 * The wheel has hybrid properties that make its behavior unclear:
 * <ul>
 *  <li>It must be displayed on the player's screen and the game must work properly - the player must always have access to his keyboard (HUD)</li>
 *  <li>The player must interact with it to choose his current spell, preferably by "blocking" his mouse. Its display should be only when desired by the player (GUI)</li>
 * </ul>
 *
 *
 * @author      Tom Froment
 */
public class SpellHUD {

    private MinecraftClient minecraft;
    private WorldRenderer worldRenderer;
    private TextRenderer fontRenderer;

    private ItemRenderer itemRenderer;
    private PlayerMinja player;

    private static CartesianPoint centre;
    private Vec3d cameraPosition;

    private static int currentIndex;

    private static boolean visible;
    private boolean isCenterSet;

    private int width;
    private int height;

    private boolean focus;


    public SpellHUD() {

        minecraft = MinecraftClient.getInstance();
        worldRenderer = minecraft.worldRenderer;
        fontRenderer = minecraft.textRenderer;
        itemRenderer = minecraft.getItemRenderer();
        player = (PlayerMinja) minecraft.player;
        centre = new CartesianPoint(minecraft.mouse.getX(),minecraft.mouse.getY());

        visible = false;
        isCenterSet = false;
    }

    /**
     * Update the scaled dimension received by the InGameHud.
     *
     * Minecraft has a very particular logic for scaling renders, which can make dimensions really hard to calculate. <br>
     * Fortunately, the class that owns this HUD, {@link net.fabricmc.minja.mixin.InGameHudMixin}, possess
     * (via injection) the equivalent of the scaled dimensions. <br>
     * This class has to provide the current dimensions before invoking the {@link #onRenderGameOverlayPost(MatrixStack, float) render method}.
     *
     * @param width scaled width
     * @param height scaled height
     */
    public void updateScreenDimension(int width, int height) {
        this.width = width/2;
        this.height = height/2;
    }

    /**
     * Update the current mouse center.
     *
     * We will use the mouse position (more precisely its angle) to determine the current spell selected by the user. <br><br>
     *
     * However, this position calculation must be done in relation to the origin of the window (top left): <br>
     * when the HUD is "opened" ({@link #setVisible(boolean) visible}), we will retrieve the current position of
     * the mouse, which will serve as a "relative center" to translate future mouse positions to the origin.
     */
    public void updateCenter() {
        centre = new CartesianPoint(minecraft.mouse.getX(), minecraft.mouse.getY());
    }

    /**
     * Make the window visible.
     *
     * @param visible the visibility of the window
     */
    public static void setVisible(boolean visible) {
        SpellHUD.visible = visible;
    }

    /**
     * Get the current index on the wheel selection
     *
     * @return the current index "circled"
     */
    public static int getSelectedIndex() {
        return currentIndex;
    }

    /**
     * Render the HUD in game.
     *
     * @param stack
     * @param partialTicks
     */
    public void onRenderGameOverlayPost(MatrixStack stack, float partialTicks) {

        final double LINE = (height * 0.7); // length between the center and each spells
        final double SAFE_RANGE = (height * 0.3); // length between the center and the crosshair in which no selection will be done

        // Do not display the HUD if it has not been set to visible
        if(!visible) {
            // If the center has previously been set, it means that the hud has just been closed. Reset data!
            if(isCenterSet) onClosed();
            return;
        }

        // Initialize the relative center
        if(!isCenterSet) {
            onOpened();
        }


        minecraft.cameraEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET,cameraPosition);


        // Get the position of the mouse
        CartesianPoint mousePos = new CartesianPoint(minecraft.mouse.getX(), minecraft.mouse.getY());

        // Calculate the difference between the relative center (centre) and the actual position of the mouse
        CartesianPoint delta = new CartesianPoint(mousePos.x() - centre.x(), mousePos.y() - centre.y());


        // Get the origin of the window
        CartesianPoint crosshair = new CartesianPoint(0,0);

        // Create a point with the same difference from the origin
        CartesianPoint Mc = new CartesianPoint(crosshair.x() + delta.x(), crosshair.y() + delta.y());

        /** DRAWINGS FOR DEBUG ** /
         fontRenderer.draw(stack, "C", (float) (centre.x), (float) (centre.y), Color.RED.getRGB());
         fontRenderer.draw(stack, "M", (float) (mousePos.x), (float) (mousePos.y), Color.RED.getRGB());

         fontRenderer.draw(stack, "C'", (float) (crosshair.x), (float) (crosshair.y), Color.ORANGE.getRGB());
         fontRenderer.draw(stack, "M'", (float) (Mc.x), (float) (Mc.y), Color.ORANGE.getRGB());

         minecraft.player.sendMessage(new LiteralText("Angle : " + M.theta),true);
         **/

        // Get polar info for our new points
        PolarPoint M = Mc.CartToPolar();
        double thetaModulo = Operations.modulo(M.theta(), 2*Math.PI);


        // Get number of spells available for the users
        double length = player.getSpells().size();

        // Get the angle in radius of each wheel's node
        double rad = Math.toRadians(360 / length);

        // Check if the mouse is in the safe range
        boolean isInSafeRange = M.r() > SAFE_RANGE;

        // Draw the wheel
        for(int i=0 ; i < length ; i++) {

            // Create a new point at the origin and get its info
            PolarPoint pp = new PolarPoint(LINE,  i * rad);
            CartesianPoint p = pp.PolarToCart();

            // In case we are in the safe range :
            if(!isInSafeRange) {

                // Check if the current spell drawn is the active one
                if(i == currentIndex)   drawOK(stack, width+p.x(), height +p.y(), i);
                else                    drawKO(stack, width+p.x(), height +p.y(), i);


                // In case we are in the selection zone :
            } else {

                // If the mouse is in the range of the current point :
                if(isInInterval(pp,i,rad,thetaModulo)) {
                    // In case we have changed of interval, trigger a sound and update status
                    if(currentIndex != i) {
                        currentIndex = i;
                        minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK,1.0F, 1.0F);
                    }

                    drawOK(stack, width+p.x(), height +p.y(), i);
                }
                else
                    drawKO(stack, width+p.x(), height +p.y(), i);

            }
        }

        minecraft.cameraEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES,cameraPosition);

    }

    /**
     * Called when the HUD is closed.
     *
     * In fact, this method is called a tick after its closure.
     */
    private void onClosed() {
        isCenterSet = false;
        player.setActiveSpell(currentIndex);
    }

    /**
     * Called when the HUD is opened the first time.
     *
     */
    private void onOpened() {
        currentIndex = player.getActiveSpellIndex();
        this.updateCenter();
        isCenterSet = true;

        calculateCameraPosition();
    }

    /** TO_REPLACE */
    private void calculateCameraPosition() {

        Vec3d A =  minecraft.crosshairTarget.getPos();
        Vec3d O = minecraft.player.getPos().add(0,1.620,0);

        Vec3d v = A.subtract(O);

        cameraPosition = A.add(v.multiply(1000));

    }

    /**
     * Draw the render in the matrix if the spell is selected
     * @param stack matrix drawn
     * @param x X coordinate
     * @param y Y coordinate
     * @param i index of the spell
     */
    private void drawOK(MatrixStack stack, double x, double y, int i) {
        Renderer.draw(stack, SpellHUDTexture.VALIDATE_CIRCLE, x, y);
        Renderer.draw(stack, player.getSpell(i), x, y);
    }

    /**
     * Draw the render in the matrix if the spell is not selected
     * @param stack matrix drawn
     * @param x X coordinate
     * @param y Y coordinate
     * @param i index of the spell
     */
    private void drawKO(MatrixStack stack, double x, double y, int i) {
        Renderer.draw(stack, player.getSpell(i), x, y);
    }

    /**
     * Check if the mouse is in the interval for a specific point.
     *
     * @param vertex position of the current spell
     * @param vertexIndex the index of the current spell
     * @param mediumAngle the angle between two nodes
     * @param mouseAngle the angle of the mouse in the Polar plan
     *
     */
    private boolean isInInterval(PolarPoint vertex, int vertexIndex, double mediumAngle, double mouseAngle) {
        // Calculate the range of selection of the iterating spell
        double borneInf = Operations.modulo(vertex.theta() - mediumAngle / 2, 2*Math.PI);
        double borneSup = Operations.modulo(vertex.theta() + mediumAngle / 2, 2*Math.PI);

        // Check if the mouse is in the range of the iterating spell
        return vertexIndex != 0     ? borneInf < mouseAngle && mouseAngle < borneSup                                                   // Classic case
                : (borneInf < mouseAngle && mouseAngle < 2*Math.PI) || 0 < mouseAngle && mouseAngle < borneSup; //  Case 0 : borneSup < borneInf
    }



}