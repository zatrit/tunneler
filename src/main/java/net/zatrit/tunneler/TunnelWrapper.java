package net.zatrit.tunneler;

import lombok.Setter;
import net.minecraft.client.resource.language.I18n;
import net.zatrit.tunneler.service.TunnelData;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class TunnelWrapper {
    @Setter
    private @Nullable TunnelData tunnel = null;

    public void map(Consumer<TunnelData> callback,
                    Consumer<Exception> errorCallback) {
        try {
            if (this.tunnel == null) {
                throw new IllegalStateException(I18n.translate(
                        "text.tunneler.no_tunnels"));
            }
            callback.accept(this.tunnel);
        } catch (Exception ex) {
            errorCallback.accept(ex);
        }
    }

    public boolean hasTunnel() {
        return this.tunnel != null;
    }
}
