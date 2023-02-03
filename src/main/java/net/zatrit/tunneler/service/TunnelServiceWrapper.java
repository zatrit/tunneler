package net.zatrit.tunneler.service;

import me.shedaniel.autoconfig.ConfigHolder;
import net.zatrit.tunneler.TunnelerConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Класс, позволяющий расширять функционал мода другими сервисами
 * кроме ngrok.
 */
public interface TunnelServiceWrapper {
    /**
     * @return Реализация данного интерфейса для конфига
     */
    @Contract("_ -> new")
    static @NotNull TunnelServiceWrapper fromConfig(ConfigHolder<TunnelerConfig> configHolder) {
        return new NgrokWrapper(configHolder);
    }

    /**
     * Метод, открывающий туннель для данного IP
     *
     * @param ip            Адрес, для которого будет открыт туннель
     * @param callback      Обработчик результата открытия туннеля
     * @param errorCallback Обработчик ошибки открытия туннеля
     * @param log           Обработчик логов сервиса
     */
    void open(String ip,
              Consumer<TunnelData> callback,
              Consumer<Exception> errorCallback,
              Consumer<String> log);

    /**
     * Закрывает туннель
     *
     * @param data          Туннель, которые закрывается
     * @param callback      Обработчик успешного результата
     * @param errorCallback Обработчик ошибок
     */
    void close(TunnelData data,
               Runnable callback,
               Consumer<Exception> errorCallback);
}
