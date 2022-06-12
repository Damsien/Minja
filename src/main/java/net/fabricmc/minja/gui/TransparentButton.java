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

public class TransparentButton extends WButton {

    @Nullable
    private Icon icon = null;
    @Nullable
    private Text label;

    @Nullable
    private Runnable onHover = null;

    @Nullable
    private Runnable onHoverLost = null;

    private boolean recentlyHovered = false;

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
     * Gets the hover handler of this button.
     *
     * @return the hover handler
     * @since 2.2.0
     */
    @Nullable
    public Runnable getOnHover() {
        return onHover;
    }

    /**
     * Sets the hover handler of this button.
     *
     * @param onHover the new hover handler
     * @return this button
     */
    public WButton setOnHover(@Nullable Runnable onHover) {
        this.onHover = onHover;
        return this;
    }

    /**
     * Gets the click handler of this button.
     *
     * @return the hover lost handler
     * @since 2.2.0
     */
    @Nullable
    public Runnable getOnHoverLost() {
        return onHoverLost;
    }

    /**
     * Sets the hover lost handler of this button.
     *
     * @param onHoverLost the new hover lost handler
     * @return this button
     */
    public WButton setOnHoverLost(@Nullable Runnable onHoverLost) {
        this.onHoverLost = onHoverLost;
        return this;
    }

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

        Identifier texture = new Identifier("gui:textures/void.png"); //new Identifier("libgui", "textures/widget/dark_widgets.png");
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

    public TransparentButton setOnClick(@Nullable Runnable onClick) {
        return (TransparentButton) super.setOnClick(onClick);
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public boolean isClicked() {
        return this.clicked;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        clicked = !clicked;
        return super.onClick(x, y, button);
    }

    @Override
    public TransparentButton setLabel(Text label) {
        this.label = label;
        return this;
    }

    @Override
    public TransparentButton setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }
}
