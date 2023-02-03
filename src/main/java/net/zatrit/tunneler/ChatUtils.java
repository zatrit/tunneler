package net.zatrit.tunneler;

import net.minecraft.text.Text;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Утилитный класс, позволяющий не дублировать код вывода ошибок
 */
public class ChatUtils {
    /**
     * @return Обработчик ошибок для {@link FeedbackReceiver}
     */
    @Contract(pure = true)
    public static @NotNull Consumer<Exception> error(FeedbackReceiver source) {
        return ex -> source.sendError(Text.translatable(
                "text.tunneler.error",
                Text.of(ex.getMessage())));
    }
}
