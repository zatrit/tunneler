package net.zatrit.tunneler.service;

import lombok.Data;
import lombok.NonNull;

@Data
public class TunnelData {
    private @NonNull String ip;

    public String getShortIp() {
        return this.getIp().substring(6);
    }
}