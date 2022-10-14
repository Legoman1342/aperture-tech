package com.Legoman1342.blocks.custom;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.sounds.SoundRegistration;
import com.Legoman1342.utilities.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class ChamberlockDoor extends BaseEntityBlock {

	//Block state properties
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<ATDoorPart> PART = EnumProperty.create("part", ATDoorPart.class);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty FRONT_CONDUCTIVE = BooleanProperty.create("front_conductive");
	public static final BooleanProperty BACK_CONDUCTIVE = BooleanProperty.create("back_conductive");


	/**
	 * Used to determine which portion of the door this block is.
	 */
	public enum ATDoorPart implements StringRepresentable {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, PART, OPEN, POWERED, FRONT_CONDUCTIVE, BACK_CONDUCTIVE);
	}

	public ChamberlockDoor(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Level level = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();
		Direction facing = pContext.getHorizontalDirection().getOpposite();
		boolean canPlaceAbove = level.getBlockState(pos.above()).canBeReplaced(pContext);
		boolean canPlaceBelow = level.getBlockState(pos.below()).canBeReplaced(pContext);
		//leftDirection and rightDirection are relative to player
		Direction leftDirection = facing.getClockWise();
		Direction rightDirection = facing.getCounterClockWise();
		BlockState toReturn = this.defaultBlockState()
				.setValue(FACING, facing)
				.setValue(OPEN, false)
				.setValue(POWERED, false);
		if (canPlaceAbove) {
			if (level.getBlockState(pos.relative(rightDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(rightDirection).above())).canBeReplaced(pContext)) {
				return toReturn.setValue(PART, ATDoorPart.BOTTOM_LEFT);
			} else if (level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(leftDirection).above())).canBeReplaced(pContext)) {
				return toReturn.setValue(PART, ATDoorPart.BOTTOM_RIGHT);
			}
		} else if (canPlaceBelow) {
			if (level.getBlockState(pos.relative(rightDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(rightDirection).below())).canBeReplaced(pContext)) {
				return toReturn.setValue(PART, ATDoorPart.TOP_LEFT);
			} else if (level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(leftDirection).below())).canBeReplaced(pContext)) {
				return toReturn.setValue(PART, ATDoorPart.TOP_RIGHT);
			}
		}
		return null;
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		BlockPos[] positions = getOtherPartPositions(pPos, pState);
		ATDoorPart[] parts = getCorrectOtherParts(pState);
		pLevel.setBlock(positions[0], pState.setValue(PART, parts[0]), 3);
		pLevel.setBlock(positions[1], pState.setValue(PART, parts[1]), 3);
		pLevel.setBlock(positions[2], pState.setValue(PART, parts[2]), 3);
	}

	/**
	 * Gets the positions of the other three parts of the door.
	 * @param pos This part's position
	 * @param state This part's state
	 * @return An array containing the BlockPos-es in this order: Above/below, above/below-left/right, left/right
	 */
	public static BlockPos[] getOtherPartPositions(BlockPos pos, BlockState state) {
		BlockPos[] toReturn = new BlockPos[3];
		Direction facing = state.getValue(FACING);
		//leftDirection and rightDirection are relative to a player looking at the front
		Direction leftDirection = facing.getClockWise();
		Direction rightDirection = facing.getCounterClockWise();
		switch (state.getValue(PART)) {
			case BOTTOM_LEFT -> {
				toReturn[0] = pos.above();
				toReturn[1] = pos.relative(rightDirection).above();
				toReturn[2] = pos.relative(rightDirection);
			}
			case BOTTOM_RIGHT -> {
				toReturn[0] = pos.above();
				toReturn[1] = pos.relative(leftDirection).above();
				toReturn[2] = pos.relative(leftDirection);
			}
			case TOP_LEFT -> {
				toReturn[0] = pos.below();
				toReturn[1] = pos.relative(rightDirection).below();
				toReturn[2] = pos.relative(rightDirection);
			}
			case TOP_RIGHT -> {
				toReturn[0] = pos.below();
				toReturn[1] = pos.relative(leftDirection).below();
				toReturn[2] = pos.relative(leftDirection);
			}
		}
		return toReturn;
	}

	/**
	 * Gets the block states of the other three parts of the door.
	 * @param pos This part's position
	 * @param state This part's state
	 * @return An array containing the BlockStates in this order: Above/below, above/below-left/right, left/right
	 */
	public static BlockState[] getOtherPartStates(BlockPos pos, BlockState state, Level level) {
		BlockState[] toReturn = new BlockState[3];
		BlockPos[] positions = getOtherPartPositions(pos, state);
		for (int i = 0; i <= 2; i++) {
			toReturn[i] = level.getBlockState(positions[i]);
		}
		return toReturn;
	}

	/**
	 * When given an ATDoorPart, returns what the other three ATDoorParts should be.
	 * @param state This part's state
	 * @return An array containing the correct ATDoorParts in this order: Above/below, above/below-left/right, left/right
	 */
	public static ATDoorPart[] getCorrectOtherParts(BlockState state) {
		ATDoorPart[] toReturn = new ATDoorPart[3];
		switch (state.getValue(PART)) {
			case BOTTOM_LEFT -> {
				toReturn[0] = ATDoorPart.TOP_LEFT;
				toReturn[1] = ATDoorPart.TOP_RIGHT;
				toReturn[2] = ATDoorPart.BOTTOM_RIGHT;
			}
			case BOTTOM_RIGHT -> {
				toReturn[0] = ATDoorPart.TOP_RIGHT;
				toReturn[1] = ATDoorPart.TOP_LEFT;
				toReturn[2] = ATDoorPart.BOTTOM_LEFT;
			}
			case TOP_LEFT -> {
				toReturn[0] = ATDoorPart.BOTTOM_LEFT;
				toReturn[1] = ATDoorPart.BOTTOM_RIGHT;
				toReturn[2] = ATDoorPart.TOP_RIGHT;
			}
			case TOP_RIGHT -> {
				toReturn[0] = ATDoorPart.BOTTOM_RIGHT;
				toReturn[1] = ATDoorPart.BOTTOM_LEFT;
				toReturn[2] = ATDoorPart.TOP_LEFT;
			}
		}
		return toReturn;
	}

	/**
	 * Checks if the other three parts' positions actually contain the correct parts.
	 * @param pos This part's position
	 * @param state This part's state
	 * @return An array containing a boolean value for each part in this order: Above/below, above/below-left/right, left/right
	 */
	public static boolean[] checkOtherParts(BlockPos pos, BlockState state, Level level) {
		boolean[] toReturn = new boolean[3];
		BlockState[] states = getOtherPartStates(pos, state, level);
		ATDoorPart[] correctParts = getCorrectOtherParts(state);
		toReturn[0] = states[0].is(state.getBlock()) && states[0].getValue(PART) == correctParts[0];
		toReturn[1] = states[1].is(state.getBlock()) && states[1].getValue(PART) == correctParts[1];
		toReturn[2] = states[2].is(state.getBlock()) && states[2].getValue(PART) == correctParts[2];
		return toReturn;
	}

	/**
	 * Handles block updates, including opening/closing the door when it's powered/unpowered.
	 */
	@Override
	public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
		BlockPos[] positions = getOtherPartPositions(pPos, pState);
		if (pLevel.hasNeighborSignal(pPos)) { //If I'm powered...
			if (!pLevel.getBlockState(pPos).getValue(OPEN)) { //If I'm not already open, play a sound
				pLevel.playSound(null, pPos, SoundRegistration.CHAMBERLOCK_DOOR_OPEN.get(), SoundSource.BLOCKS, 1, 1);
			}
			pLevel.setBlock(pPos, pState.setValue(POWERED, true).setValue(OPEN, true), 3); //...open me and mark me as powered
		} else if (pLevel.getBlockState(positions[0]).getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get() && pLevel.getBlockState(positions[0]).getValue(POWERED)
				|| pLevel.getBlockState(positions[1]).getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get() && pLevel.getBlockState(positions[1]).getValue(POWERED)
				|| pLevel.getBlockState(positions[2]).getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get() && pLevel.getBlockState(positions[2]).getValue(POWERED)) { //If I'm not powered, but one of the other door parts is...
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
		for (BlockState state : getOtherPartStates(pCurrentPos, pState, (Level) pLevel)) {
			if (state.getBlock() != BlockRegistration.CHAMBERLOCK_DOOR.get()) {
				pLevel.setBlock(pCurrentPos, Blocks.AIR.defaultBlockState(), 35);
				return Blocks.AIR.defaultBlockState();
			}
		}
		return pState;
	}

	@Override
	public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		if (!pLevel.isClientSide && pPlayer.isCreative()) {
			preventCreativeDropFromOtherParts(pLevel, pPos, pState, pPlayer);
		}
		super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
	}

	/**
	 Used to make sure that only one item is dropped when catwalk stairs are broken. <br>
	 Some code taken from {@link net.minecraft.world.level.block.DoublePlantBlock#preventCreativeDropFromBottomPart(Level, BlockPos, BlockState, Player) net.minecraft.world.level.block.DoublePlantBlock#preventCreativeDropFromBottomPart}
	 */
	protected static void preventCreativeDropFromOtherParts(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		BlockPos[] positions = getOtherPartPositions(pPos, pState);
		BlockState[] states = getOtherPartStates(pPos, pState, pLevel);
		for (int i = 0; i <= 2; i++) {
			if (checkOtherParts(pPos, pState, pLevel)[i]) {
				BlockState replacementState = states[i].hasProperty(BlockStateProperties.WATERLOGGED) && states[i].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
				pLevel.setBlock(positions[i], replacementState, 35);
				pLevel.levelEvent(pPlayer, 2001, positions[i], Block.getId(states[i]));
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		Direction facing = pState.getValue(FACING);
		Boolean open = pState.getValue(OPEN);
		ATDoorPart part = pState.getValue(PART);
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
