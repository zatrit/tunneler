package net.zatrit.tunneler.service;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.protocol.CreateTunnel;
import com.github.alexdlaird.ngrok.protocol.Proto;
import lombok.RequiredArgsConstructor;
import me.shedaniel.autoconfig.ConfigHolder;
import net.zatrit.tunneler.TunnelerConfig;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Реализация {@link TunnelServiceWrapper} для ngrok
 * Класс имеет модификатор доступа package-only, так как
 * для инициализации класса надо использовать {@link TunnelServiceWrapper#fromConfig}
 */
@RequiredArgsConstructor
class NgrokWrapper implements TunnelServiceWrapper {
    private final ConfigHolder<TunnelerConfig> configHolder;
    private final ExecutorService threadPool = Executors.newSingleThreadExecutor();
    private NgrokClient ngrokClient;

    public void open(String ip,
                     Consumer<TunnelData> callback,
                     Consumer<Exception> errorCallback,
                     Consumer<String> log) {
        // Открывает туннель в новом потоке, чтобы игра не зависала
        threadPool.execute(() -> openSync(ip,
                callback,
                errorCallback,
                log));
    }

    /**
     * Открывает туннель, используется в {@link TunnelServiceWrapper#open}
     */
    private void openSync(String ip,
                          Consumer<TunnelData> callback,
                          Consumer<Exception> errorCallback,
                          Consumer<String> log) {
        try {
            final var config = this.configHolder.getConfig();
            // Создаёт конфиг из настроек
            final var ngrokConfig = new JavaNgrokConfig.Builder()
                    .withAuthToken(config.token)
                    .withRegion(config.region)
                    .withLogEventCallback(ngrokLog -> {
                        log.accept(ngrokLog.getMsg());
                        return null;
                    })
                    .build();
            // Создаёт клиент из конфига
            this.ngrokClient = new NgrokClient.Builder()
                    .withJavaNgrokConfig(ngrokConfig)
                    .build();
            // Создаёт наконец сам туннель
            final var createTunnel = new CreateTunnel.Builder()
                    .withProto(Proto.TCP)
                    .withAddr(ip)
                    .build();
            final var tunnel = this.ngrokClient.connect(createTunnel);
            // Вызывает заданную функцию с данными о туннеле
            callback.accept(new TunnelData(tunnel.getPublicUrl()));
        } catch (Exception ex) {
            errorCallback.accept(ex);
        }
    }

    public void close(TunnelData data,
                      Runnable callback,
                      Consumer<Exception> errorCallback) {
        threadPool.execute(() -> closeSync(data,
                callback,
                errorCallback));
    }

    /**
     * Закрывает туннель, используется в {@link TunnelServiceWrapper#close}
     */
    private void closeSync(@NotNull TunnelData tunnelData,
                           Runnable callback,
                           Consumer<Exception> errorCallback) {
        try {
            this.ngrokClient.disconnect(tunnelData.getIp());
            this.ngrokClient.kill();
            callback.run();
        } catch (Exception ex) {
            errorCallback.accept(ex);
        }
    }
}
