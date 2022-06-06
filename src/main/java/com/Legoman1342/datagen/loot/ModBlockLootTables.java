package com.Legoman1342.datagen.loot;

import com.Legoman1342.setup.Registration;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {
	@Override
	protected void addTables() {
		//Add loot tables for every block here
		this.dropSelf(Registration.catwalk.get());
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
	}
}
