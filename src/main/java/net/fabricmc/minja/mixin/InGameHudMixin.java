package net.fabricmc.minja.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.minja.player.PlayerMinja;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Display of the mana bar
 *
 */
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {

    /**
     * Width
     */
    @Shadow private int scaledWidth;

    /**
     * Height
     */
    @Shadow private int scaledHeight;

    /**
     * Texture of a mana droplet
     */
    private static final Identifier MANA_ICON = new Identifier("hud:textures/manabar.png");

    /**
     * Show current mana with a status bar
     * @param MatrixStack matrixStack
     * @param Callback info
     */
    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void renderStatusBarsMixin(MatrixStack matrixStack, CallbackInfo info) {
        // get the current player
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {

            // get the current mana and max mana of the player
            int currentMana = ((PlayerMinja) playerEntity).getMana();
            int manaMax = ((PlayerMinja) playerEntity).getManaMax();

            // test if player is riding an entity
            // LivingEntity livingEntity = this.getRiddenEntity();

            int height = this.scaledHeight - 49;
            int width = this.scaledWidth / 2 + 91;

            int uppderCoord = 9;
            int beneathCoord = 0;

            // draw one mana droplet at a time
            for (int i = 0; i < 10; i++) {
                int j = width - i * 8 - 9;
                // get the texture of the mana
                RenderSystem.setShaderTexture(0, MANA_ICON);

                // draw the texture placeholder (black one)
                this.drawTexture(matrixStack, j, height, 0, 0, 9, 9);

                if (i * 2 + 1 < currentMana * 20 / manaMax) {
                    // draw the big mana icon
                    this.drawTexture(matrixStack, j, height, beneathCoord, uppderCoord, 9, 9);
                }
                if (i * 2 + 1 == currentMana * 20 / manaMax) {
                    // draw the little mana icon
                    this.drawTexture(matrixStack, j, height, beneathCoord + 9, uppderCoord, 9, 9);
                }
            }
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        }
    }

    // Methods needed but not modified so @Shadow
    @Shadow
    private PlayerEntity getCameraPlayer() {
        return null;
    }

}
