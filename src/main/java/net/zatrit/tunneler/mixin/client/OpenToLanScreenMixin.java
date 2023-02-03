package net.zatrit.tunneler.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.Text;
import net.zatrit.tunneler.interfaces.TunnelMinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Миксин в класс {@link OpenToLanScreen},
 * добавляющий кнопку для проброса порта через ngrok
 */
@Environment(EnvType.CLIENT)
@Mixin(OpenToLanScreen.class)
public class OpenToLanScreenMixin extends Screen {
    protected OpenToLanScreenMixin(Text title) {
        super(title);
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        var tunnelServer = ((TunnelMinecraftServer) this.client.getServer());
        this.addDrawableChild(CyclingButtonWidget
                .onOffBuilder(tunnelServer.isTunnelEnabled())
                .build(this.width / 2 - 155,
                        this.height - 52,
                        150,
                        20,
                        Text.translatable("text.tunneler.create_tunnel"),
                        (button, enableTunnel) -> {
                            tunnelServer.setTunnelEnabled(enableTunnel);
                        }));
    }
}
