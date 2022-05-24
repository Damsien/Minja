package net.fabricmc.minja.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.minja.PlayerMinja;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {

    @Shadow private int ticks;
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    private final MinecraftClient client;
    private static final Identifier MANA_ICON = new Identifier("hud:textures/manabar.png");

    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void renderStatusBarsMixin(MatrixStack matrices, CallbackInfo info) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            int manaMax = ((PlayerMinja) playerEntity).getManaMax();
            LivingEntity livingEntity = this.getRiddenEntity();
            int variable_one;
            int variable_two;
            int height = this.scaledHeight - 49;
            int width = this.scaledWidth / 2 + 91;
            if (this.getHeartCount(livingEntity) == 0) {
                for (variable_one = 0; variable_one < 10; ++variable_one) {
                    int uppderCoord = 9;
                    /**if (ConfigInit.CONFIG.other_droplet_texture) {
                     uppderCoord = uppderCoord + 9;
                     }*/
                    int beneathCoord = 0;

                    variable_two = width - variable_one * 8 - 9;
                    RenderSystem.setShaderTexture(0, MANA_ICON);
                    this.drawTexture(matrices, variable_two, height, 0, 0, 9, 9);
                    if (variable_one * 2 + 1 < manaMax) {
                        this.drawTexture(matrices, variable_two, height, beneathCoord, uppderCoord, 9, 9); // Big icon
                    }
                    if (variable_one * 2 + 1 == manaMax) {
                        this.drawTexture(matrices, variable_two, height, beneathCoord + 9, uppderCoord, 9, 9); // Small icon
                    }
                }
                RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
            }
        }
    }

    @Inject(method = "getHeartRows", at = @At(value = "HEAD"), cancellable = true)
    private void getHeartRowsMixin(int heartCount, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue((int) Math.ceil((double) heartCount / 10.0D) + 1);
    }

    @Shadow
    private PlayerEntity getCameraPlayer() {
        return null;
    }

    @Shadow
    private LivingEntity getRiddenEntity() {
        return null;
    }

    @Shadow
    private int getHeartCount(LivingEntity entity) {
        return 0;
    }


}
