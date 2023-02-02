package net.zatrit.tunneler;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import net.zatrit.tunneler.service.TunnelServiceWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static net.zatrit.tunneler.ChatUtils.error;

// Класс, реализующий основной функционал мода
// также он имеет автоматическую реализацию паттерна builder'а
@Getter
@SuperBuilder
public class Tunneler {
    private final TunnelServiceWrapper serviceWrapper;
    private final TunnelWrapper tunnelWrapper = new TunnelWrapper();
    private @Builder.Default String defaultIp = "127.0.0.1";

    @Nullable
    public String getServerIp(@NotNull MinecraftServer server) {
        if (server.getServerPort() != -1) {
            return Optional
                    .ofNullable(server.getServerIp())
                    .orElse(getDefaultIp()) +
                    ":" +
                    server.getServerPort();
        } else {
            return null;
        }
    }

    public void openOnIp(FeedbackReceiver source, String ip) {
        if (this.getTunnelWrapper().hasTunnel()) {
            source.sendError(Text.translatable(
                    "text.tunneler.already_opened"));
            return;
        }
        if (ip == null) {
            source.sendError(Text.translatable(
                    "text.tunneler.not_opened"));
            return;
        }

        this.getServiceWrapper().open(ip, data -> {
            this.getTunnelWrapper().setTunnel(data);
            this.getTunnelWrapper().map(tunnel -> {
                source.sendFeedback(Text.translatable(
                        "text.tunneler.opened",
                        tunnel.getShortIp()));
            }, error(source));
        }, error(source), log -> {
        });
    }

    // Функция для открытия туннеля для определённого сервера
    // используется в net.zatrit.tunneler.client.TunnelerClient
    public void openForServer(FeedbackReceiver source,
                              MinecraftServer server) {
        openOnIp(source, getServerIp(server));
    }

    public void close(Runnable success,
                      Consumer<Exception> error) {
        this.getTunnelWrapper().map(tunnel -> {
            this.getServiceWrapper().close(tunnel, success, error);
            this.getTunnelWrapper().setTunnel(null);
        }, error);
    }
}
