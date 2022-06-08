package com.Legoman1342.blocks.custom;

import com.Legoman1342.utilities.Lang;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class Catwalk extends Block {
	
	//Creating block states
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty CATWALK_LEFT = BooleanProperty.create("left");
	public static final BooleanProperty CATWALK_RIGHT = BooleanProperty.create("right");
	public static final EnumProperty<CatwalkEnd> CATWALK_END = EnumProperty.create("end", CatwalkEnd.class);
	

	
	//The CatwalkEnd block property needs to be defined differently since it's an enum
	public enum CatwalkEnd implements StringRepresentable {
		DROP, RAILING, ATTACH,; //Possible shapes for the end of catwalks (possible values for the enum)
		
		public static CatwalkEnd byIndex(int index) { //Returns an enum value when given an index
			return values()[index];
		}

		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}
	
	//Constructor
	public Catwalk(Properties properties) {
		super(properties);
	}
	
	//Returns the VoxelShape for the current block state
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		CatwalkEnd end = state.getValue(CATWALK_END);
		boolean left = state.getValue(CATWALK_LEFT);
		boolean right = state.getValue(CATWALK_RIGHT);
		Direction facing = state.getValue(FACING);
		String shapeOutput; //Stores the shape without the direction, input for the second switch block
		VoxelShape output = null; //Final output, will never remain null
		switch (end) {
			case DROP:
				shapeOutput = "straight";
				break;
			case RAILING:
				if (left && right) {
				shapeOutput = "t";
				break;
			} else if (left) {
				shapeOutput = "corner_left";
				break;
			} else if (right) {
				shapeOutput = "corner_right";
				break;
			} else {
				shapeOutput = "end";
				break;
			}
			case ATTACH:
				if (left && right) {
					shapeOutput = "cross";
					break;
				} else if (left) {
					shapeOutput = "t_left";
					break;
				} else if (right) {
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
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, CATWALK_LEFT, CATWALK_RIGHT, CATWALK_END);
	}
	
	//Defines the state the block will be in when placed
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(CATWALK_LEFT, false)
				.setValue(CATWALK_RIGHT, false)
				.setValue(CATWALK_END, CatwalkEnd.RAILING);
	}
	
	
	
	
	//Defining the outlines of the block for different block states
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
