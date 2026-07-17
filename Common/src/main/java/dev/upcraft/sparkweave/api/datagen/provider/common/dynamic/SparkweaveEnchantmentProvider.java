package dev.upcraft.sparkweave.api.datagen.provider.common.dynamic;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public abstract class SparkweaveEnchantmentProvider extends SparkweaveDynamicRegistryEntryProvider {
	@Override
	public final void generate(RegistrySetBuilder builder) {
		builder.add(Registries.ENCHANTMENT, bootstrapContext -> {
			var damageTypes = bootstrapContext.lookup(Registries.DAMAGE_TYPE);
			var enchantments = bootstrapContext.lookup(Registries.ENCHANTMENT);
			var items = bootstrapContext.lookup(Registries.ITEM);
			var blocks = bootstrapContext.lookup(Registries.BLOCK);
			generateEnchantments(new SparkweaveEnchantmentProvider.Context(bootstrapContext), damageTypes, enchantments, items, blocks);
		});
	}

	protected abstract void generateEnchantments(SparkweaveEnchantmentProvider.Context ctx, HolderGetter<DamageType> damageTypes, HolderGetter<Enchantment> enchantments, HolderGetter<Item> items, HolderGetter<Block> blocks);

	@Override
	public String getName() {
		return "Enchantments";
	}

	public class Context extends SparkweaveDynamicRegistryEntryProvider.Context<Enchantment> {

		protected Context(BootstrapContext<Enchantment> bootstrapContext) {
			super(bootstrapContext);
		}

		public void register(ResourceKey<Enchantment> key, Enchantment value, Lifecycle lifecycle, String name, String description) {
			bootstrapContext.register(key, value, lifecycle);
			if(value.description().getContents() instanceof TranslatableContents translatableContents) {
				addTranslation(translatableContents.getKey(), name);
			} else {
				LOGGER.warn("Cannot translate enchantment {} as its name is not a translatable component!", key.identifier());
			}

			addTranslation(Util.makeDescriptionId("enchantment", key.identifier()) + ".desc", description);
		}

		public void register(ResourceKey<Enchantment> key, Enchantment.Builder builder, Lifecycle lifecycle, String name, String description) {
			register(key, builder.build(key.identifier()), lifecycle, name, description);
		}

		public void register(ResourceKey<Enchantment> key, Enchantment.Builder builder, String name, String description) {
			register(key, builder, Lifecycle.stable(), name, description);
		}
	}
}
