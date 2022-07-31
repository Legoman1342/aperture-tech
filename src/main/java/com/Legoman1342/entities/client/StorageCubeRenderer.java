package com.Legoman1342.entities.client;

import com.Legoman1342.entities.custom.CubeEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class StorageCubeRenderer extends EntityRenderer<CubeEntity> {
	public StorageCubeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager);
		this.shadowRadius = 0.45f;
	}

	@Override
	public ResourceLocation getTextureLocation(CubeEntity instance) {
		return new ResourceLocation(MODID, "textures/entity/storage_cube.png");
	}
}
