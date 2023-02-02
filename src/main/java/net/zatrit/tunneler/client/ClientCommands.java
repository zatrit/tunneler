package net.zatrit.tunneler.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.SuperBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.zatrit.tunneler.AbstractCommands;
import net.zatrit.tunneler.TunnelerConfig;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import net.zatrit.tunneler.interfaces.NextScreen;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.zatrit.tunneler.ChatUtils.error;

@SuperBuilder
@SuppressWarnings("CodeBlock2Expr")
@Environment(EnvType.CLIENT)
public class ClientCommands extends AbstractCommands implements ClientCommandRegistrationCallback {
    /**
     * Если есть открытый туннель, копирует его IP адрес в буфер обмена.
     * Применение: /tunnel copyip
     */
    private int tunnelCopyIp(@NotNull CommandContext<FabricClientCommandSource> context) {
        var source = context.getSource();
        this.getTunneler().getTunnelWrapper().map(tunnel -> {
            final var client = source.getClient();
            client.keyboard.setClipboard(tunnel.getShortIp());
            source.sendFeedback(Text.translatable("text.tunneler.copied"));
        }, error((FeedbackReceiver) source));
        return 0;
    }

    /**
     * Создаёт туннель для локального или удалённого сервера.
     * Применение: /tunnel open
     */
    private int tunnelOpen(@NotNull CommandContext<FabricClientCommandSource> context) {
        final var source = context.getSource();
        final var server = source.getClient().getServer();
        final var serverInfo = source.getClient().getCurrentServerEntry();
        String ip = null;
        if (server != null) {
            if (server.getServerPort() != -1) {
                ip = this.getTunneler().getServerIp(server);
            } else {
                source.sendError(Text.translatable(
                        "text.tunneler.not_opened"));
            }
        } else if (serverInfo != null) {
            ip = serverInfo.address;
        }
        if (ip != null) {
            getTunneler().openOnIp((FeedbackReceiver) source, ip);
        }
        return 0;
    }

    /**
     * Открывает экран настроек.
     * Применение: /tunnel options
     */
    private int tunnelOptions(@NotNull CommandContext<FabricClientCommandSource> context) {
        var client = context.getSource().getClient();
        ((NextScreen) client.currentScreen).setNextScreen(parent -> {
            return AutoConfig
                    .getConfigScreen(TunnelerConfig.class, parent)
                    .get();
        });
        return 0;
    }

    @Override
    public void register(@NotNull CommandDispatcher<FabricClientCommandSource> dispatcher,
                         CommandRegistryAccess registryAccess) {
        var command = LiteralArgumentBuilder
                .<FabricClientCommandSource>literal("tunnel")
                .then(LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal("open")
                        .executes(this::tunnelOpen))
                .then(LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal("close")
                        .executes(this::tunnelClose))
                .then(LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal("copyip")
                        .executes(this::tunnelCopyIp))
                .then(LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal("help")
                        .executes(this::tunnelHelp))
                .then(LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal("options")
                        .executes(this::tunnelOptions))
                .then(LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal("token")
                        .then(RequiredArgumentBuilder
                                .<FabricClientCommandSource, String>argument(
                                        "token",
                                        string())
                                .executes(this::tunnelToken))
                        .executes(this::tunnelHelp))
                .executes(this::tunnelHelp);
        dispatcher.register(command);
    }
}
