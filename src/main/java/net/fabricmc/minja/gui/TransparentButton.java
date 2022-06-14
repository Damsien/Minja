package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * TransparentButton is a WButton without any background (basic background of Minecraft buttons)
 * TransparentButton also defines a onHover/onHoverLost method because the coloration of the background isn't applied
 *
 */
public class TransparentButton extends WButton {

    /**
     * Icon is the skin of the button as the background is transparent
     */
    @Nullable
    private Icon icon = null;

    /**
     * A text can be displayed at the right of the icon
     */
    @Nullable
    private Text label;

    /**
     * Runnable executed when the button is hovered
     */
    @Nullable
    private Runnable onHover = null;

    /**
     * Runnable executed when the button was hovered
     */
    @Nullable
    private Runnable onHoverLost = null;

    /**
     * True when hovered put to false when onHoverLost has been executed
     */
    private boolean recentlyHovered = false;

    /**
     * True when the button is clicked once, false if twice (This boolean can be set with setClicked)
     */
    private boolean clicked = false;

    public TransparentButton(Icon icon, Text label) {
        super();
        this.icon = icon;
        this.label = label;
    }

    public TransparentButton() {
        super();
        this.icon = null;
        this.label = null;
    }

    /**
     * Method called by the game on each tick
     */
    @Environment(EnvType.CLIENT)
    @Override
    public void tick() {
        if(isHovered()) { // onHover
            recentlyHovered = true;
            if(onHover != null) {onHover.run();}
        } else {
            if(recentlyHovered) { // onHoverLost
                recentlyHovered = false;
                if(onHoverLost != null) {onHoverLost.run();}
            }
        }
    }

    /**
     * Get the hover runnable
     *
     * @return the hover runnable
     */
    @Nullable
    public Runnable getOnHover() {
        return onHover;
    }

    /**
     * Set the hover runnable
     *
     * @param onHover the new hover runnable
     * @return this button with the new hover runnable
     */
    public WButton setOnHover(@Nullable Runnable onHover) {
        this.onHover = onHover;
        return this;
    }

    /**
     * Get the hover lost runnable
     *
     * @return the hover lost runnable
     */
    @Nullable
    public Runnable getOnHoverLost() {
        return onHoverLost;
    }

    /**
     * Set the hover lost runnable
     *
     * @param onHoverLost the new hover lost runnable
     * @return this button with the new hover lost runnable
     */
    public WButton setOnHoverLost(@Nullable Runnable onHoverLost) {
        this.onHoverLost = onHoverLost;
        return this;
    }

    /**
     * Set the click runnable
     *
     * @param onClick the new click runnable
     * @return this button with the new click runnable
     */
    public TransparentButton setOnClick(@Nullable Runnable onClick) {
        return (TransparentButton) super.setOnClick(onClick);
    }

    /**
     * Set the clicked boolean
     *
     * @param clicked the new value of clicked
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Get the clicked boolean
     *
     * @return the value of clicked
     */
    public boolean isClicked() {
        return this.clicked;
    }

    /**
     * Method called on button click
     * This method also modify the boolean clicked before executing onClick runnable
     *
     * @param x the position of the mouse
     * @param y the coordinates of the event
     * @param button the button pressed of the mouse
     * @return Input result
     */
    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        clicked = !clicked;
        return super.onClick(x, y, button);
    }

    /**
     * Method called by the panel containing this button
     *
     */
    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        boolean hovered = (mouseX>=0 && mouseY>=0 && mouseX<getWidth() && mouseY<getHeight());
        int state = 1; //1=regular. 2=hovered. 0=disabled.
        if (hovered || isFocused()) {
            state = 2;
        }

        float px = 1/256f;
        float buttonLeft = 0 * px;
        float buttonTop = (46 + (state*20)) * px;
        int halfWidth = getWidth()/2;
        if (halfWidth>198) halfWidth=198;
        float buttonWidth = halfWidth*px;
        float buttonHeight = 20*px;

        float buttonEndLeft = (200-(getWidth()/2)) * px;

        Identifier texture = new Identifier("gui:textures/void.png"); // Basic background of a button : new Identifier("libgui", "textures/widget/dark_widgets.png");
        ScreenDrawing.texturedRect(matrices, x, y, getWidth()/2, 20, texture, buttonLeft, buttonTop, buttonLeft+buttonWidth, buttonTop+buttonHeight, 0xFFFAEE);
        ScreenDrawing.texturedRect(matrices, x+(getWidth()/2), y, getWidth()/2, 20, texture, buttonEndLeft, buttonTop, 200*px, buttonTop+buttonHeight, 0xFFFAEE);

        if (icon != null) {
            icon.paint(matrices, x + 1, y + 1, 16);
        }

        if (label != null) {
            int color = 0xE0E0E0;

            int xOffset = (icon != null && alignment == HorizontalAlignment.LEFT) ? 18 : 0;
            ScreenDrawing.drawStringWithShadow(matrices, label.asOrderedText(), alignment, x + xOffset, y + ((20 - 8) / 2), width, color);
        }
    }

    /**
     * Set the label
     *
     * @param label the new value of label
     * @return this button with the new label
     */
    @Override
    public TransparentButton setLabel(Text label) {
        this.label = label;
        return this;
    }

    /**
     * Set the icon
     *
     * @param icon the new value of icon
     * @return this button with the new icon
     */
    @Override
    public TransparentButton setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }
}
