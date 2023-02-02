package net.zatrit.tunneler.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(FabricClientCommandSource.class)
public interface FabricClientCommandSourceMixin extends FeedbackReceiver {
}
