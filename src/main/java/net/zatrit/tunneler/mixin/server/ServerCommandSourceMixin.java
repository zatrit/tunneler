package net.zatrit.tunneler.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Реализация {@link FeedbackReceiver} для источника команд сервера
 */
@Environment(EnvType.SERVER)
@Mixin(value = ServerCommandSource.class)
public abstract class ServerCommandSourceMixin implements FeedbackReceiver {
    @Shadow(prefix = "shadow_")
    public abstract void shadow_sendError(Text text);

    @Shadow
    public abstract void sendFeedback(Text text,
                                      boolean broadcastToOps);

    @Override
    public void sendFeedback(Text text) {
        this.sendFeedback(text, false);
    }

    @Override
    public void sendError(Text text) {
        this.shadow_sendError(text);
    }
}
