package com.Legoman1342.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ATCreativeTab {
	public static final CreativeModeTab AT_CREATIVE_TAB = new CreativeModeTab("atcreativetab") {
		@Override
		public ItemStack makeIcon() {
			//TODO Change this to a better item eventually
			return new ItemStack(Registration.catwalk.get());
		}
	};
}
