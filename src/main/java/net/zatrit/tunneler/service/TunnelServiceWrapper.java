package net.zatrit.tunneler.service;

import java.util.function.Consumer;

public interface TunnelServiceWrapper {
    void open(String ip,
              Consumer<TunnelData> callback,
              Consumer<Exception> errorCallback,
              Consumer<String> log);

    void close(TunnelData data,
               Runnable callback,
               Consumer<Exception> errorCallback);
}
