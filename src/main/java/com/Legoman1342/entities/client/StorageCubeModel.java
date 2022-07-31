package com.Legoman1342.entities.client;

import com.Legoman1342.entities.custom.CubeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class StorageCubeModel extends AnimatedGeoModel<CubeEntity> {
	@Override
	public ResourceLocation getModelLocation(CubeEntity object) {
		return new ResourceLocation(MODID, "models/entity/weighted_cube.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CubeEntity object) {
		return new ResourceLocation(MODID, "textures/entity/storage_cube.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(CubeEntity animatable) {
		return new ResourceLocation(MODID, "models/entity/weighted_cube.animation.json");
	}
}
