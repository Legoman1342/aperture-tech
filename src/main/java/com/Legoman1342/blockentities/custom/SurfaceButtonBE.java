package com.Legoman1342.blockentities.custom;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SurfaceButtonBE extends BlockEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);

	public SurfaceButtonBE(BlockPos pWorldPosition, BlockState pBlockState) {
		super(BlockEntityRegistration.SURFACE_BUTTON_BE.get(), pWorldPosition, pBlockState);
	}

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		//TODO Add code to control animations
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
