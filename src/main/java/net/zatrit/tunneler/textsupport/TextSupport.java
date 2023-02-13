package net.zatrit.tunneler.textsupport;

import lombok.SneakyThrows;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import net.minecraft.SharedConstants;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Поддержка версий ниже 1.19.3
 */
public interface TextSupport {
    TextSupport INSTANCE = createInstance();

    @SneakyThrows
    static @NotNull TextSupport createInstance() {
        var predicate = VersionPredicate.parse(">=1.19.3");
        var version = Version.parse(SharedConstants.getGameVersion()
                .getName());

        return predicate.test(version) ?
                new NewText() :
                new LegacyText();
    }

    /**
     * Текст для IP адреса
     */
    Text ipText(String ip);
}

