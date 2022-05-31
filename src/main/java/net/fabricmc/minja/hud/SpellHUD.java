package net.fabricmc.minja.hud;


import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.textures.SpellHUDTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;

public class SpellHUD {

    private MinecraftClient minecraft;
    private WorldRenderer worldRenderer;
    private TextRenderer fontRenderer;

    private ItemRenderer itemRenderer;
    private PlayerMinja player;

    private static PointCartesien centre;
    private Vec3d cameraPosition;

    private int currentIndex;

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
        centre = new PointCartesien(minecraft.mouse.getX(),minecraft.mouse.getY());

        visible = false;
        isCenterSet = false;
    }

    public void updateScreenDimension(int width, int height) {
        this.width = width/2;
        this.height = height/2;
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

        Vec3d vec = minecraft.cameraEntity.getRotationVecClient();
        minecraft.player.sendMessage(new LiteralText(""+vec.x+" : "+vec.y+" : "+vec.z),true);

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

            minecraft.cameraEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET,cameraPosition);

            // Create a new point at the origin and get its info
            PointPolaire pp = new PointPolaire(LINE,  i * rad);
            PointCartesien p = pp.PolarToCard();

            // In case we are in the safe range :
            if(!isInSafeRange) {

                // Check if the current spell drawn is the active one
                if(i == currentIndex)   drawOK(stack, width+p.x, height +p.y);
                else                    drawKO(stack, width+p.x, height +p.y);


                // In case we are in the selection zone :
            } else {

                // If the mouse is in the range of the current point :
                if(isInInterval(pp,i,rad,thetaModulo)) {
                    // In case we have changed of interval, trigger a sound and update status
                    if(currentIndex != i) {
                        currentIndex = i;
                        minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK,1.0F, 1.0F);
                    }

                    drawOK(stack, width+p.x, height +p.y);
                }
                else
                    drawKO(stack, width+p.x, height +p.y);

            }
        }

        minecraft.cameraEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES,cameraPosition);

    }

    private void sendMessage(String message) {
        sendMessage(message,false);
    }

    private void sendMessage(String message, boolean actionBar) {
        minecraft.player.sendMessage(new LiteralText(message),actionBar);
    }

    private void onClosed() {
        isCenterSet = false;
        player.setActiveSpell(currentIndex);
    }

    private void onOpened() {
        currentIndex = player.getActiveSpellIndex();
        this.updateCenter();
        isCenterSet = true;

        calculateCameraPosition();
    }

    private void calculateCameraPosition() {



        Vec3d A =  minecraft.crosshairTarget.getPos();
        Vec3d O = minecraft.player.getPos().add(0,1.620,0);

        Vec3d v = A.subtract(O);

        cameraPosition = A.add(v.multiply(1000));

        sendMessage("A : ("+A.x+", "+A.y+", "+A.z+")");
        sendMessage("O : ("+O.x+", "+O.y+", "+O.z+")");
        sendMessage("B : ("+cameraPosition.x+", "+cameraPosition.y+", "+cameraPosition.z+")");

    }

    private void drawOK(MatrixStack stack, double x, double y) {
        Renderer.draw(stack, SpellHUDTexture.VALIDATE_CIRCLE, x, y);
        Renderer.draw(stack, player.getActiveSpell(), x, y);
    }

    private void drawKO(MatrixStack stack, double x, double y) {
        Renderer.draw(stack, player.getActiveSpell(), x, y);
    }

    private double modulo(double r, double mod) {
        double result = r % (mod);
        if (result<0) result += mod;
        return result;
    }

    private boolean isInInterval(PointPolaire vertex, int vertexIndex, double angleMoyen, double angleDeLaSouris) {
        // Calculate the range of selection of the iterating spell
        double borneInf = modulo(vertex.theta - angleMoyen / 2, 2*Math.PI);
        double borneSup = modulo(vertex.theta + angleMoyen / 2, 2*Math.PI);

        // Check if the mouse is in the range of the iterating spell
        return vertexIndex != 0     ? borneInf < angleDeLaSouris && angleDeLaSouris < borneSup                                                   // Classic case
                : (borneInf < angleDeLaSouris && angleDeLaSouris < 2*Math.PI) || 0 < angleDeLaSouris && angleDeLaSouris < borneSup; //  Case 0 : borneSup < borneInf
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