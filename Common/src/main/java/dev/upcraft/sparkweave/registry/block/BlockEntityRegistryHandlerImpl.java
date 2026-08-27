package dev.upcraft.sparkweave.registry.block;

import dev.upcraft.sparkweave.api.platform.Services;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.block.BlockEntityRegistryHandler;
import dev.upcraft.sparkweave.platform.SparkweaveHelperService;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BlockEntityRegistryHandlerImpl implements BlockEntityRegistryHandler {

	private static final SparkweaveHelperService SERVICE = Services.getService(SparkweaveHelperService.class);
	private final RegistryHandler<BlockEntityType<?>> delegate;

	public BlockEntityRegistryHandlerImpl(RegistryHandler<BlockEntityType<?>> delegate) {
		this.delegate = delegate;
	}

	@Override
	public final <S extends BlockEntity> RegistrySupplier<BlockEntityType<S>> register(String name, BlockEntityType.BlockEntitySupplier<S> factory, boolean onlyOpCanSetNbt, Set<? extends Supplier<? extends Block>> validBlocks) {
		return register(name, () -> SERVICE.createBlockEntityType(factory, onlyOpCanSetNbt, validBlocks.stream().map(Supplier::get).toArray(Block[]::new)));
	}

	@Override
	public <S extends BlockEntityType<?>> RegistrySupplier<S> register(String name, Supplier<S> factory) {
		return delegate.register(name, factory);
	}

	@Override
	public <S extends BlockEntityType<?>> RegistrySupplier<S> register(ResourceKey<BlockEntityType<?>> id, Supplier<S> factory) {
		return delegate.register(id, factory);
	}

	@Override
	public Registry<BlockEntityType<?>> createNewRegistry(boolean sync, @Nullable Identifier defaultEntry) {
		return delegate.createNewRegistry();
	}

	@Override
	public Map<Identifier, RegistrySupplier<? extends BlockEntityType<?>>> values() {
		return delegate.values();
	}

	@Override
	public List<RegistrySupplier<? extends BlockEntityType<?>>> getEntriesOrdered() {
		return delegate.getEntriesOrdered();
	}

	@Override
	public Stream<RegistrySupplier<? extends BlockEntityType<?>>> stream() {
		return delegate.stream();
	}

	@Override
	public ResourceKey<Registry<BlockEntityType<?>>> registry() {
		return delegate.registry();
	}

	@Override
	public String getNamespace() {
		return delegate.getNamespace();
	}

	@Override
	public void accept(RegistryService registryService) {
		delegate.accept(registryService);
	}
}
