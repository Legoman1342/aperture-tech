package com.Legoman1342.entities.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CubeEntity extends LivingEntity implements IAnimatable {
	private AnimationFactory factory = new AnimationFactory(this);

	public CubeEntity(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	public static AttributeSupplier setAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.0D)
				.build();
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return NonNullList.withSize(4, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot pSlot) {
		return new ItemStack(Items.AIR);
	}

	@Override
	public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
		if (event.isMoving()) { //TODO Change this condition
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.weighted_cube.activate", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.weighted_cube.deactivate", true));
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
}
