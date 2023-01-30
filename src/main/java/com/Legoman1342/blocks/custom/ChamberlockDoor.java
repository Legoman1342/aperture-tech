package com.Legoman1342.blocks.custom;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blocks.ATMultiblock;
import com.Legoman1342.blocks.ATMultiblock.ATMultiblockPart;
import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.sounds.SoundRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ChamberlockDoor extends BaseEntityBlock {

	ATMultiblock multiblock = new ATMultiblock(true, false, false, FACING, PART);

	//Block state properties
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<ATMultiblockPart> PART = EnumProperty.create("part", ATMultiblockPart.class);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty FRONT_CONDUCTIVE = BooleanProperty.create("front_conductive");
	public static final BooleanProperty BACK_CONDUCTIVE = BooleanProperty.create("back_conductive");

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, PART, OPEN, POWERED, FRONT_CONDUCTIVE, BACK_CONDUCTIVE);
	}

	public ChamberlockDoor(Properties properties) {
		super(properties);
	}

	/**
	 * Sets additional blockstates (<code>OPEN, POWERED, FRONT_CONDUCTIVE, BACK_CONDUCTIVE</code>) that {@link ATMultiblock#getStateForPlacement(BlockPlaceContext, Block) ATMultiblock#getStateForPlacement} doesn't cover.
	 */
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		BlockState toReturn = multiblock.getStateForPlacement(pContext, this);
		if (toReturn != null) {
			return toReturn.setValue(OPEN, false).setValue(POWERED, false).setValue(FRONT_CONDUCTIVE, true).setValue(BACK_CONDUCTIVE, false);
		} else {
			return null;
		}
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		multiblock.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
	}

	/**
	 * Handles block updates, including opening/closing the door when it's powered/unpowered.
	 */
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		BlockState[] states = multiblock.getOtherPartStates(pPos, pState, pLevel);
		if (pLevel.hasNeighborSignal(pPos)) { //If I'm powered...
			if (!pLevel.getBlockState(pPos).getValue(OPEN)) { //If I'm not already open, play a sound
				pLevel.playSound(null, pPos, SoundRegistration.CHAMBERLOCK_DOOR_OPEN.get(), SoundSource.BLOCKS, 1, 1);
			}
			pLevel.setBlock(pPos, pState.setValue(POWERED, true).setValue(OPEN, true), 3); //...open me and mark me as powered
		} else if (states[0].getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get() && states[0].getValue(POWERED)
				|| states[1].getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get() && states[1].getValue(POWERED)
				|| states[2].getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get() && states[2].getValue(POWERED)) { //If I'm not powered, but one of the other door parts is...
			pLevel.setBlock(pPos, pState.setValue(POWERED, false).setValue(OPEN, true), 3); //...just open me
		} else { //Otherwise, close the door
			if (pLevel.getBlockState(pPos).getValue(POWERED)) { //If I was the powered part of the door, play a sound as I close
				pLevel.playSound(null, pPos, SoundRegistration.CHAMBERLOCK_DOOR_CLOSE.get(), SoundSource.BLOCKS, 1, 1);
			}
			pLevel.setBlock(pPos, pState.setValue(POWERED, false).setValue(OPEN, false), 3);
		}
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		return multiblock.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos, this);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction facing = pState.getValue(FACING);
		Boolean open = pState.getValue(OPEN);
		ATMultiblockPart part = pState.getValue(PART);
		switch (part) {
			case TOP_LEFT -> {
				return switch (facing) {
					case NORTH -> open ? TOP_LEFT_OPEN_NORTH : TOP_LEFT_CLOSED_NORTH;
					case SOUTH -> open ? TOP_LEFT_OPEN_SOUTH : TOP_LEFT_CLOSED_SOUTH;
					case WEST -> open ? TOP_LEFT_OPEN_WEST : TOP_LEFT_CLOSED_WEST;
					case EAST -> open ? TOP_LEFT_OPEN_EAST : TOP_LEFT_CLOSED_EAST;
					default -> throw new IllegalStateException("Unexpected value: " + facing);
				};
			}
			case TOP_RIGHT -> {
				return switch (facing) {
					case NORTH -> open ? TOP_RIGHT_OPEN_NORTH : TOP_RIGHT_CLOSED_NORTH;
					case SOUTH -> open ? TOP_RIGHT_OPEN_SOUTH : TOP_RIGHT_CLOSED_SOUTH;
					case WEST -> open ? TOP_RIGHT_OPEN_WEST : TOP_RIGHT_CLOSED_WEST;
					case EAST -> open ? TOP_RIGHT_OPEN_EAST : TOP_RIGHT_CLOSED_EAST;
					default -> throw new IllegalStateException("Unexpected value: " + facing);
				};
			}
			case BOTTOM_LEFT -> {
				return switch (facing) {
					case NORTH -> open ? BOTTOM_LEFT_OPEN_NORTH : BOTTOM_LEFT_CLOSED_NORTH;
					case SOUTH -> open ? BOTTOM_LEFT_OPEN_SOUTH : BOTTOM_LEFT_CLOSED_SOUTH;
					case WEST -> open ? BOTTOM_LEFT_OPEN_WEST : BOTTOM_LEFT_CLOSED_WEST;
					case EAST -> open ? BOTTOM_LEFT_OPEN_EAST : BOTTOM_LEFT_CLOSED_EAST;
					default -> throw new IllegalStateException("Unexpected value: " + facing);
				};
			}
			case BOTTOM_RIGHT -> {
				return switch (facing) {
					case NORTH -> open ? BOTTOM_RIGHT_OPEN_NORTH : BOTTOM_RIGHT_CLOSED_NORTH;
					case SOUTH -> open ? BOTTOM_RIGHT_OPEN_SOUTH : BOTTOM_RIGHT_CLOSED_SOUTH;
					case WEST -> open ? BOTTOM_RIGHT_OPEN_WEST : BOTTOM_RIGHT_CLOSED_WEST;
					case EAST -> open ? BOTTOM_RIGHT_OPEN_EAST : BOTTOM_RIGHT_CLOSED_EAST;
					default -> throw new IllegalStateException("Unexpected value: " + facing);
				};
			}
		}
		return null;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState pState) {
		return true;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return BlockEntityRegistration.CHAMBERLOCK_DOOR_BE.get().create(pPos, pState);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}


	//Defining VoxelShapes used for different block states
	private static final VoxelShape BOTTOM_LEFT_CLOSED_NORTH = Stream.of(
			Block.box(12, 0, 0, 16, 16, 3),
			Block.box(0, 0, 1, 12, 16, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_LEFT_CLOSED_EAST = Stream.of(
			Block.box(13, 0, 12, 16, 16, 16),
			Block.box(14, 0, 0, 15, 16, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_LEFT_CLOSED_SOUTH = Stream.of(
			Block.box(0, 0, 13, 4, 16, 16),
			Block.box(4, 0, 14, 16, 16, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_LEFT_CLOSED_WEST = Stream.of(
			Block.box(0, 0, 0, 3, 16, 4),
			Block.box(1, 0, 4, 2, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_LEFT_OPEN_NORTH =
			Block.box(12, 0, 0, 16, 16, 3);

	private static final VoxelShape BOTTOM_LEFT_OPEN_EAST =
			Block.box(13, 0, 12, 16, 16, 16);

	private static final VoxelShape BOTTOM_LEFT_OPEN_SOUTH =
			Block.box(0, 0, 13, 4, 16, 16);

	private static final VoxelShape BOTTOM_LEFT_OPEN_WEST =
			Block.box(0, 0, 0, 3, 16, 4);

	private static final VoxelShape BOTTOM_RIGHT_CLOSED_NORTH = Stream.of(
			Block.box(0, 0, 0, 4, 16, 3),
			Block.box(4, 0, 1, 16, 16, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_RIGHT_CLOSED_EAST = Stream.of(
			Block.box(13, 0, 0, 16, 16, 4),
			Block.box(14, 0, 4, 15, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_RIGHT_CLOSED_SOUTH = Stream.of(
			Block.box(12, 0, 13, 16, 16, 16),
			Block.box(0, 0, 14, 12, 16, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_RIGHT_CLOSED_WEST = Stream.of(
			Block.box(0, 0, 12, 3, 16, 16),
			Block.box(1, 0, 0, 2, 16, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape BOTTOM_RIGHT_OPEN_NORTH =
			Block.box(0, 0, 0, 4, 16, 3);

	private static final VoxelShape BOTTOM_RIGHT_OPEN_EAST =
			Block.box(13, 0, 0, 16, 16, 4);

	private static final VoxelShape BOTTOM_RIGHT_OPEN_SOUTH =
			Block.box(12, 0, 13, 16, 16, 16);

	private static final VoxelShape BOTTOM_RIGHT_OPEN_WEST =
			Block.box(0, 0, 12, 3, 16, 16);

	private static final VoxelShape TOP_LEFT_CLOSED_NORTH = Stream.of(
			Block.box(12, 0, 0, 16, 16, 3),
			Block.box(0, 13, 0, 12, 16, 3),
			Block.box(0, 0, 1, 12, 13, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_CLOSED_EAST = Stream.of(
			Block.box(13, 0, 12, 16, 16, 16),
			Block.box(13, 13, 0, 16, 16, 12),
			Block.box(14, 0, 0, 15, 13, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_CLOSED_SOUTH = Stream.of(
			Block.box(0, 0, 13, 4, 16, 16),
			Block.box(4, 13, 13, 16, 16, 16),
			Block.box(4, 0, 14, 16, 13, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_CLOSED_WEST = Stream.of(
			Block.box(0, 0, 0, 3, 16, 4),
			Block.box(0, 13, 4, 3, 16, 16),
			Block.box(1, 0, 4, 2, 13, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_OPEN_NORTH = Stream.of(
			Block.box(12, 0, 0, 16, 16, 3),
			Block.box(0, 13, 0, 12, 16, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_OPEN_EAST = Stream.of(
			Block.box(13, 0, 12, 16, 16, 16),
			Block.box(13, 13, 0, 16, 16, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_OPEN_SOUTH = Stream.of(
			Block.box(0, 0, 13, 4, 16, 16),
			Block.box(4, 13, 13, 16, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_LEFT_OPEN_WEST = Stream.of(
			Block.box(0, 0, 0, 3, 16, 4),
			Block.box(0, 13, 4, 3, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_CLOSED_NORTH = Stream.of(
			Block.box(0, 0, 0, 4, 16, 3),
			Block.box(4, 13, 0, 16, 16, 3),
			Block.box(4, 0, 1, 16, 13, 2)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_CLOSED_EAST = Stream.of(
			Block.box(13, 0, 0, 16, 16, 4),
			Block.box(13, 13, 4, 16, 16, 16),
			Block.box(14, 0, 4, 15, 13, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_CLOSED_SOUTH = Stream.of(
			Block.box(12, 0, 13, 16, 16, 16),
			Block.box(0, 13, 13, 12, 16, 16),
			Block.box(0, 0, 14, 12, 13, 15)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_CLOSED_WEST = Stream.of(
			Block.box(0, 0, 12, 3, 16, 16),
			Block.box(0, 13, 0, 3, 16, 12),
			Block.box(1, 0, 0, 2, 13, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_OPEN_NORTH = Stream.of(
			Block.box(0, 0, 0, 4, 16, 3),
			Block.box(4, 13, 0, 16, 16, 3)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_OPEN_EAST = Stream.of(
			Block.box(13, 0, 0, 16, 16, 4),
			Block.box(13, 13, 4, 16, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_OPEN_SOUTH = Stream.of(
			Block.box(12, 0, 13, 16, 16, 16),
			Block.box(0, 13, 13, 12, 16, 16)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

	private static final VoxelShape TOP_RIGHT_OPEN_WEST = Stream.of(
			Block.box(0, 0, 12, 3, 16, 16),
			Block.box(0, 13, 0, 3, 16, 12)
	).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();


}
