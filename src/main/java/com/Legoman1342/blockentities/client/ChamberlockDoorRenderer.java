package com.Legoman1342.blockentities.client;

import com.Legoman1342.blockentities.custom.ChamberlockDoorBE;
import com.Legoman1342.blocks.custom.ChamberlockDoor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class ChamberlockDoorRenderer extends GeoBlockRenderer<ChamberlockDoorBE> {
	public ChamberlockDoorRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new ChamberlockDoorModel());
	}

	@Override
	public ResourceLocation getTextureLocation(ChamberlockDoorBE instance) {
		BlockState blockState = instance.getBlockState();
		if (blockState.getValue(ChamberlockDoor.FRONT_CONDUCTIVE)) {
			if (blockState.getValue(ChamberlockDoor.BACK_CONDUCTIVE)) {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door_whitewhite.png");
			} else {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door_whiteblack.png");
			}
		} else {
			if (blockState.getValue(ChamberlockDoor.BACK_CONDUCTIVE)) {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door_blackwhite.png");
			} else {
				return new ResourceLocation(MODID, "textures/block/chamberlock_door_blackblack.png");
			}
		}
	}

	/**
	 * Sets the render type. <br>
	 * GeckoLib block entities override this method instead of using {@link com.Legoman1342.aperturetech.ApertureTech#clientSetup(FMLClientSetupEvent) com.Legoman1342.aperturetech.ApertureTech#clientSetup}.
	 */
	@Override
	public RenderType getRenderType(ChamberlockDoorBE animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
