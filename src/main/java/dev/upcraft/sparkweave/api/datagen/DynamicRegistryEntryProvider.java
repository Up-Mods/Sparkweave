package dev.upcraft.sparkweave.api.datagen;

import dev.upcraft.sparkweave.SparkweaveHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class DynamicRegistryEntryProvider {

    private static final Map<String, RegistrySetBuilder> fabricBuilderHack = new HashMap<>();

	protected abstract void generate(RegistrySetBuilder builder);

    public static EntriesProvider.Builder builder(String modid) {
        return new EntriesProvider.Builder(modid);
    }

    public static synchronized FabricDynamicRegistryProvider getGenerator(String modid, FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        var registryBuilder = fabricBuilderHack.remove(modid);
		return new FabricDynamicRegistryProvider(output, registriesFuture) {
            @Override
            protected void configure(HolderLookup.Provider registries, Entries entries) {
                if(registryBuilder != null) {
	                registryBuilder.getEntries().stream()
                            .map(RegistrySetBuilder.RegistryStub::key)
                            .distinct()
                            .map(registries::lookupOrThrow)
                            .forEach(entries::addAll);
                }
            }

            @Override
            public String getName() {
                return SparkweaveHelper.id(modid + "_dynamic_registries").toString();
            }
        };
    }

    public static class EntriesProvider {

        public static class Builder {

	        private final List<Supplier<DynamicRegistryEntryProvider>> providers = new ArrayList<>();
			private final String modid;

            private Builder(String modid) {
				this.modid = modid;
            }

            public Builder add(Supplier<DynamicRegistryEntryProvider> provider) {
                providers.add(provider);
                return this;
            }

            public void build(RegistrySetBuilder registrySetBuilder) {
                fabricBuilderHack.put(modid, registrySetBuilder);
                for(var provider : providers) {
                    provider.get().generate(registrySetBuilder);
                }
            }
        }
    }
}
