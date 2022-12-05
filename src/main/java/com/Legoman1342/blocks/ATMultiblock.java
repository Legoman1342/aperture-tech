package com.Legoman1342.blocks;

import com.Legoman1342.utilities.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class ATMultiblock extends Block {

	public boolean canBeHorizontal;
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final EnumProperty<ATMultiblockPart> PART = EnumProperty.create("part", ATMultiblockPart.class);

	public ATMultiblock(Properties properties, boolean canBeHorizontal) {
		super(properties);
		this.canBeHorizontal = canBeHorizontal;
	}

	/**
	 * Used to determine which portion of the multiblock this block is.<br><br>
	 *
	 * When the multiblock is facing up or down, directions are from the perspective of a player looking straight up/down at
	 * the multiblock while facing south (towards +Z).  This means that when facing up (on the floor), "top" means south (+Z) and "right" means west (-X).
	 * When facing down (on the ceiling), "top" is reversed to north (-Z), but "right" still means west (-X).<br><br>
	 *
	 * When facing north/east/south/west (on walls), the directions are exactly as expected from the perspective of a player looking straight at the front of the multiblock.
	 */
	public enum ATMultiblockPart implements StringRepresentable {
		TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, PART);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		Level level = pContext.getLevel();
		BlockPos pos = pContext.getClickedPos();
		Direction facing = canBeHorizontal ? pContext.getClickedFace() : pContext.getHorizontalDirection().getOpposite();

		//Directions are from the perspective of the player placing the block
		Direction upDirection = switch (facing) {
			case DOWN -> Direction.NORTH;
			case UP -> Direction.SOUTH;
			case NORTH, EAST, SOUTH, WEST -> Direction.UP;
		};
		Direction downDirection = upDirection.getOpposite();
		Direction rightDirection = switch (facing) {
			case UP, DOWN -> Direction.WEST;
			case NORTH, EAST, SOUTH, WEST -> facing.getCounterClockWise();
		};
		Direction leftDirection = rightDirection.getOpposite();

		boolean canPlaceAbove = level.getBlockState(pos.relative(upDirection)).canBeReplaced(pContext);
		boolean canPlaceBelow = level.getBlockState(pos.relative(downDirection)).canBeReplaced(pContext);
		boolean canPlaceLeft = level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext);
		boolean canPlaceRight = level.getBlockState(pos.relative(rightDirection)).canBeReplaced(pContext);
		boolean canPlaceAboveLeft = level.getBlockState(pos.relative(upDirection).relative(leftDirection)).canBeReplaced(pContext);
		boolean canPlaceAboveRight = level.getBlockState(pos.relative(upDirection).relative(rightDirection)).canBeReplaced(pContext);
		boolean canPlaceBelowLeft = level.getBlockState(pos.relative(downDirection).relative(leftDirection)).canBeReplaced(pContext);
		boolean canPlaceBelowRight = level.getBlockState(pos.relative(downDirection).relative(rightDirection)).canBeReplaced(pContext);

		BlockState toReturn = this.defaultBlockState()
				.setValue(FACING, facing);

		if (canPlaceAbove) {
			if (canPlaceRight && canPlaceAboveRight) {
				return toReturn.setValue(PART, ATMultiblockPart.BOTTOM_LEFT);
			} else if (canPlaceLeft && canPlaceAboveLeft) {
				return toReturn.setValue(PART, ATMultiblockPart.BOTTOM_RIGHT);
			}
		} else if (canPlaceBelow) {
			if (canPlaceRight && canPlaceBelowRight) {
				return toReturn.setValue(PART, ATMultiblockPart.TOP_LEFT);
			} else if (canPlaceLeft && canPlaceBelowLeft) {
				return toReturn.setValue(PART, ATMultiblockPart.TOP_RIGHT);
			}
		}
		return null;
	}

	/**
	 * Places the other three parts of the multiblock.
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		BlockPos[] positions = getOtherPartPositions(pPos, pState);
		ATMultiblockPart[] parts = getCorrectOtherParts(pState);
		pLevel.setBlock(positions[0], pState.setValue(PART, parts[0]), 3);
		pLevel.setBlock(positions[1], pState.setValue(PART, parts[1]), 3);
		pLevel.setBlock(positions[2], pState.setValue(PART, parts[2]), 3);
	}

	/**
	 * Gets the positions of the other three parts of the multiblock.
	 * @param pos This part's position
	 * @param state This part's state
	 * @return An array containing the BlockPos-es in this order: Above/below, above/below-left/right, left/right
	 */
	public static BlockPos[] getOtherPartPositions(BlockPos pos, BlockState state) {
		BlockPos[] toReturn = new BlockPos[3];
		Direction facing = state.getValue(FACING);
		//Directions are relative to a player looking straight at the front
		Direction upDirection = switch (facing) {
			case DOWN -> Direction.NORTH;
			case UP -> Direction.SOUTH;
			case NORTH, EAST, SOUTH, WEST -> Direction.UP;
		};
		Direction downDirection = upDirection.getOpposite();
		Direction rightDirection = switch (facing) {
			case UP, DOWN -> Direction.WEST;
			case NORTH, EAST, SOUTH, WEST -> facing.getCounterClockWise();
		};
		Direction leftDirection = rightDirection.getOpposite();
		
		switch (state.getValue(PART)) {
			case BOTTOM_LEFT -> {
				toReturn[0] = pos.relative(upDirection);
				toReturn[1] = pos.relative(rightDirection).relative(upDirection);
				toReturn[2] = pos.relative(rightDirection);
			}
			case BOTTOM_RIGHT -> {
				toReturn[0] = pos.relative(upDirection);
				toReturn[1] = pos.relative(leftDirection).relative(upDirection);
				toReturn[2] = pos.relative(leftDirection);
			}
			case TOP_LEFT -> {
				toReturn[0] = pos.relative(downDirection);
				toReturn[1] = pos.relative(rightDirection).relative(downDirection);
				toReturn[2] = pos.relative(rightDirection);
			}
			case TOP_RIGHT -> {
				toReturn[0] = pos.relative(downDirection);
				toReturn[1] = pos.relative(leftDirection).relative(downDirection);
				toReturn[2] = pos.relative(leftDirection);
			}
		}
		return toReturn;
	}

	/**
	 * Gets the block states of the other three parts of the multiblock.
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
	 * When given a blockstate with an ATMultiblockPart, returns what the other three ATMultiblockParts should be.
	 * @param state This part's state
	 * @return An array containing the correct ATMultiblockParts in this order: Above/below, above/below-left/right, left/right
	 */
	public static ATMultiblockPart[] getCorrectOtherParts(BlockState state) {
		ATMultiblockPart[] toReturn = new ATMultiblockPart[3];
		switch (state.getValue(PART)) {
			case BOTTOM_LEFT -> {
				toReturn[0] = ATMultiblockPart.TOP_LEFT;
				toReturn[1] = ATMultiblockPart.TOP_RIGHT;
				toReturn[2] = ATMultiblockPart.BOTTOM_RIGHT;
			}
			case BOTTOM_RIGHT -> {
				toReturn[0] = ATMultiblockPart.TOP_RIGHT;
				toReturn[1] = ATMultiblockPart.TOP_LEFT;
				toReturn[2] = ATMultiblockPart.BOTTOM_LEFT;
			}
			case TOP_LEFT -> {
				toReturn[0] = ATMultiblockPart.BOTTOM_LEFT;
				toReturn[1] = ATMultiblockPart.BOTTOM_RIGHT;
				toReturn[2] = ATMultiblockPart.TOP_RIGHT;
			}
			case TOP_RIGHT -> {
				toReturn[0] = ATMultiblockPart.BOTTOM_RIGHT;
				toReturn[1] = ATMultiblockPart.BOTTOM_LEFT;
				toReturn[2] = ATMultiblockPart.TOP_LEFT;
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
		ATMultiblockPart[] correctParts = getCorrectOtherParts(state);
		toReturn[0] = states[0].is(state.getBlock()) && states[0].getValue(PART) == correctParts[0];
		toReturn[1] = states[1].is(state.getBlock()) && states[1].getValue(PART) == correctParts[1];
		toReturn[2] = states[2].is(state.getBlock()) && states[2].getValue(PART) == correctParts[2];
		return toReturn;
	}

	/**
	 * Called when the block is destroyed.  Makes sure the other parts are destroyed correctly as well.
	 */
	@Override
	public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		if (!pLevel.isClientSide && pPlayer.isCreative()) {
			preventCreativeDropFromOtherParts(pLevel, pPos, pState, pPlayer);
		}
		super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
	}

	/**
	 Used to make sure that only one item is dropped when the multiblock is broken. <br>
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
}
