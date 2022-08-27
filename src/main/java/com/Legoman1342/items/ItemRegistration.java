package com.Legoman1342.items;

import com.Legoman1342.items.custom.ConfigurationTool;
import com.Legoman1342.items.custom.StorageCubeItem;
import com.Legoman1342.setup.ATCreativeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class ItemRegistration {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(bus);
	}

	public static final RegistryObject<Item> configuration_tool = ITEMS.register("configuration_tool",
			() -> new ConfigurationTool(new Item.Properties().tab(ATCreativeTab.AT_CREATIVE_TAB).stacksTo(1)));

	public static final RegistryObject<Item> storage_cube = ITEMS.register("storage_cube",
			() -> new StorageCubeItem(new Item.Properties().tab(ATCreativeTab.AT_CREATIVE_TAB).stacksTo(1)));
}
