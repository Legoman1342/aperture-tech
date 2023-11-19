package com.Legoman1342.entities.client;

import com.Legoman1342.entities.custom.CubeEntity;
import com.Legoman1342.entities.custom.PortalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class WallPortalRenderer extends GeoEntityRenderer<PortalEntity> {
	public WallPortalRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new WallPortalModel());
		this.shadowRadius = 0.0f;
	}

	@Override
	public ResourceLocation getTextureLocation(PortalEntity instance) {
		return new ResourceLocation(MODID, "textures/entity/portal.png");
	}

	@Override
	public RenderType getRenderType(PortalEntity animatable, float partialTicks, PoseStack stack,
	                                @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
	                                int packedLightIn, ResourceLocation textureLocation) {
		stack.scale(1.0f, 1.0f, 1.0f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}
