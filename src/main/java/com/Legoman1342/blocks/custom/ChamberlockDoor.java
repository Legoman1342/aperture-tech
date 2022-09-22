package com.Legoman1342.blocks.custom;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.utilities.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.Nullable;

public class ChamberlockDoor extends BaseEntityBlock {

	//Block state properties
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<ATDoorPart> PART = EnumProperty.create("part", ATDoorPart.class);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty FRONT_CONDUCTIVE = BooleanProperty.create("front_conductive");
	public static final BooleanProperty BACK_CONDUCTIVE = BooleanProperty.create("back_conductive");

	/**
	 * Used to determine which portion of the door this block is
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
		pBuilder.add(FACING, PART, OPEN, FRONT_CONDUCTIVE, BACK_CONDUCTIVE);
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
		if (canPlaceAbove) {
			if (level.getBlockState(pos.relative(rightDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(rightDirection).above())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.BOTTOM_LEFT);
			} else if (level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(leftDirection).above())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.BOTTOM_RIGHT);
			}
		} else if (canPlaceBelow) {
			if (level.getBlockState(pos.relative(rightDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(rightDirection).below())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.TOP_LEFT);
			} else if (level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(leftDirection).below())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.TOP_RIGHT);
			}
		}
		return null;
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
		Direction facing = pState.getValue(FACING);
		//leftDirection and rightDirection are relative to player
		Direction leftDirection = facing.getClockWise();
		Direction rightDirection = facing.getCounterClockWise();

		switch (pState.getValue(PART)) {
			case BOTTOM_LEFT -> {
				pLevel.setBlock(pPos.above(), pState.setValue(PART, ATDoorPart.TOP_LEFT), 3);
				pLevel.setBlock(pPos.relative(rightDirection), pState.setValue(PART, ATDoorPart.BOTTOM_RIGHT), 3);
				pLevel.setBlock(pPos.relative(rightDirection).above(), pState.setValue(PART, ATDoorPart.TOP_RIGHT), 3);
			}
			case BOTTOM_RIGHT -> {
				pLevel.setBlock(pPos.above(), pState.setValue(PART, ATDoorPart.TOP_RIGHT), 3);
				pLevel.setBlock(pPos.relative(leftDirection), pState.setValue(PART, ATDoorPart.BOTTOM_LEFT), 3);
				pLevel.setBlock(pPos.relative(leftDirection).above(), pState.setValue(PART, ATDoorPart.TOP_LEFT), 3);
			}
			case TOP_LEFT -> {
				pLevel.setBlock(pPos.below(), pState.setValue(PART, ATDoorPart.BOTTOM_LEFT), 3);
				pLevel.setBlock(pPos.relative(rightDirection), pState.setValue(PART, ATDoorPart.TOP_RIGHT), 3);
				pLevel.setBlock(pPos.relative(rightDirection).below(), pState.setValue(PART, ATDoorPart.BOTTOM_RIGHT), 3);
			}
			case TOP_RIGHT -> {
				pLevel.setBlock(pPos.below(), pState.setValue(PART, ATDoorPart.BOTTOM_RIGHT), 3);
				pLevel.setBlock(pPos.relative(leftDirection), pState.setValue(PART, ATDoorPart.TOP_LEFT), 3);
				pLevel.setBlock(pPos.relative(leftDirection).below(), pState.setValue(PART, ATDoorPart.BOTTOM_LEFT), 3);
			}
		}
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
	 Code modified from {@link net.minecraft.world.level.block.DoublePlantBlock#preventCreativeDropFromBottomPart(Level, BlockPos, BlockState, Player) net.minecraft.world.level.block.DoublePlantBlock#preventCreativeDropFromBottomPart}
	 */
	protected static void preventCreativeDropFromOtherParts(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		Direction facing = pState.getValue(FACING);
		//leftDirection and rightDirection are relative to player who placed the block
		Direction leftDirection = facing.getClockWise();
		Direction rightDirection = facing.getCounterClockWise();
		BlockPos[] positions = new BlockPos[3];
		BlockState[] states = new BlockState[3];
		switch (pState.getValue(PART)) {
			case BOTTOM_LEFT -> {
				positions[0] = pPos.above();
				positions[1] = pPos.relative(rightDirection).above();
				positions[2] = pPos.relative(rightDirection);
				for (int i = 0; i < 3; i++) {
					states[i] = pLevel.getBlockState(positions[i]);
				}
				if (states[0].is(pState.getBlock()) && states[0].getValue(PART) == ATDoorPart.TOP_LEFT) {
					BlockState replacementState = states[0].hasProperty(BlockStateProperties.WATERLOGGED) && states[0].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[0], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[0], Block.getId(states[0]));
				}
				if (states[1].is(pState.getBlock()) && states[1].getValue(PART) == ATDoorPart.TOP_RIGHT) {
					BlockState replacementState = states[1].hasProperty(BlockStateProperties.WATERLOGGED) && states[1].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[1], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[1], Block.getId(states[1]));
				}
				if (states[2].is(pState.getBlock()) && states[2].getValue(PART) == ATDoorPart.BOTTOM_RIGHT) {
					BlockState replacementState = states[2].hasProperty(BlockStateProperties.WATERLOGGED) && states[2].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[2], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[2], Block.getId(states[2]));
				}
			}
			case BOTTOM_RIGHT -> {
				positions[0] = pPos.above();
				positions[1] = pPos.relative(leftDirection).above();
				positions[2] = pPos.relative(leftDirection);
				for (int i = 0; i < 3; i++) {
					states[i] = pLevel.getBlockState(positions[i]);
				}
				if (states[0].is(pState.getBlock()) && states[0].getValue(PART) == ATDoorPart.TOP_RIGHT) {
					BlockState replacementState = states[0].hasProperty(BlockStateProperties.WATERLOGGED) && states[0].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[0], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[0], Block.getId(states[0]));
				}
				if (states[1].is(pState.getBlock()) && states[1].getValue(PART) == ATDoorPart.TOP_LEFT) {
					BlockState replacementState = states[1].hasProperty(BlockStateProperties.WATERLOGGED) && states[1].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[1], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[1], Block.getId(states[1]));
				}
				if (states[2].is(pState.getBlock()) && states[2].getValue(PART) == ATDoorPart.BOTTOM_LEFT) {
					BlockState replacementState = states[2].hasProperty(BlockStateProperties.WATERLOGGED) && states[2].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[2], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[2], Block.getId(states[2]));
				}
			}
			case TOP_LEFT -> {
				positions[0] = pPos.below();
				positions[1] = pPos.relative(rightDirection).below();
				positions[2] = pPos.relative(rightDirection);
				for (int i = 0; i < 3; i++) {
					states[i] = pLevel.getBlockState(positions[i]);
				}
				if (states[0].is(pState.getBlock()) && states[0].getValue(PART) == ATDoorPart.BOTTOM_LEFT) {
					BlockState replacementState = states[0].hasProperty(BlockStateProperties.WATERLOGGED) && states[0].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[0], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[0], Block.getId(states[0]));
				}
				if (states[1].is(pState.getBlock()) && states[1].getValue(PART) == ATDoorPart.BOTTOM_RIGHT) {
					BlockState replacementState = states[1].hasProperty(BlockStateProperties.WATERLOGGED) && states[1].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[1], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[1], Block.getId(states[1]));
				}
				if (states[2].is(pState.getBlock()) && states[2].getValue(PART) == ATDoorPart.TOP_RIGHT) {
					BlockState replacementState = states[2].hasProperty(BlockStateProperties.WATERLOGGED) && states[2].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[2], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[2], Block.getId(states[2]));
				}
			}
			case TOP_RIGHT -> {
				positions[0] = pPos.below();
				positions[1] = pPos.relative(leftDirection).below();
				positions[2] = pPos.relative(leftDirection);
				for (int i = 0; i < 3; i++) {
					states[i] = pLevel.getBlockState(positions[i]);
				}
				if (states[0].is(pState.getBlock()) && states[0].getValue(PART) == ATDoorPart.BOTTOM_RIGHT) {
					BlockState replacementState = states[0].hasProperty(BlockStateProperties.WATERLOGGED) && states[0].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[0], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[0], Block.getId(states[0]));
				}
				if (states[1].is(pState.getBlock()) && states[1].getValue(PART) == ATDoorPart.BOTTOM_LEFT) {
					BlockState replacementState = states[1].hasProperty(BlockStateProperties.WATERLOGGED) && states[1].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[1], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[1], Block.getId(states[1]));
				}
				if (states[2].is(pState.getBlock()) && states[2].getValue(PART) == ATDoorPart.TOP_LEFT) {
					BlockState replacementState = states[2].hasProperty(BlockStateProperties.WATERLOGGED) && states[2].getValue(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
					pLevel.setBlock(positions[2], replacementState, 35);
					pLevel.levelEvent(pPlayer, 2001, positions[2], Block.getId(states[2]));
				}
			}
		}
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
}
