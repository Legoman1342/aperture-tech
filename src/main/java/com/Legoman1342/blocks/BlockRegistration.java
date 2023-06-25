package com.Legoman1342.blocks;

import com.Legoman1342.blocks.custom.Catwalk;
import com.Legoman1342.blocks.custom.CatwalkStairs;
import com.Legoman1342.blocks.custom.ChamberlockDoor;
import com.Legoman1342.blocks.custom.SurfaceButton;
import com.Legoman1342.setup.ATCreativeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class BlockRegistration {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(bus);
		ITEMS.register(bus);
	}

	public static final RegistryObject<Block> CATWALK = registerBlock("catwalk", () -> new Catwalk(
			BlockBehaviour
					.Properties.of(Material.METAL)
					.sound(SoundType.LANTERN)
					.strength(2.0f)
					.noOcclusion()),
			ATCreativeTab.AT_CREATIVE_TAB);
	public static final RegistryObject<Block> CATWALK_STAIRS = registerBlock("catwalk_stairs", () -> new CatwalkStairs(
			BlockBehaviour
					.Properties.copy(CATWALK.get())),
			ATCreativeTab.AT_CREATIVE_TAB);
	public static final RegistryObject<Block> CHAMBERLOCK_DOOR = registerBlock("chamberlock_door", () -> new ChamberlockDoor(
			BlockBehaviour
					.Properties.of(Material.METAL)
					.sound(SoundType.METAL)
					.strength(2.0f)
					.noOcclusion()),
			ATCreativeTab.AT_CREATIVE_TAB);
	public static final RegistryObject<Block> SURFACE_BUTTON = registerBlock("surface_button", () -> new SurfaceButton(
			BlockBehaviour
					.Properties.of(Material.METAL)
					.sound(SoundType.METAL)
					.strength(2.0f)
					.noOcclusion()),
			ATCreativeTab.AT_CREATIVE_TAB);



	/**
	 * Registers a block and a corresponding block item.
	 */
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, tab);
		return toReturn;
	}

	/**
	 * Registers a block and a corresponding block item with a tooltip.
	 */
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block,
	                                                                 CreativeModeTab tab, String tooltipKey) {
		RegistryObject<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn, tab, tooltipKey);
		return toReturn;
	}

	/**
	 * Registers a block without registering a block item.
	 */
	private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
		return BLOCKS.register(name, block);
	}

	/**
	 * Registers a block item.
	 */
	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
	                                                                        CreativeModeTab tab) {
		return ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().tab(tab)));
	}

	/**
	 *Registers a block item with a tooltip.
	 */
	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
	                                                                        CreativeModeTab tab, String tooltipKey) {
		return ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().tab(tab)) {
			@Override
			public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
				pTooltip.add(Component.translatable(tooltipKey));
			}
		});
	}


}
