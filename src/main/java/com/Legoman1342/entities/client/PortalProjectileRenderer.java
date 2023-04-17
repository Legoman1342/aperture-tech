package com.Legoman1342.entities.client;

import com.Legoman1342.entities.custom.PortalProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PortalProjectileRenderer extends EntityRenderer<PortalProjectile> {
	public PortalProjectileRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager);
		this.shadowRadius = 0f;
	}

	@Override
	public ResourceLocation getTextureLocation(PortalProjectile pEntity) {
		return null;
	}
}
