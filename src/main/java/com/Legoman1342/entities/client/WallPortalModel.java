package com.Legoman1342.entities.client;

import com.Legoman1342.entities.custom.CubeEntity;
import com.Legoman1342.entities.custom.PortalEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class WallPortalModel extends AnimatedGeoModel<PortalEntity> {
	@Override
	public ResourceLocation getModelResource(PortalEntity object) {
		return new ResourceLocation(MODID, "geo/portal/wall_portal.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(PortalEntity object) {
		return new ResourceLocation(MODID, "textures/entity/portal.png");
	}

	@Override
	public ResourceLocation getAnimationResource(PortalEntity animatable) {
		return new ResourceLocation(MODID, "animations/portal.animation.json");
	}
}
