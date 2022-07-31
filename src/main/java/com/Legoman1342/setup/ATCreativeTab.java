package com.Legoman1342.setup;

import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.items.ItemRegistration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ATCreativeTab {
	public static final CreativeModeTab AT_CREATIVE_TAB = new CreativeModeTab("atcreativetab") {
		@Override
		public ItemStack makeIcon() {
			//TODO Change this to a better item eventually
			return new ItemStack(ItemRegistration.configuration_tool.get());
		}
	};
}
