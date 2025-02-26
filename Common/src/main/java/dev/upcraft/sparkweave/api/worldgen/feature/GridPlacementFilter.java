package dev.upcraft.sparkweave.api.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.upcraft.sparkweave.registry.SparkweavePlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class GridPlacementFilter extends PlacementFilter {

	private final RandomSpreadType spreadType;

	/**
	 * Average distance (in chunks) between two neighboring generation attempts.
	 * Value between 0 and 4096 (inclusive).
	 */
	private final int spacing;

	/**
	 * Minimum distance (in chunks) between two neighboring attempts. Value between 0 and 4096 (inclusive).
	 * Has to be strictly smaller than {@link GridPlacementFilter#spacing}.
	 * The maximum distance of two neighboring generation attempts is 2*spacing - separation.
	 */
	private final int separation;

	private final int salt;

	public static final MapCodec<GridPlacementFilter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(f -> f.spreadType),
		ExtraCodecs.intRange(0, 4096).fieldOf("spacing").forGetter(f -> f.spacing),
		ExtraCodecs.intRange(0, 4096).fieldOf("separation").forGetter(f -> f.separation),
		Codec.INT.fieldOf("salt").forGetter(f -> f.salt)
	).apply(instance, GridPlacementFilter::new));

	public GridPlacementFilter(RandomSpreadType spreadType, int spacing, int separation, int salt) {
		this.spreadType = spreadType;
		this.spacing = spacing;
		this.separation = separation;
		this.salt = salt;
	}

	@Override
	protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
		int chunkX = SectionPos.blockToSectionCoord(pos.getX());
		int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());

		// based off https://github.com/TelepathicGrunt/RepurposedStructures/blob/5877644c41d9ffd5e437ff8278104be250960495/common/src/main/java/com/telepathicgrunt/repurposedstructures/world/structures/placements/AdvancedRandomSpread.java#L105-L115
		int regionX = Math.floorDiv(chunkX, this.spacing);
		int regionZ = Math.floorDiv(chunkZ, this.spacing);

		int diff = this.spacing - this.separation;
		var worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
		worldgenRandom.setLargeFeatureWithSalt(context.getLevel().getSeed(), regionX, regionZ, salt);
		int offsetX = this.spreadType.evaluate(worldgenRandom, diff);
		int offsetZ = this.spreadType.evaluate(worldgenRandom, diff);

		return chunkX == regionX * this.spacing + offsetX && chunkZ == regionZ * this.spacing + offsetZ;
	}

	@Override
	public PlacementModifierType<?> type() {
		return SparkweavePlacementModifiers.GRID_FILTER.get();
	}
}
