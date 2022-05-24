package net.fabricmc.minja.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
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
    private static final Identifier MANA_ICON = new Identifier("minja:hud/textures/manabar.png");

    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }

    @Inject(method = "renderStatusBars", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 1))
    private void renderStatusBarsMixin(MatrixStack matrices, CallbackInfo info) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            LivingEntity livingEntity = this.getRiddenEntity();
            int variable_one;
            int variable_two;
            int variable_three;
            int height = this.scaledHeight - 49;
            int width = this.scaledWidth / 2 + 91;
            if (this.getHeartCount(livingEntity) == 0) {
                for (variable_one = 0; variable_one < 10; ++variable_one) {
                    variable_three = height;
                    /**if (thirstManager.dehydration >= 4.0F && this.ticks % (thirst * 3 + 1) == 0) {
                        variable_three = height + (client.world.random.nextInt(3) - 1); // bouncy
                        thirstManager.dehydration -= 4.0F;
                    } else if (this.ticks % (thirst * 8 + 3) == 0) {
                        variable_three = height + (client.world.random.nextInt(3) - 1); // bouncy
                    }*/
                    int uppderCoord = 9;
                    /**if (ConfigInit.CONFIG.other_droplet_texture) {
                        uppderCoord = uppderCoord + 9;
                    }*/
                    int beneathCoord = 0;
                    // Check for freezing later too
                    // if (playerEntity.hasStatusEffect(EffectInit.DEHYDRATION)) {
                    // beneathCoord = 36;
                    // }
                    /*if (playerEntity.hasStatusEffect(EffectInit.THIRST)) {
                        beneathCoord = 18;
                    }*/
                    variable_two = width - variable_one * 8 - 9;
                    variable_three = variable_three;
                    RenderSystem.setShaderTexture(0, MANA_ICON);
                    this.drawTexture(matrices, variable_two, variable_three, 0, 0, 9, 9);
                    /**if (variable_one * 2 + 1 < thirst) {
                        this.drawTexture(matrices, variable_two, variable_three, beneathCoord, uppderCoord, 9, 9); // Big icon
                    }
                    if (variable_one * 2 + 1 == thirst) {
                        this.drawTexture(matrices, variable_two, variable_three, beneathCoord + 9, uppderCoord, 9, 9); // Small icon
                    }*/
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
