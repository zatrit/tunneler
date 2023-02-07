package net.zatrit.tunneler;

import com.github.alexdlaird.ngrok.protocol.Region;
import com.mojang.brigadier.context.CommandContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.text.Text;
import net.zatrit.tunneler.interfaces.FeedbackReceiver;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.zatrit.tunneler.ChatUtils.error;

/**
 * Общая для клиента и сервера реализация команд,
 * позволяющая создавать специфичные для среды
 * реализаций команд.
 */
@SuperBuilder
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCommands {
    private final ConfigHolder<TunnelerConfig> configHolder;
    private final Tunneler tunneler;

    /**
     * Задаёт токен ngrok. Применение: /tunnel token [токен]
     */
    protected int tunnelToken(CommandContext<?> context) {
        final var configHolder = this.getConfigHolder();
        final var config = configHolder.getConfig();
        config.token = getString(context, "token");
        configHolder.setConfig(config);
        configHolder.save();
        return 0;
    }

    protected int tunnelRegion(CommandContext<?> context) {
        final var source = (FeedbackReceiver) context.getSource();
        final var configHolder = this.getConfigHolder();
        final var config = configHolder.getConfig();
        final var region = context.getArgument("region", Region.class);

        config.region = region;
        configHolder.setConfig(config);
        configHolder.save();

        return 0;
    }

    /**
     * Сообщение о неизвестной команде
     */
    protected int tunnelUnknownCommand(@NotNull CommandContext<?> context) {
        final var source = (FeedbackReceiver) context.getSource();
        source.sendFeedback(Text.translatable(
                "text.tunneler.unknown_command"));
        return 0;
    }

    /**
     * Команда для закрытия туннеля. Применение: /tunnel close
     */
    protected int tunnelClose(@NotNull CommandContext<?> context) {
        final var source = (FeedbackReceiver) context.getSource();
        this.getTunneler().close(() -> {
            source.sendFeedback(Text.translatable("text.tunneler.closed"));
        }, error(source));
        return 0;
    }
}
