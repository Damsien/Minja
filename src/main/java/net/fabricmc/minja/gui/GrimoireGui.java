package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.objects.Grimoire;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GrimoireGui extends LightweightGuiDescription {

    public int cpt = 0;
    private static BackgroundPainter BACKGROUND = BackgroundPainter.createNinePatch(
            new Identifier("gui:textures/leftpagegrimoireopen.png")
    );
    private Map<Integer, String> pages;

    private List<WWidget> displayedWidgets;

    private Integer currentPage;

    private WGridPanel root = new WGridPanel();

    public GrimoireGui(PlayerEntity player) {
        pages = new HashMap<>();
        currentPage = 1;
        displayedWidgets = new ArrayList<>();
        MatrixStack matrixStack = new MatrixStack();
        setRootPanel(root);
        root.setSize(126, 180); // 7 & 10 for 18x18 gridpannel
        // root.setLocation(145,179);
        root.setInsets(Insets.ROOT_PANEL);
        BACKGROUND.paintBackground(matrixStack, 0,0, root);

        /* PAGE 1 */
        pages.put(1, "page1");

        /* PAGE 2 */
        pages.put(2, "page2");

        //WLabel mana = new WLabel(new LiteralText("Current mana : " + ((PlayerMinja) player).getMana() + "/" + ((PlayerMinja) player).getManaMax()), 0x000000);
        //pages.get(2).add(new Component(mana, 0, 5, 2, 1));

        //WSprite icon = new WSprite(new Identifier("minecraft:textures/item/redstone.png"));
        //root.add(icon, 10, 10, 18, 18);
        /*
        WButton button = new WButton(new TextureIcon(new Identifier("gui:textures/rightarrow.png")), Text.of("Button"));
        root.add(button, 0, 3, 3, 1);
        button = button.setOnClick(new Test());

        WLabel mana = new WLabel(new LiteralText("Current mana : " + ((PlayerMinja) player).getMana() + "/" + ((PlayerMinja) player).getManaMax()), 0x000000);
        root.add(mana, 0, 5, 2, 1);

        WLabel spells = new WLabel(new LiteralText(((PlayerMinja) player).getSpells().toString()));
        root.add(spells, 0, 7, 2, 1);
        */
        display();
    }

    @Override
    public void addPainters() {
        if (this.rootPanel!=null && !fullscreen) {
            this.rootPanel.setBackgroundPainter(BACKGROUND);
        }
    }

    private void display(){
        for (Iterator<Component> it = getComponents(currentPage).iterator(); it.hasNext(); ) {
            Component component = it.next();
            int[] coords = component.getCoords();
            WWidget currentWidget = component.getWidget();
            displayedWidgets.add(currentWidget);
            root.add(currentWidget, coords[0], coords[1], coords[2], coords[3]);
        }
        root.validate(this);
    }

    private void delete(){
        for (Iterator<WWidget> it = displayedWidgets.iterator(); it.hasNext(); ) {
            WWidget widget = it.next();
            root.remove(widget);
        }
        displayedWidgets = new ArrayList<>();
        root.validate(this);
    }

    public void nextPage(){
        delete();
        currentPage = currentPage + 1;
        display();
    }

    public void previousPage(){
        delete();
        currentPage = currentPage - 1;
        display();
    }

    public void update(){
        delete();
        display();
    }

    private List<Component> getComponents(Integer page) {
        try {
            return (List<Component>) GrimoireGui.class.getMethod(pages.get(page)).invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.print("AAAAAAAAAAAAAAAAAAAA" + e.toString());
            return null;
        }
    }

    public List<Component> page1() {
        List<Component> components = new ArrayList<>();
        TransparentButton button = new TransparentButton(new TextureIcon(new Identifier("gui:textures/rightarrow.png")), null);
        button = button.setOnClick(new NextPage());
        components.add(new Component(button, 7, 10, 1, 1));

        return components;
    }

    public List<Component> page2() {
        List<Component> components = new ArrayList<>();
        TransparentButton button = new TransparentButton(new TextureIcon(new Identifier("gui:textures/leftarrow.png")), null);
        button = button.setOnClick(new PreviousPage());
        components.add(new Component(button, 0, 10, 1, 1));

        WLabel mana = new WLabel(new LiteralText("Nb clic btn : " + cpt));
        components.add(new Component(mana, 0, 5, 3, 1));

        TransparentButton button2 = new TransparentButton(new TextureIcon(new Identifier("gui:textures/leftarrow.png")), null);
        button2 = button2.setOnClick(new IncCpt());
        components.add(new Component(button2, 2, 2, 1, 1));

        return components;
    }

    class Component {
        private int[] coords;
        private WWidget widget;

        public Component(WWidget widget, int x, int y, int w, int h){
            this.coords = new int[4];
            this.coords[0] = x;
            this.coords[1] = y;
            this.coords[2] = w;
            this.coords[3] = h;
            this.widget = widget;
        }

        public int[] getCoords(){
            return this.coords;
        }

        public WWidget getWidget(){
            return this.widget;
        }
    }

    class TransparentButton extends WButton {

        @Nullable
        private Icon icon = null;
        @Nullable
        private Text label;

        public TransparentButton(Icon icon, Text label) {
            super();
            this.icon = icon;
            this.label = label;
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
    }

    class NextPage implements Runnable {

        @Override
        public void run() {
            nextPage();
        }
    }

    class PreviousPage implements Runnable {

        @Override
        public void run() {
            previousPage();
        }
    }

    class IncCpt implements Runnable {

        @Override
        public void run() {
            cpt = cpt + 1;
            update();
        }
    }

}

