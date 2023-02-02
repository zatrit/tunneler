package net.zatrit.tunneler.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.SuperBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.zatrit.tunneler.AbstractCommands;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.zatrit.tunneler.ChatUtils.error;

@SuperBuilder
@SuppressWarnings("CodeBlock2Expr")
@Environment(EnvType.SERVER)
public class ServerCommands extends AbstractCommands implements CommandRegistrationCallback {
    /**
     * Создаёт туннель для данного сервера. Применение: /tunnel open
     */
    private int tunnelOpen(@NotNull CommandContext<ServerCommandSource> context) {
        final var source = context.getSource();
        final var wrapper = this.getTunneler().getTunnelWrapper();
        if (wrapper.hasTunnel()) {
            source.sendError(Text.translatable(
                    "text.tunneler.already_opened"));
            return 0;
        }
        final var server = source.getServer();
        String ip = null;
        if (server != null) {
            ip = getTunneler().getServerIp(server);
        }
        if (ip != null) {
            this.getTunneler().getServiceWrapper().open(ip, data -> {
                wrapper.setTunnel(data);
                wrapper.map(tunnel -> {
                    source.sendMessage(Text.translatable(
                            "text.tunneler.opened",
                            tunnel.getShortIp()));
                }, error((FeedbackReceiver) source));
            }, error((FeedbackReceiver) source), log -> {});
        }
        return 0;
    }

    @Override
    public void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher,
                         CommandRegistryAccess registryAccess,
                         CommandManager.RegistrationEnvironment environment) {
        var command = LiteralArgumentBuilder
                .<ServerCommandSource>literal("tunnel")
                .then(LiteralArgumentBuilder
                        .<ServerCommandSource>literal("open")
                        .executes(this::tunnelOpen))
                .then(LiteralArgumentBuilder
                        .<ServerCommandSource>literal("close")
                        .executes(this::tunnelClose))
                .then(LiteralArgumentBuilder
                        .<ServerCommandSource>literal("help")
                        .executes(this::tunnelHelp))
                .then(LiteralArgumentBuilder
                        .<ServerCommandSource>literal("token")
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, String>argument(
                                        "token",
                                        string())
                                .executes(this::tunnelToken))
                        .executes(this::tunnelHelp))
                .executes(this::tunnelHelp);
        dispatcher.register(command);
    }
}
