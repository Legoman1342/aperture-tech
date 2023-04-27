package com.Legoman1342.entities.custom;

import com.Legoman1342.aperturetech.PortalChannel;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class PortalProjectile extends Projectile {

	private static final EntityDataAccessor<Integer> CHANNEL = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(PortalProjectile.class, EntityDataSerializers.BYTE);
	private static final Vector3f BLUE_DUST_COLOR = new Vector3f(0F, 0F, 1F);
	private static final Vector3f ORANGE_DUST_COLOR = new Vector3f(1F, 0.55F, 0F);

	public PortalProjectile(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(CHANNEL, 0);
		this.entityData.define(ID_FLAGS, (byte)0);
	}

	@Override
	public void tick() {
		level.addParticle(new DustParticleOptions(BLUE_DUST_COLOR, 1F), true, position().x, position().y, position().z, 0, 0, 0);
	}
}
