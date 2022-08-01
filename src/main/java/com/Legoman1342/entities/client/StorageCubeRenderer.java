package com.Legoman1342.entities.client;

import com.Legoman1342.entities.custom.CubeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class StorageCubeRenderer extends GeoEntityRenderer<CubeEntity> {
	public StorageCubeRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new StorageCubeModel());
		this.shadowRadius = 0.45f;
	}

	@Override
	public ResourceLocation getTextureLocation(CubeEntity instance) {
		return new ResourceLocation(MODID, "textures/entity/storage_cube.png");
	}

	@Override
	public RenderType getRenderType(CubeEntity animatable, float partialTicks, PoseStack stack,
	                                @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder,
	                                int packedLightIn, ResourceLocation textureLocation) {
		stack.scale(1.0f, 1.0f, 1.0f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}
