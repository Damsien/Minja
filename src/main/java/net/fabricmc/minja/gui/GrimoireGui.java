package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GrimoireGui extends LightweightGuiDescription {

    public int cpt = 0;
    private static BackgroundPainter BACKGROUND = BackgroundPainter.createNinePatch(
            new Identifier("gui:textures/grimoiretexture.png")
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

        /* DISPLAYING FIRST PAGE */
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
                if(currentButton.isClicked()) {
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

