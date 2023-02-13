package net.zatrit.tunneler;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import net.zatrit.tunneler.service.TunnelServiceWrapper;
import net.zatrit.tunneler.textsupport.TextSupport;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

import static net.zatrit.tunneler.ChatUtils.error;

/**
 * Класс, реализующий основной функционал мода
 * также он имеет автоматическую реализацию паттерна builder'а
 */
@Getter
@SuperBuilder
public class Tunneler {
    /**
     * Реализации сервиса для создания туннелей
     */
    private final TunnelServiceWrapper serviceWrapper;
    /**
     * Обёртка для туннеля, подробнее в описании {@link TunnelWrapper}
     */
    private final TunnelWrapper tunnelWrapper = new TunnelWrapper();
    /**
     * Стандартный IP для туннелей
     */
    private @Builder.Default String defaultIp = "127.0.0.1";

    /**
     * @return IP адрес сервера
     */
    @Nullable
    public String getServerIp(@NotNull MinecraftServer server) {
        return Optional.of(getDefaultIp())
                .or(() -> Optional.ofNullable(server.getServerIp()))
                .filter(s -> server.getServerPort() != -1)
                .map(s -> s + ":" + server.getServerPort())
                .orElse(null);
    }

    public void openOnIp(FeedbackReceiver source, String ip) {
        if (this.getTunnelWrapper().hasTunnel()) {
            // Возвращает ошибку, если туннель уже открыт
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
                        TextSupport.INSTANCE.ipText(tunnel.getShortIp())));
            }, error(source));
        }, error(source), log -> {
        });
    }

    /**
     * Функция для открытия туннеля для определённого сервера
     * используется в {@link net.zatrit.tunneler.client.TunnelerClient}
     */
    public void openForServer(FeedbackReceiver source,
                              MinecraftServer server) {
        openOnIp(source, getServerIp(server));
    }

    /**
     * Функция для закрытия туннеля с обработчиком ошибок и
     * успешного результата
     */
    public void close(Runnable success, Consumer<Exception> error) {
        this.getTunnelWrapper().map(tunnel -> {
            this.getServiceWrapper().close(tunnel, success, error);
            this.getTunnelWrapper().setTunnel(null);
        }, error);
    }
}
