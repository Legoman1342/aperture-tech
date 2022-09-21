package com.Legoman1342.blockentities.custom;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ChamberlockDoorBE extends BlockEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);

	public ChamberlockDoorBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityRegistration.CHAMBERLOCK_DOOR_BE.get(), pWorldPosition, pBlockState);
	}


	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<ChamberlockDoorBE>(
				this, "controller", 0, this::predicate
		));
	}

	private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder().addAnimation("open", false));
		event.getController().setAnimation(new AnimationBuilder().addAnimation("close", false));

		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}
}
