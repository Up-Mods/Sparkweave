package dev.upcraft.sparkweave.api.datagen;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dev.upcraft.sparkweave.SparkweaveHelper;
import dev.upcraft.sparkweave.impl.datagen.RegistrySetBuilderDynamicProvider;
import dev.upcraft.sparkweave.util.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class DynamicRegistryEntryProvider {

    private static final Multimap<String, RegistrySetBuilder> BUILDERS = Multimaps.synchronizedSetMultimap(HashMultimap.create(2, 32));

	protected abstract void generate(RegistrySetBuilder builder);

    public static EntriesProvider.Builder builder(String modid) {
	    Utils.assertValidFabricModId(modid);
        return new EntriesProvider.Builder(modid);
    }

    public static FabricDynamicRegistryProvider getGenerator(String modid, FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        Utils.assertValidFabricModId(modid);

		// this is a dirty hack to work around fabric api issues
		registriesFuture.join();

		return new RegistrySetBuilderDynamicProvider(output, registriesFuture, BUILDERS.removeAll(modid), SparkweaveHelper.id("dynamic_registries/" + modid));
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
                BUILDERS.put(modid, registrySetBuilder);
                for(var provider : providers) {
                    provider.get().generate(registrySetBuilder);
                }
            }
        }
    }
}
