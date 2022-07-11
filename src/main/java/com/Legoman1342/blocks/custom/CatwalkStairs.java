package com.Legoman1342.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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

	//Defining VoxelShapes used for different block states
	private static final VoxelShape BOTTOM_NORTH = Stream.of(
			Block.box(0, 0, 0, 1, 15, 1),
			Block.box(0, 1, 1, 1, 16, 2),
			Block.box(0, 2, 2, 1, 16, 3),
			Block.box(0, 3, 3, 1, 16, 4),
			Block.box(0, 4, 4, 1, 16, 5),
			Block.box(0, 5, 5, 1, 16, 6),
			Block.box(0, 6, 6, 1, 16, 7),
			Block.box(0, 7, 7, 1, 16, 8),
			Block.box(0, 8, 8, 1, 16, 9),
			Block.box(0, 9, 9, 1, 16, 10),
			Block.box(0, 10, 10, 1, 16, 11),
			Block.box(0, 11, 11, 1, 16, 12),
			Block.box(0, 12, 12, 1, 16, 13),
			Block.box(0, 13, 13, 1, 16, 14),
			Block.box(0, 14, 14, 1, 16, 15),
			Block.box(0, 15, 15, 1, 16, 16),
			Block.box(1, 4, 0, 15, 5, 4),
			Block.box(1, 8, 4, 15, 9, 8),
			Block.box(1, 12, 8, 15, 13, 12),
			Block.box(15, 0, 0, 16, 15, 1),
			Block.box(15, 1, 1, 16, 16, 2),
			Block.box(15, 2, 2, 16, 16, 3),
			Block.box(15, 3, 3, 16, 16, 4),
			Block.box(15, 4, 4, 16, 16, 5),
			Block.box(15, 5, 5, 16, 16, 6),
			Block.box(15, 6, 6, 16, 16, 7),
			Block.box(15, 7, 7, 16, 16, 8),
			Block.box(15, 8, 8, 16, 16, 9),
			Block.box(15, 9, 9, 16, 16, 10),
			Block.box(15, 10, 10, 16, 16, 11),
			Block.box(15, 11, 11, 16, 16, 12),
			Block.box(15, 12, 12, 16, 16, 13),
			Block.box(15, 13, 13, 16, 16, 14),
			Block.box(15, 14, 14, 16, 16, 15),
			Block.box(15, 15, 15, 16, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_EAST = Stream.of(
			Block.box(15, 0, 0, 16, 15, 1),
			Block.box(14, 1, 0, 15, 16, 1),
			Block.box(13, 2, 0, 14, 16, 1),
			Block.box(12, 3, 0, 13, 16, 1),
			Block.box(11, 4, 0, 12, 16, 1),
			Block.box(10, 5, 0, 11, 16, 1),
			Block.box(9, 6, 0, 10, 16, 1),
			Block.box(8, 7, 0, 9, 16, 1),
			Block.box(7, 8, 0, 8, 16, 1),
			Block.box(6, 9, 0, 7, 16, 1),
			Block.box(5, 10, 0, 6, 16, 1),
			Block.box(4, 11, 0, 5, 16, 1),
			Block.box(3, 12, 0, 4, 16, 1),
			Block.box(2, 13, 0, 3, 16, 1),
			Block.box(1, 14, 0, 2, 16, 1),
			Block.box(0, 15, 0, 1, 16, 1),
			Block.box(12, 4, 1, 16, 5, 15),
			Block.box(8, 8, 1, 12, 9, 15),
			Block.box(4, 12, 1, 8, 13, 15),
			Block.box(15, 0, 15, 16, 15, 16),
			Block.box(14, 1, 15, 15, 16, 16),
			Block.box(13, 2, 15, 14, 16, 16),
			Block.box(12, 3, 15, 13, 16, 16),
			Block.box(11, 4, 15, 12, 16, 16),
			Block.box(10, 5, 15, 11, 16, 16),
			Block.box(9, 6, 15, 10, 16, 16),
			Block.box(8, 7, 15, 9, 16, 16),
			Block.box(7, 8, 15, 8, 16, 16),
			Block.box(6, 9, 15, 7, 16, 16),
			Block.box(5, 10, 15, 6, 16, 16),
			Block.box(4, 11, 15, 5, 16, 16),
			Block.box(3, 12, 15, 4, 16, 16),
			Block.box(2, 13, 15, 3, 16, 16),
			Block.box(1, 14, 15, 2, 16, 16),
			Block.box(0, 15, 15, 1, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_SOUTH = Stream.of(
			Block.box(15, 0, 15, 16, 15, 16),
			Block.box(15, 1, 14, 16, 16, 15),
			Block.box(15, 2, 13, 16, 16, 14),
			Block.box(15, 3, 12, 16, 16, 13),
			Block.box(15, 4, 11, 16, 16, 12),
			Block.box(15, 5, 10, 16, 16, 11),
			Block.box(15, 6, 9, 16, 16, 10),
			Block.box(15, 7, 8, 16, 16, 9),
			Block.box(15, 8, 7, 16, 16, 8),
			Block.box(15, 9, 6, 16, 16, 7),
			Block.box(15, 10, 5, 16, 16, 6),
			Block.box(15, 11, 4, 16, 16, 5),
			Block.box(15, 12, 3, 16, 16, 4),
			Block.box(15, 13, 2, 16, 16, 3),
			Block.box(15, 14, 1, 16, 16, 2),
			Block.box(15, 15, 0, 16, 16, 1),
			Block.box(1, 4, 12, 15, 5, 16),
			Block.box(1, 8, 8, 15, 9, 12),
			Block.box(1, 12, 4, 15, 13, 8),
			Block.box(0, 0, 15, 1, 15, 16),
			Block.box(0, 1, 14, 1, 16, 15),
			Block.box(0, 2, 13, 1, 16, 14),
			Block.box(0, 3, 12, 1, 16, 13),
			Block.box(0, 4, 11, 1, 16, 12),
			Block.box(0, 5, 10, 1, 16, 11),
			Block.box(0, 6, 9, 1, 16, 10),
			Block.box(0, 7, 8, 1, 16, 9),
			Block.box(0, 8, 7, 1, 16, 8),
			Block.box(0, 9, 6, 1, 16, 7),
			Block.box(0, 10, 5, 1, 16, 6),
			Block.box(0, 11, 4, 1, 16, 5),
			Block.box(0, 12, 3, 1, 16, 4),
			Block.box(0, 13, 2, 1, 16, 3),
			Block.box(0, 14, 1, 1, 16, 2),
			Block.box(0, 15, 0, 1, 16, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_WEST = Stream.of(
			Block.box(0, 0, 15, 1, 15, 16),
			Block.box(1, 1, 15, 2, 16, 16),
			Block.box(2, 2, 15, 3, 16, 16),
			Block.box(3, 3, 15, 4, 16, 16),
			Block.box(4, 4, 15, 5, 16, 16),
			Block.box(5, 5, 15, 6, 16, 16),
			Block.box(6, 6, 15, 7, 16, 16),
			Block.box(7, 7, 15, 8, 16, 16),
			Block.box(8, 8, 15, 9, 16, 16),
			Block.box(9, 9, 15, 10, 16, 16),
			Block.box(10, 10, 15, 11, 16, 16),
			Block.box(11, 11, 15, 12, 16, 16),
			Block.box(12, 12, 15, 13, 16, 16),
			Block.box(13, 13, 15, 14, 16, 16),
			Block.box(14, 14, 15, 15, 16, 16),
			Block.box(15, 15, 15, 16, 16, 16),
			Block.box(0, 4, 1, 4, 5, 15),
			Block.box(4, 8, 1, 8, 9, 15),
			Block.box(8, 12, 1, 12, 13, 15),
			Block.box(0, 0, 0, 1, 15, 1),
			Block.box(1, 1, 0, 2, 16, 1),
			Block.box(2, 2, 0, 3, 16, 1),
			Block.box(3, 3, 0, 4, 16, 1),
			Block.box(4, 4, 0, 5, 16, 1),
			Block.box(5, 5, 0, 6, 16, 1),
			Block.box(6, 6, 0, 7, 16, 1),
			Block.box(7, 7, 0, 8, 16, 1),
			Block.box(8, 8, 0, 9, 16, 1),
			Block.box(9, 9, 0, 10, 16, 1),
			Block.box(10, 10, 0, 11, 16, 1),
			Block.box(11, 11, 0, 12, 16, 1),
			Block.box(12, 12, 0, 13, 16, 1),
			Block.box(13, 13, 0, 14, 16, 1),
			Block.box(14, 14, 0, 15, 16, 1),
			Block.box(15, 15, 0, 16, 16, 1)
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
