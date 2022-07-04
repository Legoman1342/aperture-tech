package com.Legoman1342.items.custom;

import com.Legoman1342.blocks.custom.Catwalk;
import com.Legoman1342.blocks.custom.Catwalk.CatwalkEnd;
import com.Legoman1342.blocks.custom.Catwalk.CatwalkSides;
import com.Legoman1342.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurationTool extends Item {

	public ConfigurationTool(Properties pProperties) {
		super(pProperties);
	}

	/**
	 * Called when the configuration tool is used on something
	 * @return The result of the interaction (success, fail, etc.)
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockState = level.getBlockState(pos);

		if (blockState.getBlock() == Registration.catwalk.get()) { //If the block is a catwalk
			if (blockState.getValue(Catwalk.CATWALK_LEFT) == CatwalkSides.RAILING && blockState.getValue(Catwalk.CATWALK_RIGHT) == CatwalkSides.RAILING) {
				if (blockState.getValue(Catwalk.CATWALK_END) == CatwalkEnd.RAILING) {
					level.setBlockAndUpdate(pos, blockState.setValue(Catwalk.CATWALK_END, CatwalkEnd.DROP));
					return InteractionResult.SUCCESS;
				} else if (blockState.getValue(Catwalk.CATWALK_END) == CatwalkEnd.DROP) {
					level.setBlockAndUpdate(pos, blockState.setValue(Catwalk.CATWALK_END, CatwalkEnd.RAILING));
					return InteractionResult.SUCCESS;
				} else {
					return InteractionResult.FAIL;
				}
			} else {
				return InteractionResult.FAIL;
			}
		//Add more interactable blocks here
		} else {
			return InteractionResult.FAIL;
		}
	}
}
