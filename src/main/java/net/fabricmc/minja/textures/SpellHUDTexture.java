package net.fabricmc.minja.textures;

import net.minecraft.util.Identifier;

/**
 * Texture useful for the Spell wheel HUD
 *
 */
public class SpellHUDTexture {

    public static final Texture VALIDATE_CIRCLE = new Validate();


    private static class Validate extends Texture {

        public Validate() {
            super(new Identifier("hud:textures/spellhud.png"), 27, 27);
        }

    }

}
