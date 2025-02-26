package dev.upcraft.sparkweave.testmod.data;

import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class TestmodBiomes {

	public static final ResourceKey<Biome> TEST_BIOME = ResourceKey.create(Registries.BIOME, SparkweaveTestmod.id("test_biome"));
}
