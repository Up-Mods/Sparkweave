package dev.upcraft.sparkweave.testmod.datagen.common.loot;

import dev.upcraft.sparkweave.testmod.block.BerryBushBlock;
import dev.upcraft.sparkweave.testmod.data.TestmodLootTables;
import dev.upcraft.sparkweave.testmod.init.TestBlocks;
import dev.upcraft.sparkweave.testmod.init.TestItems;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public record TestmodBlockInteractLoot(HolderLookup.Provider registries) implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
		consumer.accept(TestmodLootTables.BLUEBERRY_BUSH_HARVEST, LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(TestItems.BLUEBERRY.get())
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
					.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TestBlocks.BLUEBERRY_BUSH.get())
						.setProperties(StatePropertiesPredicate.Builder.properties()
							.hasProperty(BerryBushBlock.AGE, BerryBushBlock.MAX_AGE)
						)
					)
				)
			)
			.withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(TestItems.BLUEBERRY.get())
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
				)
			));
	}
}
