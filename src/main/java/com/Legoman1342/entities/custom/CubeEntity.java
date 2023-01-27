package com.Legoman1342.entities.custom;

import com.Legoman1342.blocks.custom.SurfaceButton;
import com.Legoman1342.items.ItemRegistration;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;

public class CubeEntity extends Mob implements IAnimatable {
	/**
	 * Synced data is kept in sync between the client and the server.
	 */
	private static final EntityDataAccessor<Boolean> ACTIVATING = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FIZZLING = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> FIZZLE_TIMER = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> FIZZLE_PARTICLE_COUNTER = SynchedEntityData.defineId(CubeEntity.class, EntityDataSerializers.INT);

	/**
	 * List of damage sources that can kill CubeEntities.
	 */
	public static final ArrayList<DamageSource> CAN_KILL_CUBES = new ArrayList<>(List.of(
			DamageSource.OUT_OF_WORLD,
			DamageSource.LAVA,
			DamageSource.LIGHTNING_BOLT
	));

	private AnimationFactory factory = new AnimationFactory(this);

	public CubeEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	/**
	 * Defines and sets defaults for entity attributes.
	 */
	public static AttributeSupplier setAttributes() {
		return LivingEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, Double.POSITIVE_INFINITY)
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
		entityData.define(ACTIVATING, false);
		entityData.define(FIZZLING, false);
		entityData.define(FIZZLE_TIMER, 0);
		entityData.define(FIZZLE_PARTICLE_COUNTER, 0);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		super.tick();

		//If the cube is activating a pressure plate, turn the light ring orange
		BlockState feetBlockState = this.getFeetBlockState();
		if (isPoweredPressurePlateOrSurfaceButton(feetBlockState)) {
			entityData.set(ACTIVATING, true);
		} else {
			entityData.set(ACTIVATING, false);
		}

		//If the cube is fizzling, count down the timer until it reaches 0
		if (entityData.get(FIZZLE_TIMER) > 0) {
			entityData.set(FIZZLE_TIMER, entityData.get(FIZZLE_TIMER) - 1);
		}

		//Summons particles if the cube is fizzling
		if (level.isClientSide() && entityData.get(FIZZLING)){
			ClientLevel clientLevel = (ClientLevel) level;
			double speedMultiplier = 0.25;

			clientLevel.addParticle(ParticleTypes.SMOKE, true,
					position().x, position().y, position().z,
					(random.nextDouble() - 0.5) * speedMultiplier,
					random.nextDouble() * speedMultiplier,
					(random.nextDouble() - 0.5) * speedMultiplier);

			if (entityData.get(FIZZLE_PARTICLE_COUNTER) == 5) {
				entityData.set(FIZZLE_PARTICLE_COUNTER, 0);
				clientLevel.addParticle(ParticleTypes.END_ROD, true,
						position().x, position().y, position().z,
						(random.nextDouble() - 0.5) * speedMultiplier,
						random.nextDouble() * speedMultiplier,
						(random.nextDouble() - 0.5) * speedMultiplier);
			} else {
				entityData.set(FIZZLE_PARTICLE_COUNTER, entityData.get(FIZZLE_PARTICLE_COUNTER) + 1);
			}

			if (entityData.get(FIZZLE_TIMER) == 1) {
				clientLevel.addParticle(ParticleTypes.EXPLOSION, true,
						position().x, position().y, position().z,
						0, 0, 0);
			}
		}

		//If the fizzle timer just finished, kill the cube
		if (entityData.get(FIZZLING) && entityData.get(FIZZLE_TIMER) == 0) {
			entityData.set(FIZZLING, false);
			remove(RemovalReason.KILLED);
		}
	}

	/**
	 * Convenience method used to determine if a block is a powered pressure plate, weighted pressure plate, or surface button.
	 */
	public boolean isPoweredPressurePlateOrSurfaceButton(BlockState blockState) {
		Block block = blockState.getBlock();
		if (block instanceof PressurePlateBlock) {
			return blockState.getValue(PressurePlateBlock.POWERED);
		} else if (block instanceof WeightedPressurePlateBlock) {
			return blockState.getValue(WeightedPressurePlateBlock.POWER) > 0;
		} else if (block instanceof SurfaceButton) {
			return blockState.getValue(SurfaceButton.POWERED);
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
		level.playSound(null, position().x, position().y, position().z, SoundEvents.WITHER_DEATH, SoundSource.NEUTRAL, 0.35F, 1.5F);
	}

	/**
	 * Used to determine whether the fire effect should be rendered on the entity.
	 */
	@Override
	public boolean isOnFire() {
		return false;
	}

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 *
	 * @param target The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, empty ItemStack if nothing should be added.
	 */
	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(ItemRegistration.storage_cube.get());
	}



	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		if (pPlayer.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == ItemRegistration.configuration_tool.get()
				&& pPlayer.isCrouching()) {
			remove(RemovalReason.DISCARDED);
			level.playSound(null, position().x, position().y, position().z, SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 1, 1);
			if (!this.level.isClientSide) {
				ServerPlayer serverPlayer = (ServerPlayer) pPlayer;
				GameType gameMode = serverPlayer.gameMode.getGameModeForPlayer();
				if (gameMode == GameType.SURVIVAL || gameMode == GameType.ADVENTURE) {
					pPlayer.addItem(new ItemStack(ItemRegistration.storage_cube.get()));
				}
			}
		}
		return super.mobInteract(pPlayer, pHand);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	/**
	 * Controls when animations should be played.
	 */
	private <T extends IAnimatable>PlayState predicate(AnimationEvent<T> event) {
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
