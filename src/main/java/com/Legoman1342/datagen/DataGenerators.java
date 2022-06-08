package com.Legoman1342.datagen;

import com.Legoman1342.aperturetech.ApertureTech;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ApertureTech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		
		//Generate recipes
		generator.addProvider(new ModRecipeProvider(generator));
		//Generate loot tables
		generator.addProvider(new ModLootTableProvider(generator));
		//Generate tags
		generator.addProvider(new ModBlockTagsProvider(generator, ApertureTech.MODID, existingFileHelper));
		
	}
}
