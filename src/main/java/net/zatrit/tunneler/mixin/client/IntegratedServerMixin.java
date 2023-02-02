package net.zatrit.tunneler.mixin.client;

import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.GameMode;
import net.zatrit.tunneler.client.TunnelerClient;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import net.zatrit.tunneler.interfaces.TunnelMinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin implements TunnelMinecraftServer {
    @Shadow
    @Final
    private MinecraftClient client;
    @Setter
    @Getter
    private boolean tunnelEnabled;

    @Inject(method = "openToLan", at = @At(value = "RETURN", ordinal = 0))
    private void openToLan(GameMode gameMode,
                           boolean cheatsAllowed,
                           int port,
                           CallbackInfoReturnable<Boolean> cir) {
        if (tunnelEnabled) {
            var source = this.client
                    .getNetworkHandler()
                    .getCommandSource();

            TunnelerClient
                    .getTunneler()
                    .openForServer((FeedbackReceiver) source,
                            MinecraftServer.class.cast(this));
        }
    }
}
