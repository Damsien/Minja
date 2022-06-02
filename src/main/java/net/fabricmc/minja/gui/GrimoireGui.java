package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.minja.PlayerMinja;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GrimoireGui extends LightweightGuiDescription {

    private static BackgroundPainter BACKGROUND = BackgroundPainter.createNinePatch(
            new Identifier("gui:textures/leftpagegrimoireopen.png")
    );

    public GrimoireGui(PlayerEntity player) {
        MatrixStack matrixStack = new MatrixStack();
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(145, 179);
        // root.setLocation(145,179);
        root.setInsets(Insets.ROOT_PANEL);
        BACKGROUND.paintBackground(matrixStack, 0,0, root);

        //WSprite icon = new WSprite(new Identifier("minecraft:textures/item/redstone.png"));
        //root.add(icon, 10, 10, 18, 18);

        WButton button = new WButton(new TextureIcon(new Identifier("gui:textures/rightarrow.png")), Text.of("Button"));
        root.add(button, 0, 3, 3, 1);
        button = button.setOnClick(new Test());

        WLabel mana = new WLabel(new LiteralText("Current mana : " + ((PlayerMinja) player).getMana() + "/" + ((PlayerMinja) player).getManaMax()), 0x000000);
        root.add(mana, 0, 5, 2, 1);

        WLabel spells = new WLabel(new LiteralText(((PlayerMinja) player).getSpells().toString()));
        root.add(spells, 0, 7, 2, 1);

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

