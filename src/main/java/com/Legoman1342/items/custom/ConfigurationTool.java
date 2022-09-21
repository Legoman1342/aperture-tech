package com.Legoman1342.items.custom;

import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.blocks.custom.Catwalk;
import com.Legoman1342.blocks.custom.Catwalk.ATCatwalkEnd;
import com.Legoman1342.blocks.custom.Catwalk.ATCatwalkSides;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ConfigurationTool extends Item {

	public ConfigurationTool(Properties pProperties) {
		super(pProperties);
	}

	/**
	 * Called when the configuration tool is used on a block
	 * @return The result of the interaction (success, fail, etc.)
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockState = level.getBlockState(pos);

		if (blockState.getBlock() == BlockRegistration.CATWALK.get()) { //If the block is a catwalk
			if (blockState.getValue(Catwalk.CATWALK_LEFT) == ATCatwalkSides.RAILING && blockState.getValue(Catwalk.CATWALK_RIGHT) == ATCatwalkSides.RAILING) {
				if (blockState.getValue(Catwalk.CATWALK_END) == ATCatwalkEnd.RAILING) {
					level.setBlockAndUpdate(pos, blockState.setValue(Catwalk.CATWALK_END, ATCatwalkEnd.DROP));
					return InteractionResult.SUCCESS;
				} else if (blockState.getValue(Catwalk.CATWALK_END) == ATCatwalkEnd.DROP) {
					level.setBlockAndUpdate(pos, blockState.setValue(Catwalk.CATWALK_END, ATCatwalkEnd.RAILING));
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
