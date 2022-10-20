package com.Legoman1342.blockentities.client;

import com.Legoman1342.blockentities.custom.ChamberlockDoorBE;
import com.Legoman1342.blocks.custom.ChamberlockDoor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class ChamberlockDoorModel extends AnimatedGeoModel<ChamberlockDoorBE> {
	@Override
	public ResourceLocation getModelLocation(ChamberlockDoorBE object) {
		return new ResourceLocation(MODID, switch (object.getBlockState().getValue(ChamberlockDoor.PART)) {
			case TOP_LEFT -> "geo/chamberlock_door/top_left.geo.json";
			case TOP_RIGHT -> "geo/chamberlock_door/top_right.geo.json";
			case BOTTOM_LEFT -> "geo/chamberlock_door/bottom_left.geo.json";
			case BOTTOM_RIGHT -> "geo/chamberlock_door/bottom_right.geo.json";
		});
	}

	@Override
	public ResourceLocation getTextureLocation(ChamberlockDoorBE object) {
		BlockState blockState = object.getBlockState();
		if (blockState.getValue(ChamberlockDoor.FRONT_CONDUCTIVE)) {
			if (blockState.getValue(ChamberlockDoor.BACK_CONDUCTIVE)) {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door/whitewhite.png");
			} else {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door/whiteblack.png");
			}
		} else {
			if (blockState.getValue(ChamberlockDoor.BACK_CONDUCTIVE)) {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door/blackwhite.png");
			} else {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door/blackblack.png");
			}
		}
	}

	@Override
	public ResourceLocation getAnimationFileLocation(ChamberlockDoorBE animatable) {
		return new ResourceLocation(MODID, "animations/chamberlock_door.animation.json");
	}
}
