package com.Legoman1342.blockentities.client;

import com.Legoman1342.blockentities.custom.SurfaceButtonBE;
import com.Legoman1342.blocks.custom.SurfaceButton;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class SurfaceButtonRenderer extends GeoBlockRenderer<SurfaceButtonBE> {
	public SurfaceButtonRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new SurfaceButtonModel());
	}

	@Override
	public ResourceLocation getTextureLocation(SurfaceButtonBE instance) {
		return new ResourceLocation(MODID, "textures/block/surface_button/" +
				(instance.getBlockState().getValue(SurfaceButton.POWERED) ? "activated" : "deactivated")
				+ ".png");
	}

	/**
	 * Sets the render type. <br>
	 * GeckoLib block entities override this method instead of using {@link com.Legoman1342.aperturetech.ApertureTech#clientSetup(FMLClientSetupEvent) com.Legoman1342.aperturetech.ApertureTech#clientSetup}.
	 */
	@Override
	public RenderType getRenderType(SurfaceButtonBE animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}
