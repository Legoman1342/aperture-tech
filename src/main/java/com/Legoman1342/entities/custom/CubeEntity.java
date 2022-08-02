package com.Legoman1342.entities.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CubeEntity extends LivingEntity implements IAnimatable {
	//Synced data is kept in sync between client and server
	private static final EntityDataAccessor<Boolean> ACTIVATING = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.BOOLEAN);

	private static final Logger LOGGER = LogManager.getLogger();

	private AnimationFactory factory = new AnimationFactory(this);

	public CubeEntity(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	/**
	 * Defines and sets defaults for entity attributes.
	 */
	public static AttributeSupplier setAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 1.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.0D)
				.build();
	}

	/**
	 * Defines initial states for synced data variables.
	 */
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ACTIVATING, false);
	}

	/**
	 * Called to update the entity's position/logic
	 */
	@Override
	public void tick() {
		super.tick();

		BlockState feetBlockState = this.getFeetBlockState();
		if (isPoweredPressurePlate(feetBlockState)) {
			this.entityData.set(ACTIVATING, true);
		} else {
			this.entityData.set(ACTIVATING, false);
		}
	}

	/**
	 * Convenience method used to determine if a block is a powered pressure plate or weighted pressure plate.
	 */
	public boolean isPoweredPressurePlate(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block instanceof PressurePlateBlock) {
			return blockState.getValue(PressurePlateBlock.POWERED);
		} else if (block instanceof WeightedPressurePlateBlock) {
			return blockState.getValue(WeightedPressurePlateBlock.POWER) > 0;
		} else {
			return false;
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (pSource != DamageSource.OUT_OF_WORLD) {
			return false;
		}
		return super.hurt(pSource, pAmount);
	}

	private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
		if (this.entityData.get(ACTIVATING)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("activate", true));
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(new AnimationBuilder().addAnimation("deactivate", true));
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
}
