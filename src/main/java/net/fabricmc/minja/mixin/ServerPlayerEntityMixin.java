package net.fabricmc.minja.mixin;

import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.player.ServerPlayerMinja;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerMinja {

    private PlayerMinja player;

    public void setPlayer(PlayerMinja player) {
        this.player = player;
    }

    @Inject(method = "onDisconnect", at = @At("RETURN"))
    public void onDisconnect(CallbackInfo ci) {
        player.stopManaRegeneration();
    }

}
