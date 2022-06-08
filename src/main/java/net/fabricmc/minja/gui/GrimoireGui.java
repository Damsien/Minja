package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.minja.player.PlayerMinja;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

public class GrimoireGui extends LightweightGuiDescription {

    private static BackgroundPainter BACKGROUND = BackgroundPainter.createNinePatch(
            new Identifier("gui:textures/leftpagegrimoireopen.png")
    );
    private Map<Integer, List<Component>> pages;

    private Integer currentPage;

    private WGridPanel root = new WGridPanel();

    public GrimoireGui(PlayerEntity player) {
        pages = new HashMap<>();
        currentPage = 1;
        MatrixStack matrixStack = new MatrixStack();
        setRootPanel(root);
        root.setSize(145, 179);
        // root.setLocation(145,179);
        root.setInsets(Insets.ROOT_PANEL);
        BACKGROUND.paintBackground(matrixStack, 0,0, root);

        /* PAGE 1 */
        pages.put(1, new ArrayList<>());

        WButton button = new WButton(new TextureIcon(new Identifier("gui:textures/rightarrow.png")));
        button = button.setOnClick(new Test());
        pages.get(1).add(new Component(button, 0, 3, 3, 1));

        /* PAGE 2 */
        pages.put(2, new ArrayList<>());

        WLabel mana = new WLabel(new LiteralText("Current mana : " + ((PlayerMinja) player).getMana() + "/" + ((PlayerMinja) player).getManaMax()), 0x000000);
        pages.get(2).add(new Component(mana, 0, 5, 2, 1));

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
        for (Iterator<Component> it = pages.get(currentPage).iterator(); it.hasNext(); ) {
            Component component = it.next();
            int[] coords = component.getCoords();
            root.add(component.getWidget(), coords[0], coords[1], coords[2], coords[3]);
        }
        root.validate(this);
    }

    private void delete(){
        for (Iterator<Component> it = pages.get(currentPage).iterator(); it.hasNext(); ) {
            Component component = it.next();
            root.remove(component.widget);
        }
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

    class Test implements Runnable {

        @Override
        public void run() {
            System.out.println("Clicked");
            nextPage();
        }
    }

}

