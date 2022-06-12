package net.fabricmc.minja.mixin;

import net.fabricmc.minja.hud.SpellHUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injection to the class InGameHud to add Spell wheel.
 *
 */
@Mixin(InGameHud.class)
public abstract class SpellHudMixin {

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Shadow private MinecraftClient client;

    private static SpellHUD gui;

    /**
     * Injection of the Spell HUD in the render method.
     *
     * @param stack
     * @param f
     * @param ci
     */
    @Inject(method = "render", at = @At("RETURN"))
    private void beforeRenderDebugScreen(MatrixStack stack, float f, CallbackInfo ci) {
        if (gui==null)  gui=new SpellHUD();
        gui.updateClient(client);
        gui.updateScreenDimension(scaledWidth, scaledHeight);
        gui.onRenderGameOverlayPost(stack, 0);
    }


}
