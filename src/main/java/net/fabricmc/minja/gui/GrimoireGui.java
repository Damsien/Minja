package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GrimoireGui extends LightweightGuiDescription {

    private static BackgroundPainter BACKGROUND = BackgroundPainter.createNinePatch(
            new Identifier("gui:textures/leftpagegrimoireopen.png")
    );

    public GrimoireGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(145, 179);
        // root.setLocation(145,179);
        root.setInsets(Insets.ROOT_PANEL);

        WSprite icon = new WSprite(new Identifier("minecraft:textures/item/redstone.png"));
        root.add(icon, 0, 2, 1, 1);

        WButton button = new WButton(Text.of("Button"));
        button.setIcon(new TextureIcon(new Identifier("gui:textures/rightarrow.png")));
        root.add(button, 0, 3, 4, 1);
        button = button.setOnClick(new Test());

        WLabel label = new WLabel(new LiteralText("Test"), 0xFFFFFF);
        root.add(label, 0, 5, 2, 1);
        root.validate(this);
    }

    @Override
    public void addPainters() {
        if (this.rootPanel!=null && !fullscreen) {
            this.rootPanel.setBackgroundPainter(BACKGROUND);
        }
    }

    class Test implements Runnable {

        @Override
        public void run() {
            System.out.println("Clicked");
        }
    }

}

