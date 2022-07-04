package com.Legoman1342.datagen;

import com.Legoman1342.aperturetech.ApertureTech;
import com.Legoman1342.blocks.custom.Catwalk;
import com.Legoman1342.blocks.custom.Catwalk.CatwalkEnd;
import com.Legoman1342.setup.Registration;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.Legoman1342.blocks.custom.Catwalk.*;

public class ModBlocksStateProvider extends BlockStateProvider {
	public ModBlocksStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, ApertureTech.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		//Catwalk multipart models
		ExistingModelFile catwalkFloorCenterAttach = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_center_attach"));
		ExistingModelFile catwalkFloorCenterRailing = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_center_railing"));
		ExistingModelFile catwalkFloorLeftAttach = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_left_attach"));
		ExistingModelFile catwalkFloorLeftAttachFlipped = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_left_attach_flipped"));
		ExistingModelFile catwalkFloorLeftRailing = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_left_railing"));
		ExistingModelFile catwalkFloorRightAttach = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_right_attach"));
		ExistingModelFile catwalkFloorRightAttachFlipped = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_right_attach_flipped"));
		ExistingModelFile catwalkFloorRightRailing = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/floor_right_railing"));
		ExistingModelFile catwalkPosts = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/posts"));
		ExistingModelFile catwalkRailingsDrop = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/railings_drop"));


		//Build catwalk
		this.getMultipartBuilder(Registration.catwalk.get())

				//catwalkFloorCenterAttach
				.part().modelFile(catwalkFloorCenterAttach)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.DROP)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterAttach).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.DROP)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterAttach).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.DROP)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterAttach).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.DROP)
						.condition(FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorCenterRailing
				.part().modelFile(catwalkFloorCenterRailing)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.RAILING)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterRailing).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.RAILING)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterRailing).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.RAILING)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterRailing).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.RAILING)
						.condition(FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorLeftAttach
				.part().modelFile(catwalkFloorLeftAttach)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttach).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttach).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttach).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorLeftAttachFlipped
				.part().modelFile(catwalkFloorLeftAttachFlipped)
				.addModel().nestedGroup()
				.condition(CATWALK_LEFT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.NORTH)
				.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttachFlipped).rotationY(90)
				.addModel().nestedGroup()
				.condition(CATWALK_LEFT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.EAST)
				.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttachFlipped).rotationY(180)
				.addModel().nestedGroup()
				.condition(CATWALK_LEFT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.SOUTH)
				.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttachFlipped).rotationY(270)
				.addModel().nestedGroup()
				.condition(CATWALK_LEFT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.WEST)
				.end()
				.end()

				//catwalkFloorLeftRailing
				.part().modelFile(catwalkFloorLeftRailing)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftRailing).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftRailing).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftRailing).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorRightAttach
				.part().modelFile(catwalkFloorRightAttach)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttach).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttach).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttach).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.ATTACH)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorRightAttachFlipped
				.part().modelFile(catwalkFloorRightAttachFlipped)
				.addModel().nestedGroup()
				.condition(CATWALK_RIGHT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.NORTH)
				.end()
				.end()
				.part().modelFile(catwalkFloorRightAttachFlipped).rotationY(90)
				.addModel().nestedGroup()
				.condition(CATWALK_RIGHT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.EAST)
				.end()
				.end()
				.part().modelFile(catwalkFloorRightAttachFlipped).rotationY(180)
				.addModel().nestedGroup()
				.condition(CATWALK_RIGHT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.SOUTH)
				.end()
				.end()
				.part().modelFile(catwalkFloorRightAttachFlipped).rotationY(270)
				.addModel().nestedGroup()
				.condition(CATWALK_RIGHT, CatwalkSides.ATTACH_FLIPPED)
				.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.condition(FACING, Direction.WEST)
				.end()
				.end()

				//catwalkFloorRightRailing
				.part().modelFile(catwalkFloorRightRailing)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightRailing).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightRailing).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightRailing).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, CatwalkSides.RAILING)
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
						.condition(FACING, Direction.WEST)
					.end()
				.end()

				//catwalkPosts
				.part().modelFile(catwalkPosts)
					.addModel()
						.condition(CATWALK_END, CatwalkEnd.ATTACH, CatwalkEnd.RAILING)
				.end()

				//catwalkRailingsDrop
				.part().modelFile(catwalkRailingsDrop)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.DROP)
						.condition(FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkRailingsDrop).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.DROP)
						.condition(FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkRailingsDrop).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.DROP)
						.condition(FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkRailingsDrop).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_END, CatwalkEnd.DROP)
						.condition(FACING, Direction.WEST)
					.end()
				.end();

	}
}
