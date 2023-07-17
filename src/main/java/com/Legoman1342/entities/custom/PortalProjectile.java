package com.Legoman1342.entities.custom;

import com.Legoman1342.aperturetech.PortalChannel;
import com.Legoman1342.aperturetech.PortalChannelStorage;
import com.Legoman1342.entities.EntityRegistration;
import com.Legoman1342.utilities.ColorUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class PortalProjectile extends Projectile {

	private static final Logger LOGGER = LogManager.getLogger(); //TODO Used for debugging, remove

	private static final EntityDataAccessor<Integer> CHANNEL = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.BYTE);

	private PortalProjectile(Player player, PortalChannel channel, Vec3 pos, float rotX, float rotY) {
		this(EntityRegistration.PORTAL_PROJECTILE.get(), player.level);
		setChannel(channel);
		moveTo(pos.x(), pos.y(), pos.z(), rotX, rotY);
		setDeltaMovement(calculateVelocity(getXRot(), getYRot()));
		LOGGER.info("Hello from PortalProjectile!");
		LOGGER.info("X rotation: " + getXRot());
		LOGGER.info("Y rotation: " + getYRot());
		LOGGER.info("Calculated velocity: " + calculateVelocity(getXRot(), getYRot()));
		LOGGER.info("Velocity: " + getDeltaMovement());
	}

	public PortalProjectile(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * Returns a new portal projectile.
	 * The constructor that this method uses has to be private to make some code in {@link EntityRegistration} work, so this
	 * method exists to provide access to it.
	 */
	public static PortalProjectile newPortalProjectile(Player player, PortalChannel channel, Vec3 pos, float rotX, float rotY) {
		return new PortalProjectile(player, channel, pos, rotX, rotY);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(CHANNEL, -1); //Default state, signifies that the portal channel hasn't been set
		this.entityData.define(ID_FLAGS, (byte)0);
	}

	/**
	 * Calculates the velocity the projectile should have based on its rotation. Modified from <a href="https://github.com/VazkiiMods/Botania/blob/1.18.x/Xplat/src/main/java/vazkii/botania/common/entity/EntityManaBurst.java">Botania's mana bursts</a>.
	 */
	public static Vec3 calculateVelocity(float xRot, float yRot) {
		float multiplier = 2F;
		double xVel = -Math.sin(xRot / 180 * Math.PI) * Math.cos(yRot / 180 * Math.PI) * multiplier;
		double yVel = -Math.sin(yRot / 180 * Math.PI) * multiplier;
		double zVel = Math.cos(xRot / 180 * Math.PI) * Math.cos(yRot / 180 * Math.PI) * multiplier;
		return new Vec3(xVel, yVel, zVel);
	}

	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();

		if (this.entityData.get(CHANNEL) == -1) { //If the channel hasn't been set yet...
			//Get the projectile's channel from PortalChannelStorage and puts it into synched entity data
			Map<UUID, PortalChannel> allChannelUsers = PortalChannelStorage.getAllChannelUsers();
			if (allChannelUsers.containsKey(this.uuid)) {
				setChannel(allChannelUsers.get(this.uuid));
			}
		} else if (this.entityData.get(CHANNEL) > -1) { //Otherwise...
			//Add this projectile as a user
			PortalChannelStorage.getPortalChannel(this.entityData.get(CHANNEL)).addUser(this.uuid);
			PortalChannelStorage.setDirty();
		}
	}



	@Override
	public void remove(Entity.RemovalReason pReason) {
		super.remove(pReason);
		if (pReason == RemovalReason.KILLED) {
			if (this.entityData.get(CHANNEL) != -1) {
				PortalChannelStorage.getPortalChannel(this.entityData.get(CHANNEL)).removeUser(this.uuid);
				PortalChannelStorage.setDirty();
			}
		}
	}

	@Override
	public void tick() {
		if (this.getChannel() != null) {
			Color primaryColor = this.getChannel().getPrimaryColor();
			level.addParticle(new DustParticleOptions(ColorUtils.toVector3f(primaryColor), 1F), true, position().x, position().y, position().z, 0, 0, 0);
		}

		Vec3 velocity = getDeltaMovement();
		this.setPos(this.getX() + velocity.x(), this.getY() + velocity.y(), this.getZ() + velocity.z());
	}

	/**
	 * Sets the channel ID in synched entity data to the ID of the given channel.
	 */
	public void setChannel(PortalChannel channel) {
		this.entityData.set(CHANNEL, channel.getId());
	}

	public PortalChannel getChannel() {
		if (this.entityData.get(CHANNEL) >= 0) {
			return PortalChannelStorage.getPortalChannel(this.entityData.get(CHANNEL));
		} else {
			return null;
		}
	}
}
