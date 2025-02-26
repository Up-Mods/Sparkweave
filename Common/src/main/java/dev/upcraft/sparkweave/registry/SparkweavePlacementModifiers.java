package dev.upcraft.sparkweave.registry;

import dev.upcraft.sparkweave.SparkweaveMod;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.worldgen.feature.GridPlacementFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class SparkweavePlacementModifiers {

	public static final RegistryHandler<PlacementModifierType<?>> MODIFIERS = RegistryHandler.create(Registries.PLACEMENT_MODIFIER_TYPE, SparkweaveMod.MODID);

	public static final RegistrySupplier<PlacementModifierType<GridPlacementFilter>> GRID_FILTER = MODIFIERS.register("grid_filter", () -> () -> GridPlacementFilter.CODEC);
}
