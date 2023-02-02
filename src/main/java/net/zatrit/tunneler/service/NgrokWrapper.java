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

@RequiredArgsConstructor
public class NgrokWrapper implements TunnelServiceWrapper {
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

    private void openSync(String ip,
                          Consumer<TunnelData> callback,
                          Consumer<Exception> errorCallback,
                          Consumer<String> log) {
        try {
            // Создаёт конфиг из настроек
            final var ngrokConfig = new JavaNgrokConfig.Builder()
                    .withAuthToken(this.getConfig().token)
                    .withRegion(this.getConfig().region)
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

    private TunnelerConfig getConfig() {
        return this.configHolder.getConfig();
    }

    public void close(TunnelData data,
                      Runnable callback,
                      Consumer<Exception> errorCallback) {
        threadPool.execute(() -> closeSync(data, callback, errorCallback));
    }

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
