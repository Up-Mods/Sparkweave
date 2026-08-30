package dev.upcraft.sparkweave.testmod.init;

import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import dev.upcraft.sparkweave.api.registry.entity.EntityRegistryHandler;
import dev.upcraft.sparkweave.testmod.SparkweaveTestmod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.boat.Boat;

public class TestEntities {
	public static final EntityRegistryHandler ENTITIES = RegistryHandler.entities(SparkweaveTestmod.MODID);

	public static final RegistrySupplier<EntityType<Boat>> TEST_BOAT = ENTITIES.register("test_boat", (entityType, level) -> new Boat(entityType, level, TestItems.TEST_BOAT), MobCategory.MISC, builder -> builder
		.noLootTable()
		.sized(1.375F, 0.5625F)
		.eyeHeight(0.5625F)
		.clientTrackingRange(10)
	);
}
