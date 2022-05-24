package net.fabricmc.minja.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {

    private final MinecraftClient client;

    public InGameHudMixin(MinecraftClient client) {
        this.client = client;
    }
}
