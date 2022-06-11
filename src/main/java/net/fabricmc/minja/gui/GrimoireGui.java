package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.objects.Grimoire;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.Narration;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
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

    private List<Component> buttonsSpellWheel;

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

    public void changePage(int direction){
        delete();
        currentPage = currentPage + direction;
        display();
    }

    public void update(){
        delete();
        display();
    }

    public void refresh(){
        root.validate(this);
    }

    private void initSpellWheel() {
        buttonsSpellWheel = new ArrayList<>();
        buttonPrioritySpellWheel = new ArrayList<>();
        buttonsSpellWheel.add(0, new Component(new TransparentButton(), 6, 5, 1, 1));
        buttonPrioritySpellWheel.add(0, 0);
        buttonsSpellWheel.add(1, new Component(new TransparentButton(), 6, 7, 1, 1));
        buttonPrioritySpellWheel.add(1, 6);
        buttonsSpellWheel.add(2, new Component(new TransparentButton(), 5, 8, 1, 1));
        buttonPrioritySpellWheel.add(2, 3);
        buttonsSpellWheel.add(3, new Component(new TransparentButton(), 2, 8, 1, 1));
        buttonPrioritySpellWheel.add(3, 5);
        buttonsSpellWheel.add(4, new Component(new TransparentButton(), 1, 7, 1, 1));
        buttonPrioritySpellWheel.add(4, 1);
        buttonsSpellWheel.add(5, new Component(new TransparentButton(), 1, 5, 1, 1));
        buttonPrioritySpellWheel.add(5, 7);
        buttonsSpellWheel.add(6, new Component(new TransparentButton(), 2, 4, 1, 1));
        buttonPrioritySpellWheel.add(6, 2);
        buttonsSpellWheel.add(7, new Component(new TransparentButton(), 5, 4, 1, 1));
        buttonPrioritySpellWheel.add(7, 4);
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

        WLabel title = new WLabel(new LiteralText(""));
        title.setVerticalAlignment(VerticalAlignment.CENTER);
        components.add(new Component(title, 0, 1, 3, 1));

        WLabel text = new WLabel(new LiteralText(""));
        text.setVerticalAlignment(VerticalAlignment.CENTER);
        components.add(new Component(text, 0, 2, 6, 1));

        WLabel manaCost = new WLabel(new LiteralText(""));
        manaCost.setVerticalAlignment(VerticalAlignment.CENTER);
        manaCost.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        components.add(new Component(manaCost, 0, 3, 1, 1));

        WSprite manaIcon = new WSprite(new Identifier("gui:textures/void.png"));
        components.add(new Component(manaIcon, 1, 3, 1, 1));

        WLabel swap = new WLabel(new LiteralText(""));
        swap.setHorizontalAlignment(HorizontalAlignment.CENTER);
        swap.setVerticalAlignment(VerticalAlignment.CENTER);
        components.add(new Component(swap, 1, 9, 6, 1));

        /* SPELL WHEEL */

        int selectedSpell = player.getActiveSpellIndex();
        List<Spell> spells = player.getSpells();
        int nbSpells = spells.size();
        int currentSpell = 0;

        WSprite spellWheel = new WSprite(new Identifier("gui:textures/spellwheel/spellwheel" + nbSpells + ".png"));
        components.add(new Component(spellWheel, 1, 4, 6, 5));


        for(Component currentComponent : getButtonsSpellWheelDisplayed(nbSpells)) {
            TransparentButton currentButton = (TransparentButton) currentComponent.getWidget();
            currentButton.setIcon(new TextureIcon(spells.get(currentSpell).getIcon()));
            currentComponent.setWidget(currentButton.setOnHover(new AddSpellDescription(currentSpell, title, text, manaCost, manaIcon)));
            currentComponent.setWidget(currentButton.setOnHoverLost(new RemoveSpellDescription(title, text, manaCost, manaIcon)));
            currentComponent.setWidget(currentButton.setOnClick(new SwapSpells(nbSpells, selectedSpell, player, swap)));
            components.add(currentComponent);
            currentSpell++;
        }

        TransparentButton button = new TransparentButton(new TextureIcon(new Identifier("gui:textures/leftarrow.png")), null);
        button = button.setOnClick(new PreviousPage());
        components.add(new Component(button, 0, 10, 1, 1));

        return components;
    }

    public List<Component> getButtonsSpellWheelDisplayed(int nbSpells) {
        List<Component> buttonsDisplayed= new ArrayList<>();
        for(int i = 0; i < buttonsSpellWheel.size(); i++) {
            if(buttonPrioritySpellWheel.get(i) < nbSpells) {
                buttonsDisplayed.add(buttonsSpellWheel.get(i));
            }
        }
        return buttonsDisplayed;
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

        public void setWidget(WWidget widget){
            this.widget = widget;
        }
    }

    class TransparentButton extends WButton {

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

    class NextPage implements Runnable {

        @Override
        public void run() {
            changePage(1);
        }
    }

    class PreviousPage implements Runnable {

        @Override
        public void run() {
            changePage(-1);
        }
    }

    class AddSpellDescription implements Runnable {

        private final int spellNumber;

        private final WLabel description;

        private final WLabel spellDescription;

        private final WLabel manaCost;

        private final WSprite manaIcon;

        public AddSpellDescription(int spellNumber, WLabel description, WLabel spellDescription, WLabel manaCost, WSprite manaIcon) {
            this.spellNumber = spellNumber;
            this.description = description;
            this.spellDescription = spellDescription;
            this.manaCost = manaCost;
            this.manaIcon = manaIcon;
        }
        @Override
        public void run() {
            description.setText(Text.of("Description :"));
            spellDescription.setText(Text.of(player.getSpell(spellNumber).quickDescription()));
            manaCost.setText(Text.of(String.valueOf(player.getSpell(spellNumber).getManaCost())));
            manaIcon.setImage(new Identifier("gui:textures/manadroplet.png"));
            refresh();
        }
    }

    class RemoveSpellDescription implements Runnable {

        private final WLabel description;

        private final WLabel spellDescription;

        private final WLabel manaCost;

        private final WSprite manaIcon;

        public RemoveSpellDescription(WLabel description, WLabel spellDescription, WLabel manaCost, WSprite manaIcon) {
            this.description = description;
            this.spellDescription = spellDescription;
            this.manaCost = manaCost;
            this.manaIcon = manaIcon;
        }
        @Override
        public void run() {
            description.setText(Text.of(""));
            spellDescription.setText(Text.of(""));
            manaCost.setText(Text.of(""));
            manaIcon.setImage(new Identifier("gui:textures/void.png"));
            refresh();
        }
    }

    class SwapSpells implements Runnable {

        private int nbSpells;

        private int selectedSpell;

        private PlayerMinja player;

        private final WLabel swap;

        public SwapSpells(int nbSpells, int selectedSpell, PlayerMinja player, WLabel swap) {
            this.nbSpells = nbSpells;
            this.selectedSpell = selectedSpell;
            this.player = player;
            this.swap = swap;
        }

        @Override
        public void run() {
            int firstSwap = -1;
            List<Component> displayedButtons = getButtonsSpellWheelDisplayed(nbSpells);
            for(int i = 0; i < displayedButtons.size(); i++) {
                TransparentButton currentButton = (TransparentButton) displayedButtons.get(i).getWidget();
                if(currentButton.clicked) {
                    if(firstSwap == -1) {
                        firstSwap = i;
                    } else { // Swapping spells
                        player.swapSpells(firstSwap, i);
                        NetworkEvent.swapSpells(firstSwap, i);
                        if(selectedSpell == firstSwap) { // Saving selected spell
                            selectedSpell = i;
                        } else if (selectedSpell == i) {
                            selectedSpell = firstSwap;
                        }
                        player.setActiveSpell(selectedSpell);
                        NetworkEvent.updateSpellIndex(selectedSpell);
                        ((TransparentButton) displayedButtons.get(firstSwap).getWidget()).setClicked(false);
                        ((TransparentButton) displayedButtons.get(i).getWidget()).setClicked(false);
                        firstSwap = -1;
                        update();
                        break;
                    }
                }
            }
            if(firstSwap != -1) {
                swap.setText(Text.of("Swapping : " + player.getSpell(firstSwap).getName()));
            } else {
                swap.setText(Text.of(""));
            }
            refresh();
        }
    }
}

