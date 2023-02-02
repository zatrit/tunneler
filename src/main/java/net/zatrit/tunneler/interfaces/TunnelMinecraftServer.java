package net.zatrit.tunneler.interfaces;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Интерфейс для взаимодействия с полем tunnelEnable. См.:
 * <ul>
 * <li>{@link net.zatrit.tunneler.mixin.client.OpenToLanScreenMixin}</li>
 * <li>{@link net.zatrit.tunneler.mixin.client.IntegratedServerMixin}</li>
 * </ul>
 */
@Environment(EnvType.CLIENT)
public interface TunnelMinecraftServer {
    boolean isTunnelEnabled();

    void setTunnelEnabled(boolean enabled);
}
