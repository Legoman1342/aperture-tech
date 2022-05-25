package com.Legoman1342.setup;

import com.Legoman1342.blocks.catwalk;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class Registration {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	
	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(bus);
		BLOCKS.register(bus);
	}
	
	//Items
	
	
	//Blocks
	public static final RegistryObject<catwalk> catwalk = BLOCKS.register("catwalk", catwalk::new);
	
	//Block items
	public static final RegistryObject<Item> catwalk_item = ITEMS.register("catwalk", () -> new BlockItem(catwalk.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
}
