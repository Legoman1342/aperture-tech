package com.Legoman1342.entities.custom;

import com.Legoman1342.aperturetech.PortalChannel;
import com.Legoman1342.aperturetech.PortalChannelStorage;
import com.Legoman1342.entities.EntityRegistration;
import com.Legoman1342.utilities.ColorUtils;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class PortalProjectile extends Projectile {

	private static final EntityDataAccessor<Integer> CHANNEL = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.BYTE);

	private PortalProjectile(Player player, PortalChannel channel) {
		this(EntityRegistration.PORTAL_PROJECTILE.get(), player.level);
		setChannel(channel);
	}

	public PortalProjectile(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * Returns a new portal projectile.
	 * The constructor that this method uses has to be private to make some code in {@link EntityRegistration} work, so this
	 * method exists to provide access to it.
	 */
	public static PortalProjectile newPortalProjectile(Player player, PortalChannel channel) {
		return new PortalProjectile(player, channel);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(CHANNEL, -1); //Default state, signifies that the portal channel hasn't been set
		this.entityData.define(ID_FLAGS, (byte)0);
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
