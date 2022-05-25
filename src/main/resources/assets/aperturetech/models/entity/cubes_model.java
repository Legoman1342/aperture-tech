// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


public class custom_model<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "custom_model"), "main");
	private final ModelPart TopNortheast;
	private final ModelPart TopNorthwest;
	private final ModelPart TopSoutheast;
	private final ModelPart TopSouthwest;
	private final ModelPart BottomNortheast;
	private final ModelPart BottomNorthwest;
	private final ModelPart BottomSoutheast;
	private final ModelPart BottomSouthwest;
	private final ModelPart bb_main;

	public custom_model(ModelPart root) {
		this.TopNortheast = root.getChild("TopNortheast");
		this.TopNorthwest = root.getChild("TopNorthwest");
		this.TopSoutheast = root.getChild("TopSoutheast");
		this.TopSouthwest = root.getChild("TopSouthwest");
		this.BottomNortheast = root.getChild("BottomNortheast");
		this.BottomNorthwest = root.getChild("BottomNorthwest");
		this.BottomSoutheast = root.getChild("BottomSoutheast");
		this.BottomSouthwest = root.getChild("BottomSouthwest");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition TopNortheast = partdefinition.addOrReplaceChild("TopNortheast", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 23.5F, -0.5F));

		PartDefinition 7_r1 = TopNortheast.addOrReplaceChild("7_r1", CubeListBuilder.create().texOffs(39, 6).addBox(9.5F, -6.5F, -6.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(10.5F, -6.5F, -6.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 5_r1 = TopNortheast.addOrReplaceChild("5_r1", CubeListBuilder.create().texOffs(39, 6).addBox(1.5F, -14.5F, -6.5F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(2.5F, -14.5F, -6.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.5F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition TopNorthwest = partdefinition.addOrReplaceChild("TopNorthwest", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 23.5F, -0.5F, 0.0F, -1.5708F, 0.0F));

		PartDefinition 8_r1 = TopNorthwest.addOrReplaceChild("8_r1", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 6_r1 = TopNorthwest.addOrReplaceChild("6_r1", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition TopSoutheast = partdefinition.addOrReplaceChild("TopSoutheast", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 23.5F, 0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition 10_r1 = TopSoutheast.addOrReplaceChild("10_r1", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 8_r2 = TopSoutheast.addOrReplaceChild("8_r2", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition TopSouthwest = partdefinition.addOrReplaceChild("TopSouthwest", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 23.5F, 0.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition 9_r1 = TopSouthwest.addOrReplaceChild("9_r1", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 7_r2 = TopSouthwest.addOrReplaceChild("7_r2", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition BottomNortheast = partdefinition.addOrReplaceChild("BottomNortheast", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, 16.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 8_r3 = BottomNortheast.addOrReplaceChild("8_r3", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 6_r2 = BottomNortheast.addOrReplaceChild("6_r2", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition BottomNorthwest = partdefinition.addOrReplaceChild("BottomNorthwest", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 16.5F, 7.5F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition 9_r2 = BottomNorthwest.addOrReplaceChild("9_r2", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 7_r3 = BottomNorthwest.addOrReplaceChild("7_r3", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition BottomSoutheast = partdefinition.addOrReplaceChild("BottomSoutheast", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 16.5F, -7.5F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition 11_r1 = BottomSoutheast.addOrReplaceChild("11_r1", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 9_r3 = BottomSoutheast.addOrReplaceChild("9_r3", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition BottomSouthwest = partdefinition.addOrReplaceChild("BottomSouthwest", CubeListBuilder.create().texOffs(0, 7).addBox(-7.0F, -15.0F, -7.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(-4.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(39, 6).addBox(-3.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, 16.5F, 0.5F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition 10_r2 = BottomSouthwest.addOrReplaceChild("10_r2", CubeListBuilder.create().texOffs(39, 6).addBox(10.0F, -7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(11.0F, -7.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition 8_r4 = BottomSouthwest.addOrReplaceChild("8_r4", CubeListBuilder.create().texOffs(39, 6).addBox(2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(39, 0).addBox(3.0F, -15.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -14.5F, -6.5F, 13.0F, 13.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -3.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-6.0F, -14.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Southwest_r1 = bb_main.addOrReplaceChild("Southwest_r1", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, 5.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition Southeast_r1 = bb_main.addOrReplaceChild("Southeast_r1", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, 5.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition Northwest_r1 = bb_main.addOrReplaceChild("Northwest_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 5.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 1.5708F));

		PartDefinition Northeast_r1 = bb_main.addOrReplaceChild("Northeast_r1", CubeListBuilder.create().texOffs(0, 0).addBox(6.0F, 5.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition TopWest_r1 = bb_main.addOrReplaceChild("TopWest_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 5.0F, -15.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, -1.5708F, 0.0F));

		PartDefinition TopSouth_r1 = bb_main.addOrReplaceChild("TopSouth_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 5.0F, -15.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 3.1416F, 0.0F));

		PartDefinition TopEast_r1 = bb_main.addOrReplaceChild("TopEast_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 5.0F, -15.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 1.5708F, 0.0F));

		PartDefinition TopNorth_r1 = bb_main.addOrReplaceChild("TopNorth_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 5.0F, -15.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition BottomWest_r1 = bb_main.addOrReplaceChild("BottomWest_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition BottomSouth_r1 = bb_main.addOrReplaceChild("BottomSouth_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition BottomEast_r1 = bb_main.addOrReplaceChild("BottomEast_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -7.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		TopNortheast.render(poseStack, buffer, packedLight, packedOverlay);
		TopNorthwest.render(poseStack, buffer, packedLight, packedOverlay);
		TopSoutheast.render(poseStack, buffer, packedLight, packedOverlay);
		TopSouthwest.render(poseStack, buffer, packedLight, packedOverlay);
		BottomNortheast.render(poseStack, buffer, packedLight, packedOverlay);
		BottomNorthwest.render(poseStack, buffer, packedLight, packedOverlay);
		BottomSoutheast.render(poseStack, buffer, packedLight, packedOverlay);
		BottomSouthwest.render(poseStack, buffer, packedLight, packedOverlay);
		bb_main.render(poseStack, buffer, packedLight, packedOverlay);
	}
}