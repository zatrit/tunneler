package net.zatrit.tunneler.client;

import lombok.Getter;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.zatrit.tunneler.Tunneler;
import net.zatrit.tunneler.TunnelerConfig;
import net.zatrit.tunneler.service.NgrokWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class TunnelerClient implements ClientModInitializer {
    @Getter
    private static final Logger logger = LoggerFactory.getLogger(
            "Tunneler");

    @Getter
    private static Tunneler tunneler;

    public static void shutdown() {
        tunneler.close(() -> {}, ex -> {});
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(TunnelerConfig.class,
                Toml4jConfigSerializer::new);

        final var configHolder = AutoConfig.getConfigHolder(
                TunnelerConfig.class);
        tunneler = Tunneler
                .builder()
                .serviceWrapper(new NgrokWrapper(configHolder))
                .build();
        var commands = ClientCommands
                .builder()
                .configHolder(configHolder)
                .tunneler(tunneler)
                .build();

        ClientCommandRegistrationCallback.EVENT.register(commands);
    }
}
