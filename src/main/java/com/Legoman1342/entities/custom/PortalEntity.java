package com.Legoman1342.entities.custom;

import com.Legoman1342.blocks.custom.SurfaceButton;
import com.Legoman1342.entities.EntityRegistration;
import com.Legoman1342.items.ItemRegistration;
import com.Legoman1342.sounds.SoundRegistration;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class PortalEntity extends LivingEntity implements IAnimatable {
	/**
	 * Synced data is kept in sync between the client and the server.
	 */
	private static final EntityDataAccessor<Integer> CHANNEL = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> PRIMARY = SynchedEntityData.defineId(PortalEntity.class, EntityDataSerializers.BOOLEAN);

	private AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public PortalEntity(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.noPhysics = true;
		this.setNoGravity(true);
	}

	private PortalEntity(int channel, boolean primary, Level level, Vec3 pos, float rotX, float rotY) {
		this(EntityRegistration.WALL_PORTAL.get(), level);
		entityData.set(CHANNEL, channel);
		entityData.set(PRIMARY, primary);
		moveTo(pos.x, pos.y, pos.z, rotY, rotX);
		this.rotate(Rotation.CLOCKWISE_180);
	}

	/**
	 * Returns a new portal entity.
	 * The constructor that this method uses has to be private to make some code in {@link EntityRegistration} work, so this
	 * method exists to provide access to it.
	 */
	public static PortalEntity newPortalEntity(int channel, boolean primary, Level level, Vec3 pos, float rotX, float rotY) {
		return new PortalEntity(channel, primary, level, pos, rotX, rotY);
	}

	/**
	 * Defines and sets defaults for entity attributes.
	 */
	public static AttributeSupplier setAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 1)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1)
				.add(Attributes.MOVEMENT_SPEED, 0)
				.add(Attributes.FOLLOW_RANGE, 0)
				.build();
	}

	/**
	 * Defines initial states for synced data variables.
	 */
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(CHANNEL, -1); //Default value, indicates that the channel hasn't been set yet
		entityData.define(PRIMARY, true);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		super.tick();
		this.setDeltaMovement(Vec3.ZERO);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (pSource == DamageSource.OUT_OF_WORLD) {
			return super.hurt(pSource, pAmount);
		} else {
			return false;
		}
	}

	/**
	 * Used to determine whether the fire effect should be rendered on the entity.
	 */
	@Override
	public boolean isOnFire() {
		return false;
	}

	/**
	 * Determines the position a newly summoned portal should be moved to in order to align with the block grid.
	 * @param pos The position the portal was summoned at
	 * @param facing The direction the portal is facing
	 * @return The aligned position to move the portal to
	 */
	public static Vec3 determinePortalPosition(Vec3 pos, Direction facing) {
		double x = switch (facing) {
			case EAST -> Math.round(pos.x) - 0.5;
			case WEST -> Math.round(pos.x) + 0.5;
			case NORTH, SOUTH, UP, DOWN -> Math.floor(pos.x) + 0.5;
		};
		double y = switch (facing) {
			case UP, DOWN -> Math.floor(pos.y);
			case NORTH, SOUTH, EAST, WEST -> Math.round(pos.y) - 1;
		};
		double z = switch (facing) {
			case NORTH -> Math.round(pos.z) + 0.5;
			case SOUTH -> Math.round(pos.z) - 0.5;
			case EAST, WEST, UP, DOWN -> Math.floor(pos.z) + 0.5;
		};

		return new Vec3(x, y, z);
	}

	/**
	 * Controls when animations should be played.
	 */
	private <T extends IAnimatable>PlayState predicate(AnimationEvent<T> event) {
		return PlayState.CONTINUE;
	}

	/**
	 * Sets up animation controllers.
	 */
	@Override
	public void registerControllers(AnimationData data) {
		AnimationController<PortalEntity> controller = new AnimationController(this, "controller", 0, this::predicate);
		data.addAnimationController(controller);
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
