package com.Legoman1342.blocks.custom;

import com.Legoman1342.blocks.ATMultiblock;
import com.Legoman1342.blocks.ATMultiblock.ATMultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class SurfaceButton extends BasePressurePlateBlock {

	Logger LOGGER = LogManager.getLogger();

	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final EnumProperty<ATMultiblockPart> PART = EnumProperty.create("part", ATMultiblockPart.class);
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	ATMultiblock multiblock = new ATMultiblock(true, true, true, FACING, PART);

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, PART, POWERED);
	}

	public SurfaceButton(Properties properties) {
		super(properties);
	}

	/**
	 * Sets additional blockstates (<code>POWERED</code>) that {@link ATMultiblock#getStateForPlacement(BlockPlaceContext, Block) ATMultiblock#getStateForPlacement} doesn't cover.
	 */
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState toReturn = multiblock.getStateForPlacement(pContext, this);
		if (toReturn != null) {
			return toReturn.setValue(POWERED, false);
		} else {
			return null;
		}
	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		multiblock.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
	}

	@Override
	protected void playOnSound(LevelAccessor pLevel, BlockPos pPos) {
		//TODO
	}

	@Override
	protected void playOffSound(LevelAccessor pLevel, BlockPos pPos) {
		//TODO
	}

	public boolean isSignalSource(BlockState pState) {
		return true;
	}

	@Override
	protected int getPressedTime() {
		return 0;
	}

	@Override
	protected int getSignalStrength(Level pLevel, BlockPos pPos) {
		BlockState state = pLevel.getBlockState(pPos);
		AABB aabb = switch (state.getValue(FACING)) {
			case UP ->  switch (state.getValue(PART)) {
				case BOTTOM_LEFT -> UP_BOTTOM_LEFT_AABB.move(pPos);
				case BOTTOM_RIGHT -> UP_BOTTOM_RIGHT_AABB.move(pPos);
				case TOP_LEFT -> UP_TOP_LEFT_AABB.move(pPos);
				case TOP_RIGHT -> UP_TOP_RIGHT_AABB.move(pPos);
			};
			case DOWN -> switch (state.getValue(PART)) {
				case BOTTOM_LEFT -> DOWN_BOTTOM_LEFT_AABB.move(pPos);
				case BOTTOM_RIGHT -> DOWN_BOTTOM_RIGHT_AABB.move(pPos);
				case TOP_LEFT -> DOWN_TOP_LEFT_AABB.move(pPos);
				case TOP_RIGHT -> DOWN_TOP_RIGHT_AABB.move(pPos);
			};
			case NORTH -> switch (state.getValue(PART)) {
				case BOTTOM_LEFT -> NORTH_BOTTOM_LEFT_AABB.move(pPos);
				case BOTTOM_RIGHT -> NORTH_BOTTOM_RIGHT_AABB.move(pPos);
				case TOP_LEFT -> NORTH_TOP_LEFT_AABB.move(pPos);
				case TOP_RIGHT -> NORTH_TOP_RIGHT_AABB.move(pPos);
			};
			case EAST -> switch (state.getValue(PART)) {
				case BOTTOM_LEFT -> EAST_BOTTOM_LEFT_AABB.move(pPos);
				case BOTTOM_RIGHT -> EAST_BOTTOM_RIGHT_AABB.move(pPos);
				case TOP_LEFT -> EAST_TOP_LEFT_AABB.move(pPos);
				case TOP_RIGHT -> EAST_TOP_RIGHT_AABB.move(pPos);
			};
			case SOUTH -> switch (state.getValue(PART)) {
				case BOTTOM_LEFT -> SOUTH_BOTTOM_LEFT_AABB.move(pPos);
				case BOTTOM_RIGHT -> SOUTH_BOTTOM_RIGHT_AABB.move(pPos);
				case TOP_LEFT -> SOUTH_TOP_LEFT_AABB.move(pPos);
				case TOP_RIGHT -> SOUTH_TOP_RIGHT_AABB.move(pPos);
			};
			case WEST -> switch (state.getValue(PART)) {
				case BOTTOM_LEFT -> WEST_BOTTOM_LEFT_AABB.move(pPos);
				case BOTTOM_RIGHT -> WEST_BOTTOM_RIGHT_AABB.move(pPos);
				case TOP_LEFT -> WEST_TOP_LEFT_AABB.move(pPos);
				case TOP_RIGHT -> WEST_TOP_RIGHT_AABB.move(pPos);
			};
		};
		List<? extends LivingEntity> entitiesOnButton = pLevel.getEntitiesOfClass(LivingEntity.class, aabb);
		if (!entitiesOnButton.isEmpty()) {
			for(Entity entity : entitiesOnButton) {
				if (!entity.isIgnoringBlockTriggers()) {
					return 15;
				}
			}
		}
		return 0;
	}

	@Override
	protected int getSignalForState(BlockState pState) {
		return pState.getValue(POWERED) ? 15 : 0;
	}

	@Override
	protected BlockState setSignalForState(BlockState pState, int pStrength) {
		return pState.setValue(POWERED, pStrength > 0);
	}


	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		return multiblock.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos, this);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction facing = pState.getValue(FACING);
		ATMultiblockPart part = pState.getValue(PART);
		boolean activated = pState.getValue(POWERED);
		return switch (facing) {
			case UP -> switch (part) {
				case TOP_LEFT -> activated ? UP_TOP_LEFT_ACTIVATED : UP_TOP_LEFT_DEACTIVATED;
				case TOP_RIGHT -> activated ? UP_TOP_RIGHT_ACTIVATED : UP_TOP_RIGHT_DEACTIVATED;
				case BOTTOM_LEFT -> activated ? UP_BOTTOM_LEFT_ACTIVATED : UP_BOTTOM_LEFT_DEACTIVATED;
				case BOTTOM_RIGHT -> activated ? UP_BOTTOM_RIGHT_ACTIVATED : UP_BOTTOM_RIGHT_DEACTIVATED;
			};
			case DOWN -> switch (part) {
				case TOP_LEFT -> activated ? DOWN_TOP_LEFT_ACTIVATED : DOWN_TOP_LEFT_DEACTIVATED;
				case TOP_RIGHT -> activated ? DOWN_TOP_RIGHT_ACTIVATED : DOWN_TOP_RIGHT_DEACTIVATED;
				case BOTTOM_LEFT -> activated ? DOWN_BOTTOM_LEFT_ACTIVATED : DOWN_BOTTOM_LEFT_DEACTIVATED;
				case BOTTOM_RIGHT -> activated ? DOWN_BOTTOM_RIGHT_ACTIVATED : DOWN_BOTTOM_RIGHT_DEACTIVATED;
			};
			case NORTH -> switch (part) {
				case TOP_LEFT -> activated ? NORTH_TOP_LEFT_ACTIVATED : NORTH_TOP_LEFT_DEACTIVATED;
				case TOP_RIGHT -> activated ? NORTH_TOP_RIGHT_ACTIVATED : NORTH_TOP_RIGHT_DEACTIVATED;
				case BOTTOM_LEFT -> activated ? NORTH_BOTTOM_LEFT_ACTIVATED : NORTH_BOTTOM_LEFT_DEACTIVATED;
				case BOTTOM_RIGHT -> activated ? NORTH_BOTTOM_RIGHT_ACTIVATED : NORTH_BOTTOM_RIGHT_DEACTIVATED;
			};
			case SOUTH -> switch (part) {
				case TOP_LEFT -> activated ? SOUTH_TOP_LEFT_ACTIVATED : SOUTH_TOP_LEFT_DEACTIVATED;
				case TOP_RIGHT -> activated ? SOUTH_TOP_RIGHT_ACTIVATED : SOUTH_TOP_RIGHT_DEACTIVATED;
				case BOTTOM_LEFT -> activated ? SOUTH_BOTTOM_LEFT_ACTIVATED : SOUTH_BOTTOM_LEFT_DEACTIVATED;
				case BOTTOM_RIGHT -> activated ? SOUTH_BOTTOM_RIGHT_ACTIVATED : SOUTH_BOTTOM_RIGHT_DEACTIVATED;
			};
			case EAST -> switch (part) {
				case TOP_LEFT -> activated ? EAST_TOP_LEFT_ACTIVATED : EAST_TOP_LEFT_DEACTIVATED;
				case TOP_RIGHT -> activated ? EAST_TOP_RIGHT_ACTIVATED : EAST_TOP_RIGHT_DEACTIVATED;
				case BOTTOM_LEFT -> activated ? EAST_BOTTOM_LEFT_ACTIVATED : EAST_BOTTOM_LEFT_DEACTIVATED;
				case BOTTOM_RIGHT -> activated ? EAST_BOTTOM_RIGHT_ACTIVATED : EAST_BOTTOM_RIGHT_DEACTIVATED;
			};
			case WEST -> switch (part) {
				case TOP_LEFT -> activated ? WEST_TOP_LEFT_ACTIVATED : WEST_TOP_LEFT_DEACTIVATED;
				case TOP_RIGHT -> activated ? WEST_TOP_RIGHT_ACTIVATED : WEST_TOP_RIGHT_DEACTIVATED;
				case BOTTOM_LEFT -> activated ? WEST_BOTTOM_LEFT_ACTIVATED : WEST_BOTTOM_LEFT_DEACTIVATED;
				case BOTTOM_RIGHT -> activated ? WEST_BOTTOM_RIGHT_ACTIVATED : WEST_BOTTOM_RIGHT_DEACTIVATED;
			};
		};
	}

	//Defining AABBs (Axis Aligned Bounding Boxes) used for detecting collisions
	protected static final AABB UP_BOTTOM_LEFT_AABB = new AABB(0, 0.125, 0.375, 0.625, 0.375, 1);
	protected static final AABB UP_BOTTOM_RIGHT_AABB = new AABB(0.375, 0.125, 0.375, 1, 0.375, 1);
	protected static final AABB UP_TOP_LEFT_AABB = new AABB(0, 0.125, 0, 0.625, 0.375, 0.625);
	protected static final AABB UP_TOP_RIGHT_AABB = new AABB(0.375, 0.125, 0, 1, 0.375, 0.625);
	protected static final AABB DOWN_BOTTOM_LEFT_AABB = new AABB(0, 0.625, 0, 0.625, 0.875, 0.625);
	protected static final AABB DOWN_BOTTOM_RIGHT_AABB = new AABB(0.375, 0.625, 0, 1, 0.875, 0.625);
	protected static final AABB DOWN_TOP_LEFT_AABB = new AABB(0, 0.625, 0.375, 0.625, 0.875, 1);
	protected static final AABB DOWN_TOP_RIGHT_AABB = new AABB(0.375, 0.625, 0.375, 1, 0.875, 1);
	protected static final AABB NORTH_BOTTOM_LEFT_AABB = new AABB(0,  0.375, 0.625, 0.625, 1, 0.875);
	protected static final AABB NORTH_BOTTOM_RIGHT_AABB = new AABB(0.375, 0.375, 0.625, 1, 1, 0.875);
	protected static final AABB NORTH_TOP_LEFT_AABB = new AABB(0, 0, 0.625, 0.625, 0.625, 0.875);
	protected static final AABB NORTH_TOP_RIGHT_AABB = new AABB(0.375, 0.375, 0.625, 1, 1, 0.875);
	protected static final AABB SOUTH_BOTTOM_LEFT_AABB = new AABB (0.375,0.375,0.125,1,1,0.375);
	protected static final AABB SOUTH_BOTTOM_RIGHT_AABB = new AABB(0, 0.375, 0.125, 0.625, 1, 0.375);
	protected static final AABB SOUTH_TOP_LEFT_AABB = new AABB(0.375, 0, 0.125, 1, 0.625, 0.375);
	protected static final AABB SOUTH_TOP_RIGHT_AABB = new AABB(0, 0, 0.125, 0.625, 0.625, 0.375);
	protected static final AABB EAST_BOTTOM_LEFT_AABB = new AABB(0.125, 0.375, 0, 0.375, 1, 0.625);
	protected static final AABB EAST_BOTTOM_RIGHT_AABB = new AABB(0.125, 0.375, 0.375, 0.375, 1, 1);
	protected static final AABB EAST_TOP_LEFT_AABB = new AABB(0.125, 0, 0, 0.375, 0.625, 0.625);
	protected static final AABB EAST_TOP_RIGHT_AABB = new AABB(0.125, 0, 0.375, 0.375, 0.625, 1);
	protected static final AABB WEST_BOTTOM_LEFT_AABB = new AABB(0.625, 0.375, 0.375, 0.875, 1, 1);
	protected static final AABB WEST_BOTTOM_RIGHT_AABB = new AABB(0.625, 0.375, 0, 0.875, 1, 0.625);
	protected static final AABB WEST_TOP_LEFT_AABB = new AABB(0.625, 0, 0.375, 0.875, 0.625, 1);
	protected static final AABB WEST_TOP_RIGHT_AABB = new AABB(0.625, 0, 0, 0.875, 0.625, 0.625);



	//Defining VoxelShapes used for different block states
	private static final VoxelShape UP_BOTTOM_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 0, 8, 12, 2, 16),
			Block.box(0, 0, 4, 8, 2, 8),
			Block.box(12, 0, 14, 16, 1, 16),
			Block.box(0, 0, 0, 2, 1, 4)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_BOTTOM_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 0, 8, 12, 2, 16),
			Block.box(0, 0, 4, 8, 2, 8),
			Block.box(12, 0, 14, 16, 1, 16),
			Block.box(0, 0, 0, 2, 1, 4),
			Block.box(0, 2, 9, 10, 3, 16),
			Block.box(0, 2, 6, 7, 3, 9)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_BOTTOM_RIGHT_ACTIVATED = Stream.of(
			Block.box(8, 0, 4, 16, 2, 16),
			Block.box(4, 0, 8, 8, 2, 16),
			Block.box(14, 0, 0, 16, 1, 4),
			Block.box(0, 0, 14, 4, 1, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_BOTTOM_RIGHT_DEACTIVATED = Stream.of(
			Block.box(8, 0, 4, 16, 2, 16),
			Block.box(4, 0, 8, 8, 2, 16),
			Block.box(14, 0, 0, 16, 1, 4),
			Block.box(0, 0, 14, 4, 1, 16),
			Block.box(9, 2, 6, 16, 3, 16),
			Block.box(6, 2, 9, 9, 3, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_TOP_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 0, 0, 8, 2, 12),
			Block.box(8, 0, 0, 12, 2, 8),
			Block.box(0, 0, 12, 2, 1, 16),
			Block.box(12, 0, 0, 16, 1, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_TOP_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 0, 0, 8, 2, 12),
			Block.box(8, 0, 0, 12, 2, 8),
			Block.box(0, 0, 12, 2, 1, 16),
			Block.box(12, 0, 0, 16, 1, 2),
			Block.box(0, 2, 0, 7, 3, 10),
			Block.box(7, 2, 0, 10, 3, 7)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_TOP_RIGHT_ACTIVATED = Stream.of(
			Block.box(4, 0, 0, 16, 2, 8),
			Block.box(8, 0, 8, 16, 2, 12),
			Block.box(0, 0, 0, 4, 1, 2),
			Block.box(14, 0, 12, 16, 1, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape UP_TOP_RIGHT_DEACTIVATED = Stream.of(
			Block.box(4, 0, 0, 16, 2, 8),
			Block.box(8, 0, 8, 16, 2, 12),
			Block.box(0, 0, 0, 4, 1, 2),
			Block.box(14, 0, 12, 16, 1, 16),
			Block.box(6, 2, 0, 16, 3, 7),
			Block.box(9, 2, 7, 16, 3, 10)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_BOTTOM_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 14, 0, 12, 16, 8),
			Block.box(0, 14, 8, 8, 16, 12),
			Block.box(12, 15, 0, 16, 16, 2),
			Block.box(0, 15, 12, 2, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_BOTTOM_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 14, 0, 12, 16, 8),
			Block.box(0, 14, 8, 8, 16, 12),
			Block.box(12, 15, 0, 16, 16, 2),
			Block.box(0, 15, 12, 2, 16, 16),
			Block.box(0, 13, 0, 10, 14, 7),
			Block.box(0, 13, 7, 7, 14, 10)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_BOTTOM_RIGHT_ACTIVATED = Stream.of(
			Block.box(8, 14, 0, 16, 16, 12),
			Block.box(4, 14, 0, 8, 16, 8),
			Block.box(14, 15, 12, 16, 16, 16),
			Block.box(0, 15, 0, 4, 16, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_BOTTOM_RIGHT_DEACTIVATED = Stream.of(
			Block.box(8, 14, 0, 16, 16, 12),
			Block.box(4, 14, 0, 8, 16, 8),
			Block.box(14, 15, 12, 16, 16, 16),
			Block.box(0, 15, 0, 4, 16, 2),
			Block.box(9, 13, 0, 16, 14, 10),
			Block.box(6, 13, 0, 9, 14, 7)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_TOP_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 14, 4, 8, 16, 16),
			Block.box(8, 14, 8, 12, 16, 16),
			Block.box(0, 15, 0, 2, 16, 4),
			Block.box(12, 15, 14, 16, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_TOP_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 14, 4, 8, 16, 16),
			Block.box(8, 14, 8, 12, 16, 16),
			Block.box(0, 15, 0, 2, 16, 4),
			Block.box(12, 15, 14, 16, 16, 16),
			Block.box(0, 13, 6, 7, 14, 16),
			Block.box(7, 13, 9, 10, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_TOP_RIGHT_ACTIVATED = Stream.of(
			Block.box(4, 14, 8, 16, 16, 16),
			Block.box(8, 14, 4, 16, 16, 8),
			Block.box(0, 15, 14, 4, 16, 16),
			Block.box(14, 15, 0, 16, 16, 4)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape DOWN_TOP_RIGHT_DEACTIVATED = Stream.of(
			Block.box(4, 14, 8, 16, 16, 16),
			Block.box(8, 14, 4, 16, 16, 8),
			Block.box(0, 15, 14, 4, 16, 16),
			Block.box(14, 15, 0, 16, 16, 4),
			Block.box(6, 13, 9, 16, 14, 16),
			Block.box(9, 13, 6, 16, 14, 9)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_BOTTOM_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 8, 14, 12, 16, 16),
			Block.box(0, 4, 14, 8, 8, 16),
			Block.box(12, 14, 15, 16, 16, 16),
			Block.box(0, 0, 15, 2, 4, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_BOTTOM_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 8, 14, 12, 16, 16),
			Block.box(0, 4, 14, 8, 8, 16),
			Block.box(12, 14, 15, 16, 16, 16),
			Block.box(0, 0, 15, 2, 4, 16),
			Block.box(0, 9, 13, 10, 16, 14),
			Block.box(0, 6, 13, 7, 9, 14)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_BOTTOM_RIGHT_ACTIVATED = Stream.of(
			Block.box(8, 4, 14, 16, 16, 16),
			Block.box(4, 8, 14, 8, 16, 16),
			Block.box(14, 0, 15, 16, 4, 16),
			Block.box(0, 14, 15, 4, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_BOTTOM_RIGHT_DEACTIVATED = Stream.of(
			Block.box(8, 4, 14, 16, 16, 16),
			Block.box(4, 8, 14, 8, 16, 16),
			Block.box(14, 0, 15, 16, 4, 16),
			Block.box(0, 14, 15, 4, 16, 16),
			Block.box(9, 6, 13, 16, 16, 14),
			Block.box(6, 9, 13, 9, 16, 14)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_TOP_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 12, 15, 2, 16, 16),
			Block.box(12, 0, 15, 16, 2, 16),
			Block.box(0, 0, 14, 8, 12, 16),
			Block.box(8, 0, 14, 12, 8, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_TOP_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 0, 14, 8, 12, 16),
			Block.box(8, 0, 14, 12, 8, 16),
			Block.box(0, 12, 15, 2, 16, 16),
			Block.box(12, 0, 15, 16, 2, 16),
			Block.box(0, 0, 13, 7, 10, 14),
			Block.box(7, 0, 13, 10, 7, 14)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_TOP_RIGHT_ACTIVATED = Stream.of(
			Block.box(4, 0, 14, 16, 8, 16),
			Block.box(8, 8, 14, 16, 12, 16),
			Block.box(0, 0, 15, 4, 2, 16),
			Block.box(14, 12, 15, 16, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape NORTH_TOP_RIGHT_DEACTIVATED = Stream.of(
			Block.box(4, 0, 14, 16, 8, 16),
			Block.box(8, 8, 14, 16, 12, 16),
			Block.box(0, 0, 15, 4, 2, 16),
			Block.box(14, 12, 15, 16, 16, 16),
			Block.box(6, 0, 13, 16, 7, 14),
			Block.box(9, 7, 13, 16, 10, 14)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_BOTTOM_LEFT_ACTIVATED = Stream.of(
			Block.box(4, 8, 0, 16, 16, 2),
			Block.box(8, 4, 0, 16, 8, 2),
			Block.box(14, 0, 0, 16, 4, 1),
			Block.box(0, 14, 0, 4, 16, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_BOTTOM_LEFT_DEACTIVATED = Stream.of(
			Block.box(4, 8, 0, 16, 16, 2),
			Block.box(8, 4, 0, 16, 8, 2),
			Block.box(0, 14, 0, 4, 16, 1),
			Block.box(14, 0, 0, 16, 4, 1),
			Block.box(6, 9, 2, 16, 16, 3),
			Block.box(9, 6, 2, 16, 9, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_BOTTOM_RIGHT_ACTIVATED = Stream.of(
			Block.box(0, 4, 0, 8, 16, 2),
			Block.box(8, 8, 0, 12, 16, 2),
			Block.box(0, 0, 0, 2, 4, 1),
			Block.box(12, 14, 0, 16, 16, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_BOTTOM_RIGHT_DEACTIVATED = Stream.of(
			Block.box(0, 4, 0, 8, 16, 2),
			Block.box(8, 8, 0, 12, 16, 2),
			Block.box(0, 0, 0, 2, 4, 1),
			Block.box(12, 14, 0, 16, 16, 1),
			Block.box(0, 6, 2, 7, 16, 3),
			Block.box(7, 9, 2, 10, 16, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_TOP_LEFT_ACTIVATED = Stream.of(
			Block.box(8, 0, 0, 16, 12, 2),
			Block.box(4, 0, 0, 8, 8, 2),
			Block.box(14, 12, 0, 16, 16, 1),
			Block.box(0, 0, 0, 4, 2, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_TOP_LEFT_DEACTIVATED = Stream.of(
			Block.box(8, 0, 0, 16, 12, 2),
			Block.box(4, 0, 0, 8, 8, 2),
			Block.box(14, 12, 0, 16, 16, 1),
			Block.box(0, 0, 0, 4, 2, 1),
			Block.box(9, 0, 2, 16, 10, 3),
			Block.box(6, 0, 2, 9, 7, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_TOP_RIGHT_ACTIVATED = Stream.of(
			Block.box(0, 0, 0, 12, 8, 2),
			Block.box(0, 8, 0, 8, 12, 2),
			Block.box(12, 0, 0, 16, 2, 1),
			Block.box(0, 12, 0, 2, 16, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape SOUTH_TOP_RIGHT_DEACTIVATED = Stream.of(
			Block.box(0, 0, 0, 12, 8, 2),
			Block.box(0, 8, 0, 8, 12, 2),
			Block.box(12, 0, 0, 16, 2, 1),
			Block.box(0, 12, 0, 2, 16, 1),
			Block.box(0, 0, 2, 10, 7, 3),
			Block.box(0, 7, 2, 7, 10, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_BOTTOM_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 8, 0, 2, 16, 12),
			Block.box(0, 4, 0, 2, 8, 8),
			Block.box(0, 14, 12, 1, 16, 16),
			Block.box(0, 0, 0, 1, 4, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_BOTTOM_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 8, 0, 2, 16, 12),
			Block.box(0, 4, 0, 2, 8, 8),
			Block.box(0, 14, 12, 1, 16, 16),
			Block.box(0, 0, 0, 1, 4, 2),
			Block.box(2, 9, 0, 3, 16, 10),
			Block.box(2, 6, 0, 3, 9, 7)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_BOTTOM_RIGHT_ACTIVATED = Stream.of(
			Block.box(0, 4, 8, 2, 16, 16),
			Block.box(0, 8, 4, 2, 16, 8),
			Block.box(0, 0, 14, 1, 4, 16),
			Block.box(0, 14, 0, 1, 16, 4)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_BOTTOM_RIGHT_DEACTIVATED = Stream.of(
			Block.box(0, 4, 8, 2, 16, 16),
			Block.box(0, 8, 4, 2, 16, 8),
			Block.box(0, 0, 14, 1, 4, 16),
			Block.box(0, 14, 0, 1, 16, 4),
			Block.box(2, 6, 9, 3, 16, 16),
			Block.box(2, 9, 6, 3, 16, 9)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_TOP_LEFT_ACTIVATED = Stream.of(
			Block.box(0, 0, 0, 2, 12, 8),
			Block.box(0, 0, 8, 2, 8, 12),
			Block.box(0, 12, 0, 1, 16, 2),
			Block.box(0, 0, 12, 1, 2, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_TOP_LEFT_DEACTIVATED = Stream.of(
			Block.box(0, 0, 0, 2, 12, 8),
			Block.box(0, 0, 8, 2, 8, 12),
			Block.box(0, 12, 0, 1, 16, 2),
			Block.box(0, 0, 12, 1, 2, 16),
			Block.box(2, 0, 0, 3, 10, 7),
			Block.box(2, 0, 7, 3, 7, 10)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_TOP_RIGHT_ACTIVATED = Stream.of(
			Block.box(0, 0, 4, 2, 8, 16),
			Block.box(0, 8, 8, 2, 12, 16),
			Block.box(0, 0, 0, 1, 2, 4),
			Block.box(0, 12, 14, 1, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape EAST_TOP_RIGHT_DEACTIVATED = Stream.of(
			Block.box(0, 0, 4, 2, 8, 16),
			Block.box(0, 8, 8, 2, 12, 16),
			Block.box(0, 0, 0, 1, 2, 4),
			Block.box(0, 12, 14, 1, 16, 16),
			Block.box(2, 0, 6, 3, 7, 16),
			Block.box(2, 7, 9, 3, 10, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_BOTTOM_LEFT_ACTIVATED = Stream.of(
			Block.box(14, 8, 4, 16, 16, 16),
			Block.box(14, 4, 8, 16, 8, 16),
			Block.box(15, 14, 0, 16, 16, 4),
			Block.box(15, 0, 14, 16, 4, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_BOTTOM_LEFT_DEACTIVATED = Stream.of(
			Block.box(14, 8, 4, 16, 16, 16),
			Block.box(14, 4, 8, 16, 8, 16),
			Block.box(15, 14, 0, 16, 16, 4),
			Block.box(15, 0, 14, 16, 4, 16),
			Block.box(13, 9, 6, 14, 16, 16),
			Block.box(13, 6, 9, 14, 9, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_BOTTOM_RIGHT_ACTIVATED = Stream.of(
			Block.box(14, 4, 0, 16, 16, 8),
			Block.box(14, 8, 8, 16, 16, 12),
			Block.box(15, 0, 0, 16, 4, 2),
			Block.box(15, 14, 12, 16, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_BOTTOM_RIGHT_DEACTIVATED = Stream.of(
			Block.box(14, 4, 0, 16, 16, 8),
			Block.box(14, 8, 8, 16, 16, 12),
			Block.box(15, 0, 0, 16, 4, 2),
			Block.box(15, 14, 12, 16, 16, 16),
			Block.box(13, 6, 0, 14, 16, 7),
			Block.box(13, 9, 7, 14, 16, 10)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_TOP_LEFT_ACTIVATED = Stream.of(
			Block.box(14, 0, 8, 16, 12, 16),
			Block.box(14, 0, 4, 16, 8, 8),
			Block.box(15, 12, 14, 16, 16, 16),
			Block.box(15, 0, 0, 16, 2, 4)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_TOP_LEFT_DEACTIVATED = Stream.of(
			Block.box(14, 0, 8, 16, 12, 16),
			Block.box(14, 0, 4, 16, 8, 8),
			Block.box(15, 12, 14, 16, 16, 16),
			Block.box(15, 0, 0, 16, 2, 4),
			Block.box(13, 0, 9, 14, 10, 16),
			Block.box(13, 0, 6, 14, 7, 9)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_TOP_RIGHT_ACTIVATED = Stream.of(
			Block.box(14, 0, 0, 16, 8, 12),
			Block.box(14, 8, 0, 16, 12, 8),
			Block.box(15, 0, 12, 16, 2, 16),
			Block.box(15, 12, 0, 16, 16, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	private static final VoxelShape WEST_TOP_RIGHT_DEACTIVATED = Stream.of(
			Block.box(14, 0, 0, 16, 8, 12),
			Block.box(14, 8, 0, 16, 12, 8),
			Block.box(15, 0, 12, 16, 2, 16),
			Block.box(15, 12, 0, 16, 16, 2),
			Block.box(13, 0, 0, 14, 7, 10),
			Block.box(13, 7, 0, 14, 10, 7)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
}
