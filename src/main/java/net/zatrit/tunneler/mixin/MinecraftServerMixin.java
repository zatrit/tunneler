package net.zatrit.tunneler.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.zatrit.tunneler.client.TunnelerClient;
import net.zatrit.tunneler.server.TunnelerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Environment(EnvType.CLIENT)
    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdownClient(CallbackInfo ci) {
        TunnelerClient.shutdown();
    }

    @Environment(EnvType.SERVER)
    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdownServer(CallbackInfo ci) {
        TunnelerServer.shutdown();
    }
}
