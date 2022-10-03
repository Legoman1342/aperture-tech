package com.Legoman1342.blockentities.custom;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.blocks.custom.ChamberlockDoor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ChamberlockDoorBE extends BlockEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);
	/**
	 * Used to determine when to play open/close animations.
	 */
	private boolean poweredCheck = false;

	public ChamberlockDoorBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityRegistration.CHAMBERLOCK_DOOR_BE.get(), pWorldPosition, pBlockState);
	}

	private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
		Level level = getLevel();
		BlockPos pos = getBlockPos();
		if (level != null && level.getBlockState(pos).getBlock() == BlockRegistration.CHAMBERLOCK_DOOR.get()) {
			boolean powered = level.getBlockState(pos).getValue(ChamberlockDoor.POWERED);
			//If the powered state this tick is different from what it was last tick, play an animation
			if (powered != poweredCheck) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(
						powered ? "animation.chamberlock_door.open" : "animation.chamberlock_door.close",false));
			}
			poweredCheck = powered;
			if (powered && event.getController().getAnimationState() == AnimationState.Stopped) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.chamberlock_door.opened"));
			}
		}
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<ChamberlockDoorBE> controller = new AnimationController(
				this, "controller", 0, this::predicate
		);
		data.addAnimationController(controller);
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
}
