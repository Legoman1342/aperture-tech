package com.Legoman1342.datagen;

import com.Legoman1342.aperturetech.ApertureTech;
import com.Legoman1342.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {
	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, ApertureTech.MODID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() { //Add all register functions here
		registerCatwalk();
	}
	
	private void registerCatwalk() {
		ResourceLocation catwalkTexture = new ResourceLocation(ApertureTech.MODID, "block/catwalk_aperturehalls");
		ModelFile catwalkModel = new ModelFile(new ResourceLocation(ApertureTech.MODID, "models/catwalk_aperturehalls_attach")) {
			@Override
			protected boolean exists() {
				return true;
			}
		}; //TODO I don't think this is right, look more at other examples
		
		horizontalBlock(Registration.catwalk.get(), catwalkModel);  //TODO Add multiple horizontalBlock calls in if statements for multiple block states
	}
}
