package net.zatrit.tunneler;

import lombok.Setter;
import net.minecraft.text.Text;
import net.zatrit.tunneler.service.TunnelData;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Обёртка для {@link TunnelData}
 * для избежания NullReferenceException и удобной обработки ошибок
 */
public class TunnelWrapper {
    /**
     * Позволяет задать данный туннель для работы с ним
     */
    @Setter
    private @Nullable TunnelData tunnel = null;

    /**
     * Выполняет функцию для туннеля, если таковой имеется, иначе выдаёт ошибку
     */
    public void map(Consumer<TunnelData> callback,
                    Consumer<Exception> errorCallback) {
        try {
            if (this.tunnel == null) {
                /*Выдаётся ошибка неверного состояния при попытке
                работать с несуществующим туннель. Тут используется TranslatableText
                вместо I18n, так как I18n доступен только на клиенте*/
                throw new IllegalStateException(Text
                        .translatable("text.tunneler.no_tunnels")
                        .getString());
            }
            callback.accept(this.tunnel);
        } catch (Exception ex) {
            errorCallback.accept(ex);
        }
    }

    /**
     * Проверяет, существует ли туннель
     */
    public boolean hasTunnel() {
        return this.tunnel != null;
    }
}
