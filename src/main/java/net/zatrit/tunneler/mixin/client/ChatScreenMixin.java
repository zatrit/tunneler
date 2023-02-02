package net.zatrit.tunneler.mixin.client;

import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.zatrit.tunneler.interfaces.NextScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ChatScreen.class)
@Environment(EnvType.CLIENT)
public class ChatScreenMixin implements NextScreen {
    private @Setter Function<Screen, Screen> nextScreen;

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void setScreenRedirect(MinecraftClient instance,
                                   Screen screen) {
        if (this.nextScreen != null) {
            screen = this.nextScreen.apply(screen);
        }
        instance.setScreen(screen);
    }
}
