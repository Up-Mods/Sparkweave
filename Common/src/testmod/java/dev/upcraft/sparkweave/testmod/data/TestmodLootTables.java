package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestmodLootTables {

	private static final Set<ResourceKey<LootTable>> ALL_LOOT_TABLES = new HashSet<>();
	private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOOT_TABLES = Collections.unmodifiableSet(ALL_LOOT_TABLES);

	public static Set<ResourceKey<LootTable>> all() {
		return IMMUTABLE_LOOT_TABLES;
	}

	private static ResourceKey<LootTable> register(String name) {
		var key = ResourceKey.create(Registries.LOOT_TABLE, SparkweaveTestmod.id(name));
		if(!ALL_LOOT_TABLES.add(key)) {
			throw new IllegalArgumentException("Loot table %s already registered!".formatted(key.identifier()));
		}

		return key;
	}

	public static final ResourceKey<LootTable> BLUEBERRY_BUSH_HARVEST = register("blueberry_bush_harvest");
}
