package com.Legoman1342.blockentities.client;

import com.Legoman1342.blockentities.custom.SurfaceButtonBE;
import com.Legoman1342.blocks.custom.SurfaceButton;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class SurfaceButtonModel extends AnimatedGeoModel<SurfaceButtonBE> {
	@Override
	public ResourceLocation getModelLocation(SurfaceButtonBE object) {
		String partToReturn = switch (object.getBlockState().getValue(SurfaceButton.PART)) {
			case TOP_LEFT -> "top_left";
			case TOP_RIGHT -> "top_right";
			case BOTTOM_LEFT -> "bottom_left";
			case BOTTOM_RIGHT -> "bottom_right";
		};
		return new ResourceLocation(MODID, "geo/surface_button/" + partToReturn + ".geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(SurfaceButtonBE object) {
		return new ResourceLocation(MODID, "textures/block/surface_button/" +
				(object.getBlockState().getValue(SurfaceButton.POWERED) ? "activated" : "deactivated")
		+ ".png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation(SurfaceButtonBE animatable) {
		return new ResourceLocation(MODID, "animations/surface_button.animation.json");
	}
}
