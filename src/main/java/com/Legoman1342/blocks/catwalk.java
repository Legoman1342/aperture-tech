package com.Legoman1342.blocks;

import com.Legoman1342.utilities.Lang;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class catwalk extends Block {
	
	//Creating block states that aren't in vanilla
	public static final BooleanProperty CATWALK_LEFT = BooleanProperty.create("left");
	public static final BooleanProperty CATWALK_RIGHT = BooleanProperty.create("right");
	public static final EnumProperty<CatwalkEnd> CATWALK_END = EnumProperty.create("end", CatwalkEnd.class);
	
	//The CatwalkEnd block property needs to be defined differently since it's an enum
	public enum CatwalkEnd implements StringRepresentable {
		DROP, RAILING, ATTACH,; //Possible shapes for the end of catwalks (possible values for the enum)
		
		public static CatwalkEnd byIndex(int index) { //Returns an enum value when given an index
			return values()[index];
		}
		
		
		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}
	
	public catwalk() {
		super(Properties.of(Material.METAL)
				.sound(SoundType.LANTERN)
				.strength(2.0f)
				.noOcclusion());
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, CATWALK_LEFT, CATWALK_RIGHT, CATWALK_END);
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
				.setValue(CATWALK_LEFT, false)
				.setValue(CATWALK_RIGHT, false)
				.setValue(CATWALK_END, CatwalkEnd.RAILING);
	}
}
