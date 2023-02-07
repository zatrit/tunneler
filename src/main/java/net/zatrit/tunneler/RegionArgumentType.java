package net.zatrit.tunneler;

import com.github.alexdlaird.ngrok.protocol.Region;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Значение аргумента команды для региона, используется в
 * {@link AbstractCommands}
 */
public class RegionArgumentType implements ArgumentType<Region> {
    @Override
    public Region parse(StringReader reader) throws
            CommandSyntaxException {
        // Преобразование аргумента из строки в регион
        return Region.valueOf(reader.readString().toUpperCase());
    }

    @Override
    public Collection<String> getExamples() {
        return Stream.of(Region.values())
                .map(Region::toString)
                .collect(Collectors.toList());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            CommandContext<S> context,
            SuggestionsBuilder builder) {
        Stream.of(Region.values())
                .map(Region::toString)
                .map(String::toUpperCase)
                .forEach(builder::suggest);
        return builder.buildFuture();
    }
}
