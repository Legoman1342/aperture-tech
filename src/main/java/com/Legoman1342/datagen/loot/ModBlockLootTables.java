package com.Legoman1342.datagen.loot;

import com.Legoman1342.blocks.BlockRegistration;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {
	@Override
	protected void addTables() {
		//Add loot tables for every block here
		this.dropSelf(BlockRegistration.catwalk.get());
		this.dropSelf(BlockRegistration.catwalk_stairs.get());
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		return BlockRegistration.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
	}
}
