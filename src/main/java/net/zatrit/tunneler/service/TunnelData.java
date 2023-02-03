package net.zatrit.tunneler.service;

import lombok.Data;
import lombok.NonNull;

/**
 * Класс данных для хранения данных о туннеле
 */
@Data
public class TunnelData {
    private @NonNull String ip;

    /**
     * @return Короткий IP адрес, который можно использовать
     * для подключения к серверу
     */
    public String getShortIp() {
        return this.getIp().substring(6);
    }
}