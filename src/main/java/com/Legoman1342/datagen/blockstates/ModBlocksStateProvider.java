//Useful Forge docs page:
//https://docs.minecraftforge.net/en/1.18.x/datagen/client/modelproviders/

package com.Legoman1342.datagen.blockstates;

import com.Legoman1342.aperturetech.ApertureTech;
import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.blocks.custom.Catwalk;
import com.Legoman1342.blocks.custom.Catwalk.ATCatwalkEnd;
import com.Legoman1342.blocks.custom.CatwalkStairs;
import com.Legoman1342.blocks.custom.SurfaceButton;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.Legoman1342.blocks.custom.Catwalk.*;
import static com.Legoman1342.blocks.custom.CatwalkStairs.*;

public class ModBlocksStateProvider extends BlockStateProvider {
	public ModBlocksStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, ApertureTech.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		registerCatwalk();
		registerCatwalkStairs();
		registerChamberlockDoor();
		registerSurfaceButton();
	}

	protected void registerCatwalk() {
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
		this.getMultipartBuilder(BlockRegistration.CATWALK.get())

				//catwalkFloorCenterAttach
				.part().modelFile(catwalkFloorCenterAttach)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterAttach).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterAttach).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterAttach).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorCenterRailing
				.part().modelFile(catwalkFloorCenterRailing)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterRailing).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterRailing).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorCenterRailing).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorLeftAttach
				.part().modelFile(catwalkFloorLeftAttach)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttach).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttach).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttach).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorLeftAttachFlipped
				.part().modelFile(catwalkFloorLeftAttachFlipped)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttachFlipped).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttachFlipped).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftAttachFlipped).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorLeftRailing
				.part().modelFile(catwalkFloorLeftRailing)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftRailing).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftRailing).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorLeftRailing).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_LEFT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorRightAttach
				.part().modelFile(catwalkFloorRightAttach)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttach).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttach).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttach).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorRightAttachFlipped
				.part().modelFile(catwalkFloorRightAttachFlipped)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttachFlipped).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttachFlipped).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightAttachFlipped).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.ATTACH_FLIPPED)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkFloorRightRailing
				.part().modelFile(catwalkFloorRightRailing)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightRailing).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightRailing).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkFloorRightRailing).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_RIGHT, ATCatwalkSides.RAILING)
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end()

				//catwalkPosts
				.part().modelFile(catwalkPosts)
					.addModel()
						.condition(CATWALK_END, ATCatwalkEnd.ATTACH, ATCatwalkEnd.RAILING)
				.end()

				//catwalkRailingsDrop
				.part().modelFile(catwalkRailingsDrop)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.NORTH)
					.end()
				.end()
				.part().modelFile(catwalkRailingsDrop).rotationY(90)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.EAST)
					.end()
				.end()
				.part().modelFile(catwalkRailingsDrop).rotationY(180)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.SOUTH)
					.end()
				.end()
				.part().modelFile(catwalkRailingsDrop).rotationY(270)
					.addModel().nestedGroup()
						.condition(CATWALK_END, ATCatwalkEnd.DROP)
						.condition(Catwalk.FACING, Direction.WEST)
					.end()
				.end();
	}

	protected void registerCatwalkStairs() {
		//Catwalk stairs models
		ExistingModelFile catwalkStairsLower = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/stairs_lower"));
		ExistingModelFile catwalkStairsUpper = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/catwalk/stairs_upper"));


		this.getVariantBuilder(BlockRegistration.CATWALK_STAIRS.get())
				.forAllStates(state ->
					ConfiguredModel.builder()
							.modelFile(switch (state.getValue(HALF)) {
								case LOWER -> catwalkStairsLower;
								case UPPER -> catwalkStairsUpper;
							})
							.rotationY((int) state.getValue(CatwalkStairs.FACING).getOpposite().toYRot())
							.build()
				);

	}

	protected void registerChamberlockDoor() {
		this.getVariantBuilder(BlockRegistration.CHAMBERLOCK_DOOR.get())
				.forAllStates(state ->
					ConfiguredModel.builder()
							.modelFile(models().getExistingFile(
									new ResourceLocation(ApertureTech.MODID, "block/chamberlock_door")))
							.build()
				);
	}

	protected void registerSurfaceButton() {
		ExistingModelFile bottomLeftTopRightActivated = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/surface_button/bl_tr_activated")
		);
		ExistingModelFile bottomLeftTopRightDeactivated = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/surface_button/bl_tr_deactivated")
		);
		ExistingModelFile bottomRightTopLeftActivated = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/surface_button/br_tl_activated")
		);
		ExistingModelFile bottomRightTopLeftDeactivated = models().getExistingFile(
				new ResourceLocation(ApertureTech.MODID, "block/surface_button/br_tl_deactivated")
		);


		this.getMultipartBuilder(BlockRegistration.SURFACE_BUTTON.get())

				//bottomLeftTopRightActivated
				.part().modelFile(bottomLeftTopRightActivated)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(180).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(270).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(90).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(90).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(270).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(90).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightActivated).rotationX(270).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()



				//bottomLeftTopRightDeactivated
				.part().modelFile(bottomLeftTopRightDeactivated)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(180).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(270).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(90).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(90).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(270).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(90).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomLeftTopRightDeactivated).rotationX(270).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()



				//bottomRightTopLeftActivated
				.part().modelFile(bottomRightTopLeftActivated)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(180).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(270).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(90).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(90).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(270).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(90).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftActivated).rotationX(270).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, true)
					.end()
				.end()



				//bottomRightTopLeftDeactivated
				.part().modelFile(bottomRightTopLeftDeactivated)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.UP)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(180).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.DOWN)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(270).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.NORTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(90).rotationY(180)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.SOUTH)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(90).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(270).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.EAST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(90).rotationY(270)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.BOTTOM_RIGHT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end()

				.part().modelFile(bottomRightTopLeftDeactivated).rotationX(270).rotationY(90)
					.addModel().nestedGroup()
						.condition(SurfaceButton.FACING, Direction.WEST)
						.condition(SurfaceButton.PART, SurfaceButton.ATButtonPart.TOP_LEFT)
						.condition(SurfaceButton.POWERED, false)
					.end()
				.end();
	}
}
