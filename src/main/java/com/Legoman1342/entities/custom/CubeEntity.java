package com.Legoman1342.entities.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;

public class CubeEntity extends LivingEntity implements IAnimatable {
	/**
	 * Synced data is kept in sync between the client and the server.
	 */
	private static final EntityDataAccessor<Boolean> ACTIVATING = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FIZZLING = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FIZZLE_TIMER = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.INT);

	//Used for debugging
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * List of damage sources that can kill CubeEntities.
	 */
	public static final ArrayList<DamageSource> CAN_KILL_CUBES = new ArrayList<>(List.of(
			DamageSource.OUT_OF_WORLD,
			DamageSource.LAVA,
			DamageSource.LIGHTNING_BOLT
	));

	private AnimationFactory factory = new AnimationFactory(this);

	public CubeEntity(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	/**
	 * Defines and sets defaults for entity attributes.
	 */
	public static AttributeSupplier setAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, Double.POSITIVE_INFINITY)
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
		entityData.define(ACTIVATING, false);
		entityData.define(FIZZLING, false);
		entityData.define(FIZZLE_TIMER, 0);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		super.tick();

		//If the cube is activating a pressure plate, turn the light ring orange
		BlockState feetBlockState = this.getFeetBlockState();
		if (isPoweredPressurePlate(feetBlockState)) {
			entityData.set(ACTIVATING, true);
		} else {
			entityData.set(ACTIVATING, false);
		}

		//If the cube is fizzling, count down the timer until it reaches 0
		if (entityData.get(FIZZLE_TIMER) > 0) {
			entityData.set(FIZZLE_TIMER, entityData.get(FIZZLE_TIMER) - 1);
		}

		//If the fizzle timer just finished, kill the cube
		if (entityData.get(FIZZLING) && entityData.get(FIZZLE_TIMER) == 0) {
			entityData.set(FIZZLING, false);
			this.setPos(position().x, -500, position().z);
			kill();
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
		if (!canBeKilledBy(pSource)) {
			return false;
		}
		if (entityData.get(FIZZLING)) {
			return false;
		}
		fizzle();
		return super.hurt(pSource, pAmount);
	}

	/**
	 * Returns whether cubes can be killed by the inputted source, determined by the <code>CAN_KILL_CUBES</code> ArrayList.
	 */
	public boolean canBeKilledBy(DamageSource source) {
		return CAN_KILL_CUBES.contains(source);
	}

	/**
	 * Starts the cube fizzling, the rest is handled by the <code>tick</code> and <code>predicate</code> functions.
	 */
	public void fizzle() {
		entityData.set(FIZZLE_TIMER, 60);
		entityData.set(FIZZLING, true);
	}

	/**
	 * Used to determine whether the fire effect should be rendered on the entity.
	 */
	@Override
	public boolean isOnFire() {
		return false;
	}

	private <E extends IAnimatable>PlayState predicate(AnimationEvent<E> event) {
		if (entityData.get(FIZZLING)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("fizzle", false));
			return PlayState.CONTINUE;
		} else if (entityData.get(ACTIVATING)) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("activate", true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("deactivate", true));
		return PlayState.CONTINUE;
	}

	/**
	 * Sets up animation controllers.
	 */
	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<CubeEntity> controller = new AnimationController(this, "controller", 0, this::predicate);
		controller.registerParticleListener(this::particleListener);
		//TODO soundListener
		data.addAnimationController(controller);
	}

	/**
	 * Activates whenever a particle keyframe occurs in an animation.
	 */
	private <ENTITY extends IAnimatable> void particleListener(ParticleKeyFrameEvent<ENTITY> event) {
		Vec3 pos = this.position();
		double posX = pos.x;
		double posY = pos.y;
		double posZ = pos.z;
		double speedX;
		double speedY;
		double speedZ;
		for (int i = 0; i < 100; i++) {
			speedX = random.nextDouble() - 0.5;
			speedY = random.nextDouble();
			speedZ = random.nextDouble() - 0.5;
			this.level.addParticle(ParticleTypes.END_ROD, posX, posY, posZ, speedX, speedY, speedZ);
		}
	}

	//TODO Add soundListener function

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
