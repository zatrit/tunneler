package net.zatrit.tunneler.server;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.zatrit.tunneler.Tunneler;
import net.zatrit.tunneler.TunnelerConfig;
import net.zatrit.tunneler.service.TunnelServiceWrapper;

/**
 * Инициализатор мода для сервера
 */
@Environment(EnvType.SERVER)
public class TunnelerServer implements DedicatedServerModInitializer {
    private static Tunneler tunneler;

    /**
     * Закрывает туннель, если такой есть
     * без обработки ошибок
     */
    public static void shutdown() {
        tunneler.close(() -> {}, ex -> {});
    }

    @Override
    public void onInitializeServer() {
        AutoConfig.register(TunnelerConfig.class,
                Toml4jConfigSerializer::new);

        final var configHolder = AutoConfig.getConfigHolder(
                TunnelerConfig.class);
        final var serviceWrapper = TunnelServiceWrapper.fromConfig(
                configHolder);
        tunneler = Tunneler
                .builder()
                .serviceWrapper(serviceWrapper)
                .build();
        var commands = ServerCommands
                .builder()
                .configHolder(configHolder)
                .tunneler(tunneler)
                .build();

        CommandRegistrationCallback.EVENT.register(commands);
    }
}
