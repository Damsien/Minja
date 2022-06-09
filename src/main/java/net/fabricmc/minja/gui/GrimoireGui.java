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
import net.fabricmc.minja.objects.Grimoire;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.spells.Spell;
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

    private PlayerMinja player;

    private Map<Integer, Component> buttonsSpellWheel;

    private List<Integer> buttonPrioritySpellWheel;

    private WGridPanel root = new WGridPanel();

    public GrimoireGui(PlayerEntity player) {
        pages = new HashMap<>();
        this.player = (PlayerMinja) player;
        currentPage = 1;
        displayedWidgets = new ArrayList<>();
        MatrixStack matrixStack = new MatrixStack();
        setRootPanel(root);
        root.setSize(144, 198); // 8 & 11 for 18x18 gridpannel
        // root.setLocation(145,179);
        root.setInsets(Insets.ROOT_PANEL);
        BACKGROUND.paintBackground(matrixStack, 0,0, root);

        /* PAGE 1 */
        pages.put(1, "page1");

        /* PAGE 2 */
        pages.put(2, "page2");
        initSpellWheel();

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

    private void initSpellWheel() {
        buttonsSpellWheel = new HashMap<>();
        buttonPrioritySpellWheel = new ArrayList<>();
        buttonsSpellWheel.put(0, new Component(new TransparentButton(), 6, 5, 1, 1));
        buttonPrioritySpellWheel.add(0);
        buttonsSpellWheel.put(1, new Component(new TransparentButton(), 6, 7, 1, 1));
        buttonPrioritySpellWheel.add(6);
        buttonsSpellWheel.put(2, new Component(new TransparentButton(), 5, 8, 1, 1));
        buttonPrioritySpellWheel.add(3);
        buttonsSpellWheel.put(3, new Component(new TransparentButton(), 2, 8, 1, 1));
        buttonPrioritySpellWheel.add(5);
        buttonsSpellWheel.put(4, new Component(new TransparentButton(), 1, 7, 1, 1));
        buttonPrioritySpellWheel.add(1);
        buttonsSpellWheel.put(5, new Component(new TransparentButton(), 1, 5, 1, 1));
        buttonPrioritySpellWheel.add(7);
        buttonsSpellWheel.put(6, new Component(new TransparentButton(), 2, 4, 1, 1));
        buttonPrioritySpellWheel.add(2);
        buttonsSpellWheel.put(7, new Component(new TransparentButton(), 5, 4, 1, 1));
        buttonPrioritySpellWheel.add(4);
    }

    private List<Component> getComponents(Integer page) {
        try {
            return (List<Component>) GrimoireGui.class.getMethod(pages.get(page)).invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    public List<Component> page1() {
        List<Component> components = new ArrayList<>();

        WLabel pageNumber = new WLabel(new LiteralText("Page : " + currentPage + "/" + pages.size()));
        components.add(new Component(pageNumber, 5, 0, 3, 1));

        WLabel name = new WLabel(new LiteralText("Name : " + ((PlayerEntity) player).getName().getString()));
        components.add(new Component(name, 0, 2, 3, 1));

        WLabel sorcererClass = new WLabel(new LiteralText("Class : " + "Novice"));
        components.add(new Component(sorcererClass, 0, 3, 3, 1));

        WLabel level = new WLabel(new LiteralText("Level : " + "1"));
        components.add(new Component(level, 0, 4, 3, 1));

        WLabel manaMax = new WLabel(new LiteralText("Maximum mana : " + player.getManaMax()));
        components.add(new Component(manaMax, 0, 5, 3, 1));

        TransparentButton button = new TransparentButton(new TextureIcon(new Identifier("gui:textures/rightarrow.png")), null);
        button = button.setOnClick(new NextPage());
        components.add(new Component(button, 7, 10, 1, 1));

        return components;
    }

    public List<Component> page2() {
        List<Component> components = new ArrayList<>();

        WLabel pageNumber = new WLabel(new LiteralText("Page : " + currentPage + "/" + pages.size()));
        components.add(new Component(pageNumber, 5, 0, 3, 1));

        /* SPELL WHEEL */

        int selectedSpell = player.getActiveSpellIndex();
        List<Spell> spells = player.getSpells();
        int nbSpells = spells.size();
        int currentSpell = 0;

        WSprite spellWheel = new WSprite(new Identifier("gui:textures/spellwheel/spellwheel" + nbSpells + ".png"));
        components.add(new Component(spellWheel, 1, 4, 6, 5));


        for(int i = 0; i < buttonsSpellWheel.size(); i++) {
            if(buttonPrioritySpellWheel.get(i) < nbSpells) {
                Component currentButton = buttonsSpellWheel.get(i);
                ((TransparentButton) currentButton.getWidget()).setIcon(new TextureIcon(spells.get(currentSpell).getIcon()));
                components.add(currentButton);
                currentSpell++;
            }
        }

        TransparentButton button = new TransparentButton(new TextureIcon(new Identifier("gui:textures/leftarrow.png")), null);
        button = button.setOnClick(new PreviousPage());
        components.add(new Component(button, 0, 10, 1, 1));

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

        public TransparentButton() {
            super();
            this.icon = null;
            this.label = null;
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

}

