package dev.upcraft.sparkweave.api.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class RegistryArgumentType implements ArgumentType<Identifier> {

	private static final DynamicCommandExceptionType REGISTRY_NOT_FOUND = new DynamicCommandExceptionType(x -> Component.translatable("argument.sparkweave.registry.not_found", x));
	private static final List<String> EXAMPLES = Stream.of(Registries.ITEM, Registries.BLOCK, Registries.BIOME, Registries.BANNER_PATTERN, Registries.CONFIGURED_FEATURE).map(ResourceKey::identifier).map(Identifier::toString).toList();

	public static RegistryArgumentType registry() {
		return new RegistryArgumentType();
	}

	public static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException {
		var location = ctx.getArgument(name, Identifier.class);
		Optional<Registry<T>> optional = ctx.getSource().registryAccess().lookup(ResourceKey.createRegistryKey(location));
		return optional.orElseThrow(() -> REGISTRY_NOT_FOUND.create(location));
	}

	@Override
	public Identifier parse(StringReader reader) throws CommandSyntaxException {
		return Identifier.read(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		if (context.getSource() instanceof SharedSuggestionProvider provider) {
			return SharedSuggestionProvider.suggestResource(provider.registryAccess().listRegistryKeys().map(ResourceKey::identifier), builder);
		}

		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
