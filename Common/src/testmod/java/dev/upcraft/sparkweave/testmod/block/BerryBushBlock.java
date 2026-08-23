package dev.upcraft.sparkweave.testmod.block;

import com.mojang.serialization.MapCodec;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import dev.upcraft.sparkweave.testmod.data.ConventionTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.UnaryOperator;

public abstract class BerryBushBlock extends VegetationBlock implements BonemealableBlock, BlockItemProvider {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	public static final int MAX_AGE = 3;
	protected static final VoxelShape SHAPE_SAPLING = Block.column(10.0D, 0.0D, 8.0D);
	protected static final VoxelShape SHAPE_GROWING = Block.column(14.0D, 0.0D, 16.0D);

	private final UnaryOperator<Item.Properties> itemProperties;
	protected final ResourceKey<LootTable> lootTableId;
	private final ResourceKey<DamageType> damageType;
	private final MapCodec<? extends BerryBushBlock> codec;

	public BerryBushBlock(Properties properties, UnaryOperator<Item.Properties> itemProperties, ResourceKey<LootTable> lootTableId, ResourceKey<DamageType> damageType, MapCodec<? extends BerryBushBlock> codec) {
		super(properties);
		this.itemProperties = itemProperties;
		this.lootTableId = lootTableId;
		this.damageType = damageType;
		this.codec = codec;
		this.registerDefaultState(this.getStateDefinition().any().setValue(AGE, 0));
	}

	@Override
	protected MapCodec<? extends VegetationBlock> codec() {
		return codec;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(AGE)) {
			case 0 -> SHAPE_SAPLING;
			case MAX_AGE -> Shapes.block();
			default -> SHAPE_GROWING;
		};
	}

	@Override
	public ResourceKey<Item> createItemId(ResourceKey<Block> blockId) {
		return ResourceKey.create(Registries.ITEM, blockId.identifier().withPath(path -> path.replaceFirst("_bush$", "")));
	}

	@Override
	public Item createItem(Item.Properties properties) {
		return new BlockItem(this, itemProperties.apply(properties.useItemDescriptionPrefix()));
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
		if (entity instanceof LivingEntity && !entity.is(EntityType.FOX) && !entity.is(EntityType.BEE)) {

			entity.makeStuckInBlock(state, new Vec3(0.8D, 0.75D, 0.8D));

			if (level instanceof ServerLevel serverLevel && state.getValue(AGE) > 0) {
				var movement = entity.isClientAuthoritative() ? entity.getKnownMovement() : entity.oldPosition().subtract(entity.position());

				if (movement.horizontalDistanceSqr() > 0.0D) {
					if (Math.abs(movement.x()) >= 0.003D ||  Math.abs(movement.z()) >= 0.003D) {
						entity.hurtServer(serverLevel, level.damageSources().source(this.damageType), 1.0F);
					}
				}
			}
		}
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (state.getValue(AGE) > 1) {
			if (level instanceof ServerLevel serverLevel) {
				var newState = state.setValue(AGE, 1);

				Block.dropFromBlockInteractLootTable(serverLevel, this.lootTableId, state, level.getBlockEntity(pos), null, player, (serverlvl, itemStack) -> Block.popResource(serverlvl, pos, itemStack));
				serverLevel.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
				serverLevel.setBlock(pos, newState, Block.UPDATE_CLIENTS);
				serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
			}

			return InteractionResult.SUCCESS;
		}

		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(state.getValue(AGE) < MAX_AGE && itemStack.is(ConventionTags.Items.FERTILIZERS)) {
			return InteractionResult.PASS;
		}

		return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if(state.getValue(AGE) < MAX_AGE && random.nextInt(5) == 0 && level.getRawBrightness(pos.above(), 0) >= 9) {
			grow(level, random, pos, state);
		}
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
		return true;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		return blockState.getValue(AGE) < MAX_AGE && levelReader.isEmptyBlock(blockPos.above());
	}

	@Override
	public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
		grow(serverLevel, randomSource, blockPos, blockState);
	}

	protected void grow(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);

		if(age < MAX_AGE) {
			var newState = state.setValue(AGE, age + 1);

			level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newState));
		}
	}
}
