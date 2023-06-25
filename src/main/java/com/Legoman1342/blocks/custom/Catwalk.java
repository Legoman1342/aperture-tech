package com.Legoman1342.blocks.custom;

import com.Legoman1342.utilities.Lang;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class Catwalk extends Block {
	
	//Creating block state properties
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<ATCatwalkSides> CATWALK_LEFT = EnumProperty.create("left", ATCatwalkSides.class);
	public static final EnumProperty<ATCatwalkSides> CATWALK_RIGHT = EnumProperty.create("right", ATCatwalkSides.class);
	public static final EnumProperty<ATCatwalkEnd> CATWALK_END = EnumProperty.create("end", ATCatwalkEnd.class);
	

	//Defining the enums used in the left, right, and end properties
	public enum ATCatwalkSides implements StringRepresentable {
		RAILING, ATTACH, ATTACH_FLIPPED,; //Possible shapes for the left and right of catwalks

		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}
	public enum ATCatwalkEnd implements StringRepresentable {
		DROP, RAILING, ATTACH,; //Possible shapes for the end of catwalks

		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}


	
	//Constructor
	public Catwalk(Properties pProperties) {
		super(pProperties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, CATWALK_LEFT, CATWALK_RIGHT, CATWALK_END);
	}

	/**
	 * Defines the state the block will be in when placed.
	 * @param context Information about the placement, including player information and facing direction
	 * @return The BlockState the catwalk will have when placed
	 */
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection();
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		BlockState frontNeighbor = level.getBlockState(pos.relative(direction.getOpposite()));
		BlockState leftNeighbor = level.getBlockState(pos.relative(direction.getClockWise()));
		BlockState rightNeighbor = level.getBlockState(pos.relative(direction.getCounterClockWise()));
		return this.defaultBlockState()
				.setValue(FACING, direction)
				.setValue(CATWALK_END, getEndState(direction, frontNeighbor))
				.setValue(CATWALK_LEFT, getLeftState(direction, leftNeighbor))
				.setValue(CATWALK_RIGHT, getRightState(direction, rightNeighbor));
	}

	/**
	 * Updates the state of the catwalk when a block update occurs.
	 * @return The new BlockState
	 */
	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		Direction facing = pState.getValue(FACING);
		ATCatwalkEnd end = pState.getValue(CATWALK_END);

		if (pDirection == facing.getOpposite()) {
			if (end != ATCatwalkEnd.DROP) {
				return pState.setValue(CATWALK_END, getEndState(facing, pNeighborState));
			} else if (pNeighborState.getBlock() instanceof Catwalk
					|| pNeighborState.getBlock() instanceof CatwalkStairs) {
				return pState.setValue(CATWALK_END, getEndState(facing, pNeighborState));
			}
		} else if (pDirection == facing.getClockWise()) {
			if (end != ATCatwalkEnd.DROP) {
				return pState.setValue(CATWALK_LEFT, getLeftState(facing, pNeighborState));
			}
		} else if (pDirection == facing.getCounterClockWise()) {
			if (end != ATCatwalkEnd.DROP) {
				return pState.setValue(CATWALK_RIGHT, getRightState(facing, pNeighborState));
			}
		}
		return pState;
	}

	/**
	 * Convenience method used in {@link #getStateForPlacement(BlockPlaceContext) getStateForPlacement} and {@link #updateShape(BlockState, Direction, BlockState, LevelAccessor, BlockPos, BlockPos)  updateShape}.
	 * @param direction The direction that this catwalk is facing
	 * @param neighbor The blockstate of the front neighbor
	 * @return The appropriate state for the end of the catwalk
	 */
	public ATCatwalkEnd getEndState(Direction direction, BlockState neighbor) {
		if (neighbor.getBlock() instanceof Catwalk) {
			if (neighbor.getValue(FACING) != direction.getOpposite()) {
				return ATCatwalkEnd.ATTACH;
			} else {
				return ATCatwalkEnd.RAILING;
			}
		} else if (neighbor.getBlock() instanceof CatwalkStairs) {
			if ((neighbor.getValue(CatwalkStairs.FACING) == direction
					&& neighbor.getValue(CatwalkStairs.HALF) == DoubleBlockHalf.LOWER)
					|| (neighbor.getValue(CatwalkStairs.FACING) == direction.getOpposite()
					&& neighbor.getValue(CatwalkStairs.HALF) == DoubleBlockHalf.UPPER)) {
				return ATCatwalkEnd.ATTACH;
			} else {
				return ATCatwalkEnd.RAILING;
			}
		} else {
			return ATCatwalkEnd.RAILING;
		}
	}

	/**
	 * Convenience method used in {@link #getStateForPlacement(BlockPlaceContext) getStateForPlacement} and {@link #updateShape(BlockState, Direction, BlockState, LevelAccessor, BlockPos, BlockPos)  updateShape}.
	 * @param direction The direction that this catwalk is facing
	 * @param neighbor The blockstate of the left neighbor
	 * @return The appropriate state for the left of the catwalk
	 */
	public ATCatwalkSides getLeftState(Direction direction, BlockState neighbor) {
		if (neighbor.getBlock() instanceof Catwalk) {
			if (neighbor.getValue(FACING) == direction.getCounterClockWise()) {
				return ATCatwalkSides.ATTACH;
			} else if (neighbor.getValue(FACING) == direction.getClockWise()) {
				return ATCatwalkSides.ATTACH_FLIPPED;
			} else {
				return ATCatwalkSides.RAILING;
			}
		} else if (neighbor.getBlock() instanceof CatwalkStairs) {
			if (neighbor.getValue(CatwalkStairs.HALF) == DoubleBlockHalf.LOWER
					&& neighbor.getValue(CatwalkStairs.FACING) == direction.getCounterClockWise()) {
				return ATCatwalkSides.ATTACH;
			} else if (neighbor.getValue(CatwalkStairs.HALF) == DoubleBlockHalf.UPPER
					&& neighbor.getValue(CatwalkStairs.FACING) == direction.getClockWise()) {
				return ATCatwalkSides.ATTACH_FLIPPED;
			} else {
				return ATCatwalkSides.RAILING;
			}
		} else {
			return ATCatwalkSides.RAILING;
		}
	}

	/**
	 * Convenience method used in {@link #getStateForPlacement(BlockPlaceContext) getStateForPlacement} and {@link #updateShape(BlockState, Direction, BlockState, LevelAccessor, BlockPos, BlockPos)  updateShape}.
	 * @param direction The direction that this catwalk is facing
	 * @param neighbor The blockstate of the right neighbor
	 * @return The appropriate state for the right of the catwalk
	 */
	public ATCatwalkSides getRightState(Direction direction, BlockState neighbor) {
		if (neighbor.getBlock() instanceof Catwalk) {
			if (neighbor.getValue(FACING) == direction.getClockWise()) {
				return ATCatwalkSides.ATTACH;
			} else if (neighbor.getValue(FACING) == direction.getCounterClockWise()) {
				return ATCatwalkSides.ATTACH_FLIPPED;
			} else {
				return ATCatwalkSides.RAILING;
			}
		} else if (neighbor.getBlock() instanceof CatwalkStairs) {
			if (neighbor.getValue(CatwalkStairs.HALF) == DoubleBlockHalf.LOWER
					&& neighbor.getValue(CatwalkStairs.FACING) == direction.getClockWise()) {
				return ATCatwalkSides.ATTACH;
			} else if (neighbor.getValue(CatwalkStairs.HALF) == DoubleBlockHalf.UPPER
					&& neighbor.getValue(CatwalkStairs.FACING) == direction.getCounterClockWise()) {
				return ATCatwalkSides.ATTACH_FLIPPED;
			} else {
				return ATCatwalkSides.RAILING;
			}
		} else {
			return ATCatwalkSides.RAILING;
		}
	}



	/**
	 * Gets the VoxelShape for the current block state.
	 * @return A VoxelShape used as the hitbox and collision box for the catwalk
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		ATCatwalkEnd end = state.getValue(CATWALK_END);
		ATCatwalkSides left = state.getValue(CATWALK_LEFT);
		ATCatwalkSides right = state.getValue(CATWALK_RIGHT);
		Direction facing = state.getValue(FACING);
		String shapeOutput; //Stores the shape without the direction, input for the second switch block
		VoxelShape output = null; //Final output, will never remain null
		switch (end) {
			case DROP:
				shapeOutput = "straight";
				break;
			case RAILING:
				if (isSideAttached(left) && isSideAttached(right)) {
					shapeOutput = "t";
					break;
				} else if (isSideAttached(left)) {
					shapeOutput = "corner_left";
					break;
				} else if (isSideAttached(right)) {
					shapeOutput = "corner_right";
					break;
				} else {
					shapeOutput = "end";
					break;
				}
			case ATTACH:
				if (isSideAttached(left) && isSideAttached(right)) {
					shapeOutput = "cross";
					break;
				} else if (isSideAttached(left)) {
					shapeOutput = "t_left";
					break;
				} else if (isSideAttached(right)) {
					shapeOutput = "t_right";
					break;
				} else {
					shapeOutput = "straight";
					break;
				}
			default:
				throw new IllegalStateException("Unexpected value: " + end);
		}
		switch (shapeOutput) {
			case "cross":
				output = CROSS;
				break;
			case "t":
				switch (facing) {
					case NORTH -> output = T_NORTH;
					case EAST -> output = T_EAST;
					case SOUTH -> output = T_SOUTH;
					case WEST -> output = T_WEST;
				}
				break;
			case "t_left":
				switch (facing) {
					case NORTH -> output = T_EAST;
					case EAST -> output = T_SOUTH;
					case SOUTH -> output = T_WEST;
					case WEST -> output = T_NORTH;
				}
				break;
			case "t_right":
				switch (facing) {
					case NORTH -> output = T_WEST;
					case EAST -> output = T_NORTH;
					case SOUTH -> output = T_EAST;
					case WEST -> output = T_SOUTH;
				}
				break;
			case "corner_left":
				switch (facing) {
					case NORTH -> output = CORNER_NORTH;
					case EAST -> output = CORNER_EAST;
					case SOUTH -> output = CORNER_SOUTH;
					case WEST -> output = CORNER_WEST;
				}
				break;
			case "corner_right":
				switch (facing) {
					case NORTH -> output = CORNER_WEST;
					case EAST -> output = CORNER_NORTH;
					case SOUTH -> output = CORNER_EAST;
					case WEST -> output = CORNER_SOUTH;
				}
				break;
			case "straight":
				switch (facing) {
					case NORTH, SOUTH -> output = STRAIGHT_NORTHSOUTH;
					case EAST, WEST -> output = STRAIGHT_EASTWEST;
				}
				break;
			case "end":
				switch (facing) {
					case NORTH -> output = END_NORTH;
					case EAST -> output = END_EAST;
					case SOUTH -> output = END_SOUTH;
					case WEST -> output = END_WEST;
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value:" + shapeOutput);
		}
		return output;
	}

	/**
	 * Convenience method used in {@link #getShape(BlockState, BlockGetter, BlockPos, CollisionContext) getShape}.
	 * @param side A CatwalkSides enum variable
	 * @return <code>true</code> if <code>side</code> equals <code>ATTACH</code> or <code>ATTACH_FLIPPED</code>, <code>false</code> otherwise
	 */
	public boolean isSideAttached(ATCatwalkSides side) {
		return (side == ATCatwalkSides.ATTACH || side == ATCatwalkSides.ATTACH_FLIPPED);
	}


	//Defining VoxelShapes used for different block states
	private static final VoxelShape STRAIGHT_NORTHSOUTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 0, 16, 14, 16),
			Block.box(0, 1, 0, 1, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape STRAIGHT_EASTWEST = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 15, 16, 14, 16),
			Block.box(0, 1, 0, 16, 14, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape END_NORTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 0, 16, 14, 16),
			Block.box(0, 1, 0, 1, 14, 16),
			Block.box(1, 1, 15, 15, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape END_EAST = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 15, 16, 14, 16),
			Block.box(0, 1, 0, 16, 14, 1),
			Block.box(0, 1, 1, 1, 14, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape END_SOUTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 0, 1, 14, 16),
			Block.box(15, 1, 0, 16, 14, 16),
			Block.box(1, 1, 0, 15, 14, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape END_WEST = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 0, 16, 14, 1),
			Block.box(0, 1, 15, 16, 14, 16),
			Block.box(15, 1, 1, 16, 14, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape CORNER_NORTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 0, 16, 14, 1),
			Block.box(0, 1, 0, 1, 14, 16),
			Block.box(1, 1, 15, 16, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape CORNER_EAST = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 15, 16, 14, 16),
			Block.box(0, 1, 0, 16, 14, 1),
			Block.box(0, 1, 1, 1, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape CORNER_SOUTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 15, 1, 14, 16),
			Block.box(15, 1, 0, 16, 14, 16),
			Block.box(0, 1, 0, 15, 14, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape CORNER_WEST = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 0, 1, 14, 1),
			Block.box(0, 1, 15, 16, 14, 16),
			Block.box(15, 1, 0, 16, 14, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape T_NORTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 0, 16, 14, 1),
			Block.box(0, 1, 0, 1, 14, 1),
			Block.box(0, 1, 15, 16, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape T_EAST = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 15, 16, 14, 16),
			Block.box(15, 1, 0, 16, 14, 1),
			Block.box(0, 1, 0, 1, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape T_SOUTH = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 15, 1, 14, 16),
			Block.box(15, 1, 15, 16, 14, 16),
			Block.box(0, 1, 0, 16, 14, 1)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape T_WEST= Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 1, 0, 1, 14, 1),
			Block.box(0, 1, 15, 1, 14, 16),
			Block.box(15, 1, 0, 16, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
	
	private static final VoxelShape CROSS = Stream.of(
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(15, 1, 0, 16, 14, 1),
			Block.box(15, 1, 15, 16, 14, 16),
			Block.box(0, 1, 0, 1, 14, 1),
			Block.box(0, 1, 15, 1, 14, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
}
