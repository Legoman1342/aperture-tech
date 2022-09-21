package com.Legoman1342.blocks.custom;

import com.Legoman1342.blocks.BlockRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CatwalkStairs extends Block {

	//Creating block state properties
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;


	//Constructor
	public CatwalkStairs(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, HALF);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Direction direction = pContext.getHorizontalDirection();
		BlockPos blockPos = pContext.getClickedPos();
		Level level = pContext.getLevel();
		Direction face = pContext.getClickedFace();
		Half half = !(pContext.getClickLocation().y - (double)blockPos.getY() > 0.5D) ? Half.BOTTOM : Half.TOP;
		switch (face) {
			case UP:
				if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, direction.getOpposite())
						.setValue(HALF, DoubleBlockHalf.LOWER);
			} else {
				return null;
			}
			case DOWN:
				if (blockPos.getY() > level.getMinBuildHeight() + 1 && level.getBlockState(blockPos.below()).canBeReplaced(pContext)) {
					return this.defaultBlockState()
							.setValue(FACING, direction.getOpposite())
							.setValue(HALF, DoubleBlockHalf.UPPER);
				} else {
					return null;
				}
			default:
				switch (half) {
					case TOP:
						if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(pContext)) {
							return this.defaultBlockState()
									.setValue(FACING, direction)
									.setValue(HALF, DoubleBlockHalf.LOWER);
						} else if (blockPos.getY() > level.getMinBuildHeight() + 1 && level.getBlockState(blockPos.below()).canBeReplaced(pContext)) {
							return this.defaultBlockState()
									.setValue(FACING, direction.getOpposite())
									.setValue(HALF, DoubleBlockHalf.UPPER);
						} else {
							return null;
						}
					case BOTTOM:
						if (blockPos.getY() > level.getMinBuildHeight() + 1 && level.getBlockState(blockPos.below()).canBeReplaced(pContext)) {
							return this.defaultBlockState()
									.setValue(FACING, direction.getOpposite())
									.setValue(HALF, DoubleBlockHalf.UPPER);
						} else if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(pContext)) {
							return this.defaultBlockState()
									.setValue(FACING, direction)
									.setValue(HALF, DoubleBlockHalf.LOWER);
						} else {
							return null;
						}
				}
		}
		return null; //Should never be reached
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
		switch (pState.getValue(HALF)) {
			case LOWER -> pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
			case UPPER -> pLevel.setBlock(pPos.below(), pState.setValue(HALF, DoubleBlockHalf.LOWER), 3);
		}
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		BlockState connectedBlock;
		switch (pState.getValue(HALF)) {
			case LOWER -> {
				connectedBlock = pLevel.getBlockState(pCurrentPos.above());
				if (connectedBlock.getBlock() == BlockRegistration.CATWALK_STAIRS.get()
						&& connectedBlock.getValue(FACING) == pState.getValue(FACING)
						&& connectedBlock.getValue(HALF) == DoubleBlockHalf.UPPER) {
					return pState;
				} else {
					pLevel.setBlock(pCurrentPos, Blocks.AIR.defaultBlockState(), 35);
					return Blocks.AIR.defaultBlockState();
				}
			}
			case UPPER -> {
				connectedBlock = pLevel.getBlockState(pCurrentPos.below());
				if (connectedBlock.getBlock() == BlockRegistration.CATWALK_STAIRS.get()
						&& connectedBlock.getValue(FACING) == pState.getValue(FACING)
						&& connectedBlock.getValue(HALF) == DoubleBlockHalf.LOWER) {
					return pState;
				} else {
					pLevel.setBlock(pCurrentPos, Blocks.AIR.defaultBlockState(), 35);
					return Blocks.AIR.defaultBlockState();
				}
			}
		}
		return pState;
	}

	/**
	 * Gets the VoxelShape for the current block state.
	 * @return A VoxelShape used as the block's hitbox and collision box
	 */
	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction facing = pState.getValue(FACING);
		DoubleBlockHalf half = pState.getValue(HALF);

		return switch (half) {
			case LOWER -> switch (facing) {
				case NORTH -> BOTTOM_NORTH;
				case EAST -> BOTTOM_EAST;
				case SOUTH -> BOTTOM_SOUTH;
				case WEST -> BOTTOM_WEST;
				default -> null;
			};
			case UPPER -> switch (facing) {
				case NORTH -> TOP_NORTH;
				case EAST -> TOP_EAST;
				case SOUTH -> TOP_SOUTH;
				case WEST -> TOP_WEST;
				default -> null;
			};
		};
	}

	public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		if (!pLevel.isClientSide && pPlayer.isCreative()) {
			preventCreativeDropFromBottomPart(pLevel, pPos, pState, pPlayer);
		}
		super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
	}

	/**
	 Used to make sure that only one item is dropped when catwalk stairs are broken. <br>
	 Code from {@link net.minecraft.world.level.block.DoublePlantBlock#preventCreativeDropFromBottomPart(Level, BlockPos, BlockState, Player) net.minecraft.world.level.block.DoublePlantBlock#preventCreativeDropFromBottomPart}
	*/
	protected static void preventCreativeDropFromBottomPart(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		DoubleBlockHalf doubleblockhalf = pState.getValue(HALF);
		if (doubleblockhalf == DoubleBlockHalf.UPPER) {
			BlockPos blockpos = pPos.below();
			BlockState blockstate = pLevel.getBlockState(blockpos);
			if (blockstate.is(pState.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
				BlockState blockstate1 = blockstate.hasProperty(BlockStateProperties.WATERLOGGED) && blockstate.getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				pLevel.setBlock(blockpos, blockstate1, 35);
				pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
			}
		}

	}


	//Defining VoxelShapes used for different block states
	private static final VoxelShape BOTTOM_NORTH = Stream.of(
			Block.box(0, 0, 0, 16, 5, 1),
			Block.box(0, 1, 1, 16, 5, 2),
			Block.box(0, 2, 2, 16, 5, 3),
			Block.box(0, 3, 3, 16, 5, 4),
			Block.box(0, 4, 4, 16, 9, 5),
			Block.box(0, 5, 5, 16, 9, 6),
			Block.box(0, 6, 6, 16, 9, 7),
			Block.box(0, 7, 7, 16, 9, 8),
			Block.box(0, 8, 8, 16, 13, 9),
			Block.box(0, 9, 9, 16, 13, 10),
			Block.box(0, 10, 10, 16, 13, 11),
			Block.box(0, 11, 11, 16, 13, 12),
			Block.box(0, 12, 12, 16, 16, 13),
			Block.box(0, 13, 13, 16, 16, 14),
			Block.box(0, 14, 14, 16, 16, 15),
			Block.box(0, 15, 15, 16, 16, 16),
			Block.box(0, 5, 0, 1, 15, 1),
			Block.box(0, 5, 1, 1, 16, 4),
			Block.box(0, 9, 4, 1, 16, 8),
			Block.box(0, 13, 8, 1, 16, 12),
			Block.box(15, 5, 0, 16, 15, 1),
			Block.box(15, 5, 1, 16, 16, 4),
			Block.box(15, 9, 4, 16, 16, 8),
			Block.box(15, 13, 8, 16, 16, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_EAST = Stream.of(
			Block.box(15, 0, 0, 16, 5, 16),
			Block.box(14, 1, 0, 15, 5, 16),
			Block.box(13, 2, 0, 14, 5, 16),
			Block.box(12, 3, 0, 13, 5, 16),
			Block.box(11, 4, 0, 12, 9, 16),
			Block.box(10, 5, 0, 11, 9, 16),
			Block.box(9, 6, 0, 10, 9, 16),
			Block.box(8, 7, 0, 9, 9, 16),
			Block.box(7, 8, 0, 8, 13, 16),
			Block.box(6, 9, 0, 7, 13, 16),
			Block.box(5, 10, 0, 6, 13, 16),
			Block.box(4, 11, 0, 5, 13, 16),
			Block.box(3, 12, 0, 4, 16, 16),
			Block.box(2, 13, 0, 3, 16, 16),
			Block.box(1, 14, 0, 2, 16, 16),
			Block.box(0, 15, 0, 1, 16, 16),
			Block.box(15, 5, 0, 16, 15, 1),
			Block.box(12, 5, 0, 15, 16, 1),
			Block.box(8, 9, 0, 12, 16, 1),
			Block.box(4, 13, 0, 8, 16, 1),
			Block.box(15, 5, 15, 16, 15, 16),
			Block.box(12, 5, 15, 15, 16, 16),
			Block.box(8, 9, 15, 12, 16, 16),
			Block.box(4, 13, 15, 8, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_SOUTH = Stream.of(
			Block.box(0, 0, 15, 16, 5, 16),
			Block.box(0, 1, 14, 16, 5, 15),
			Block.box(0, 2, 13, 16, 5, 14),
			Block.box(0, 3, 12, 16, 5, 13),
			Block.box(0, 4, 11, 16, 9, 12),
			Block.box(0, 5, 10, 16, 9, 11),
			Block.box(0, 6, 9, 16, 9, 10),
			Block.box(0, 7, 8, 16, 9, 9),
			Block.box(0, 8, 7, 16, 13, 8),
			Block.box(0, 9, 6, 16, 13, 7),
			Block.box(0, 10, 5, 16, 13, 6),
			Block.box(0, 11, 4, 16, 13, 5),
			Block.box(0, 12, 3, 16, 16, 4),
			Block.box(0, 13, 2, 16, 16, 3),
			Block.box(0, 14, 1, 16, 16, 2),
			Block.box(0, 15, 0, 16, 16, 1),
			Block.box(15, 5, 15, 16, 15, 16),
			Block.box(15, 5, 12, 16, 16, 15),
			Block.box(15, 9, 8, 16, 16, 12),
			Block.box(15, 13, 4, 16, 16, 8),
			Block.box(0, 5, 15, 1, 15, 16),
			Block.box(0, 5, 12, 1, 16, 15),
			Block.box(0, 9, 8, 1, 16, 12),
			Block.box(0, 13, 4, 1, 16, 8)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_WEST = Stream.of(
			Block.box(0, 0, 0, 1, 5, 16),
			Block.box(1, 1, 0, 2, 5, 16),
			Block.box(2, 2, 0, 3, 5, 16),
			Block.box(3, 3, 0, 4, 5, 16),
			Block.box(4, 4, 0, 5, 9, 16),
			Block.box(5, 5, 0, 6, 9, 16),
			Block.box(6, 6, 0, 7, 9, 16),
			Block.box(7, 7, 0, 8, 9, 16),
			Block.box(8, 8, 0, 9, 13, 16),
			Block.box(9, 9, 0, 10, 13, 16),
			Block.box(10, 10, 0, 11, 13, 16),
			Block.box(11, 11, 0, 12, 13, 16),
			Block.box(12, 12, 0, 13, 16, 16),
			Block.box(13, 13, 0, 14, 16, 16),
			Block.box(14, 14, 0, 15, 16, 16),
			Block.box(15, 15, 0, 16, 16, 16),
			Block.box(0, 5, 15, 1, 15, 16),
			Block.box(1, 5, 15, 4, 16, 16),
			Block.box(4, 9, 15, 8, 16, 16),
			Block.box(8, 13, 15, 12, 16, 16),
			Block.box(0, 5, 0, 1, 15, 1),
			Block.box(1, 5, 0, 4, 16, 1),
			Block.box(4, 9, 0, 8, 16, 1),
			Block.box(8, 13, 0, 12, 16, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_NORTH = Stream.of(
			Block.box(0, 0, 15, 1, 14, 16),
			Block.box(0, 0, 14, 1, 13, 15),
			Block.box(0, 0, 13, 1, 12, 14),
			Block.box(0, 0, 12, 1, 11, 13),
			Block.box(0, 0, 11, 1, 10, 12),
			Block.box(0, 0, 10, 1, 9, 11),
			Block.box(0, 0, 9, 1, 8, 10),
			Block.box(0, 0, 8, 1, 7, 9),
			Block.box(0, 0, 7, 1, 6, 8),
			Block.box(0, 0, 6, 1, 5, 7),
			Block.box(0, 0, 5, 1, 4, 6),
			Block.box(0, 0, 4, 1, 3, 5),
			Block.box(0, 0, 3, 1, 2, 4),
			Block.box(0, 0, 2, 1, 1, 3),
			Block.box(1, 0, 12, 15, 1, 16),
			Block.box(15, 0, 15, 16, 14, 16),
			Block.box(15, 0, 14, 16, 13, 15),
			Block.box(15, 0, 13, 16, 12, 14),
			Block.box(15, 0, 12, 16, 11, 13),
			Block.box(15, 0, 11, 16, 10, 12),
			Block.box(15, 0, 10, 16, 9, 11),
			Block.box(15, 0, 9, 16, 8, 10),
			Block.box(15, 0, 8, 16, 7, 9),
			Block.box(15, 0, 7, 16, 6, 8),
			Block.box(15, 0, 6, 16, 5, 7),
			Block.box(15, 0, 5, 16, 4, 6),
			Block.box(15, 0, 4, 16, 3, 5),
			Block.box(15, 0, 3, 16, 2, 4),
			Block.box(15, 0, 2, 16, 1, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_EAST = Stream.of(
			Block.box(0, 0, 0, 1, 14, 1),
			Block.box(1, 0, 0, 2, 13, 1),
			Block.box(2, 0, 0, 3, 12, 1),
			Block.box(3, 0, 0, 4, 11, 1),
			Block.box(4, 0, 0, 5, 10, 1),
			Block.box(5, 0, 0, 6, 9, 1),
			Block.box(6, 0, 0, 7, 8, 1),
			Block.box(7, 0, 0, 8, 7, 1),
			Block.box(8, 0, 0, 9, 6, 1),
			Block.box(9, 0, 0, 10, 5, 1),
			Block.box(10, 0, 0, 11, 4, 1),
			Block.box(11, 0, 0, 12, 3, 1),
			Block.box(12, 0, 0, 13, 2, 1),
			Block.box(13, 0, 0, 14, 1, 1),
			Block.box(0, 0, 1, 4, 1, 15),
			Block.box(0, 0, 15, 1, 14, 16),
			Block.box(1, 0, 15, 2, 13, 16),
			Block.box(2, 0, 15, 3, 12, 16),
			Block.box(3, 0, 15, 4, 11, 16),
			Block.box(4, 0, 15, 5, 10, 16),
			Block.box(5, 0, 15, 6, 9, 16),
			Block.box(6, 0, 15, 7, 8, 16),
			Block.box(7, 0, 15, 8, 7, 16),
			Block.box(8, 0, 15, 9, 6, 16),
			Block.box(9, 0, 15, 10, 5, 16),
			Block.box(10, 0, 15, 11, 4, 16),
			Block.box(11, 0, 15, 12, 3, 16),
			Block.box(12, 0, 15, 13, 2, 16),
			Block.box(13, 0, 15, 14, 1, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_SOUTH = Stream.of(
			Block.box(15, 0, 0, 16, 14, 1),
			Block.box(15, 0, 1, 16, 13, 2),
			Block.box(15, 0, 2, 16, 12, 3),
			Block.box(15, 0, 3, 16, 11, 4),
			Block.box(15, 0, 4, 16, 10, 5),
			Block.box(15, 0, 5, 16, 9, 6),
			Block.box(15, 0, 6, 16, 8, 7),
			Block.box(15, 0, 7, 16, 7, 8),
			Block.box(15, 0, 8, 16, 6, 9),
			Block.box(15, 0, 9, 16, 5, 10),
			Block.box(15, 0, 10, 16, 4, 11),
			Block.box(15, 0, 11, 16, 3, 12),
			Block.box(15, 0, 12, 16, 2, 13),
			Block.box(15, 0, 13, 16, 1, 14),
			Block.box(1, 0, 0, 15, 1, 4),
			Block.box(0, 0, 0, 1, 14, 1),
			Block.box(0, 0, 1, 1, 13, 2),
			Block.box(0, 0, 2, 1, 12, 3),
			Block.box(0, 0, 3, 1, 11, 4),
			Block.box(0, 0, 4, 1, 10, 5),
			Block.box(0, 0, 5, 1, 9, 6),
			Block.box(0, 0, 6, 1, 8, 7),
			Block.box(0, 0, 7, 1, 7, 8),
			Block.box(0, 0, 8, 1, 6, 9),
			Block.box(0, 0, 9, 1, 5, 10),
			Block.box(0, 0, 10, 1, 4, 11),
			Block.box(0, 0, 11, 1, 3, 12),
			Block.box(0, 0, 12, 1, 2, 13),
			Block.box(0, 0, 13, 1, 1, 14)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_WEST = Stream.of(
			Block.box(15, 0, 15, 16, 14, 16),
			Block.box(14, 0, 15, 15, 13, 16),
			Block.box(13, 0, 15, 14, 12, 16),
			Block.box(12, 0, 15, 13, 11, 16),
			Block.box(11, 0, 15, 12, 10, 16),
			Block.box(10, 0, 15, 11, 9, 16),
			Block.box(9, 0, 15, 10, 8, 16),
			Block.box(8, 0, 15, 9, 7, 16),
			Block.box(7, 0, 15, 8, 6, 16),
			Block.box(6, 0, 15, 7, 5, 16),
			Block.box(5, 0, 15, 6, 4, 16),
			Block.box(4, 0, 15, 5, 3, 16),
			Block.box(3, 0, 15, 4, 2, 16),
			Block.box(2, 0, 15, 3, 1, 16),
			Block.box(12, 0, 1, 16, 1, 15),
			Block.box(15, 0, 0, 16, 14, 1),
			Block.box(14, 0, 0, 15, 13, 1),
			Block.box(13, 0, 0, 14, 12, 1),
			Block.box(12, 0, 0, 13, 11, 1),
			Block.box(11, 0, 0, 12, 10, 1),
			Block.box(10, 0, 0, 11, 9, 1),
			Block.box(9, 0, 0, 10, 8, 1),
			Block.box(8, 0, 0, 9, 7, 1),
			Block.box(7, 0, 0, 8, 6, 1),
			Block.box(6, 0, 0, 7, 5, 1),
			Block.box(5, 0, 0, 6, 4, 1),
			Block.box(4, 0, 0, 5, 3, 1),
			Block.box(3, 0, 0, 4, 2, 1),
			Block.box(2, 0, 0, 3, 1, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

}
