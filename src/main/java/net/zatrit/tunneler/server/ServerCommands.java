package net.zatrit.tunneler.server;

import com.github.alexdlaird.ngrok.protocol.Region;
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
import net.zatrit.tunneler.RegionArgumentType;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.string;

/**
 * Команды для сервера для работы с ngrok
 */
@SuperBuilder
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
        final var receiver = (FeedbackReceiver) source;
        this.getTunneler().openForServer(receiver, server);
        return 0;
    }

    /**
     * Регистрирует все команды с помощью функционала FabricMC
     */
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
                        .<ServerCommandSource>literal("token")
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, String>argument(
                                        "token", string())
                                .executes(this::tunnelToken))
                        .executes(this::tunnelUnknownCommand))
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("region")
                        .then(RequiredArgumentBuilder
                                .<ServerCommandSource, Region>argument(
                                        "region", new RegionArgumentType())
                                .executes(this::tunnelRegion))
                        .executes(this::tunnelUnknownCommand))
                .executes(this::tunnelUnknownCommand);
        dispatcher.register(command);
    }
}
