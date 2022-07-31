package com.Legoman1342.datagen;

import com.Legoman1342.blocks.BlockRegistration;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
	public ModRecipeProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
		//Catwalk
		ShapedRecipeBuilder.shaped(BlockRegistration.catwalk.get())
				.define('b', Items.IRON_BARS)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("b b")
				.pattern("b b")
				.pattern("iii")
				.unlockedBy("has_iron_ingot",inventoryTrigger(ItemPredicate.Builder.item()
						.of(Tags.Items.INGOTS_IRON).build())) //Recipe unlocks when the player has an iron ingot (or any item matching the tag)
				.save(finishedRecipeConsumer);
	}
}
