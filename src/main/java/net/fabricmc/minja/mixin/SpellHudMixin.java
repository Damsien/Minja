package net.fabricmc.minja.mixin;

import net.fabricmc.minja.hud.SpellHUD;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)

public abstract class SpellHudMixin {

    private static SpellHUD gui;

    @Inject(method = "renderStatusEffectOverlay", at = @At("RETURN"))
    private void afterRenderStatusEffects(MatrixStack stack, CallbackInfo ci) {
        if (gui == null)
            gui = new SpellHUD();
        gui.afterRenderStatusEffects(stack, 0);
    }

    @Inject(method = "render", at = @At("RETURN"))

    private void beforeRenderDebugScreen(MatrixStack stack, float f, CallbackInfo ci) {
        if (gui==null)
            gui=new SpellHUD();
        gui.onRenderGameOverlayPost(stack, 0);
    }


}
