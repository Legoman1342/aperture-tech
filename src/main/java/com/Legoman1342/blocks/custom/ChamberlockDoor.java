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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
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
						.setValue(PART, ATDoorPart.BOTTOM_LEFT)
						.setValue(OPEN, false);
			} else if (level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(leftDirection).above())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.BOTTOM_RIGHT)
						.setValue(OPEN, false);
			}
		} else if (canPlaceBelow) {
			if (level.getBlockState(pos.relative(rightDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(rightDirection).below())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.TOP_LEFT)
						.setValue(OPEN, false);
			} else if (level.getBlockState(pos.relative(leftDirection)).canBeReplaced(pContext)
					&& (level.getBlockState(pos.relative(leftDirection).below())).canBeReplaced(pContext)) {
				return this.defaultBlockState()
						.setValue(FACING, facing)
						.setValue(PART, ATDoorPart.TOP_RIGHT)
						.setValue(OPEN, false);
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
