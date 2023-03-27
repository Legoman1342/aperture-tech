package com.Legoman1342.datagen;

import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.items.ItemRegistration;
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
		ShapedRecipeBuilder.shaped(BlockRegistration.CATWALK.get())
				.define('b', Items.IRON_BARS)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("b b")
				.pattern("b b")
				.pattern("iii")
				.unlockedBy("has_iron_ingot", inventoryTrigger(ItemPredicate.Builder.item()
						.of(Tags.Items.INGOTS_IRON).build())) //Recipe unlocks when the player has an iron ingot (or any item matching the tag)
				.save(finishedRecipeConsumer);

		//Storage cube
		ShapedRecipeBuilder.shaped(ItemRegistration.STORAGE_CUBE.get())
				.define('i', Tags.Items.INGOTS_IRON)
				.define('g', Items.GLOWSTONE)
				.define('c', Tags.Items.CHESTS_WOODEN)
				.pattern("iii")
				.pattern("gcg")
				.pattern("iii")
				.unlockedBy("has_chest", inventoryTrigger(ItemPredicate.Builder.item()
						.of(Tags.Items.CHESTS_WOODEN).build()))
				.save(finishedRecipeConsumer);

		//Chamberlock door
		ShapedRecipeBuilder.shaped(BlockRegistration.CHAMBERLOCK_DOOR.get())
				.define('i', Tags.Items.INGOTS_IRON)
				.define('p', Items.PISTON)
				.define('d', Items.IRON_DOOR)
				.pattern("ipi")
				.pattern("d d")
				.pattern("ipi")
				.unlockedBy("has_iron_ingot", inventoryTrigger(ItemPredicate.Builder.item()
						.of(Tags.Items.INGOTS_IRON).build()))
				.save(finishedRecipeConsumer);

		//Surface button
		ShapedRecipeBuilder.shaped(BlockRegistration.SURFACE_BUTTON.get())
				.define('i', Tags.Items.INGOTS_IRON)
				.define('c', Items.RED_CONCRETE)
				.define('p', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
				.pattern("   ")
				.pattern("ccc")
				.pattern("ipi")
				.unlockedBy("has_iron_ingot", inventoryTrigger(ItemPredicate.Builder.item()
						.of(Tags.Items.INGOTS_IRON).build()))
				.save(finishedRecipeConsumer);
	}
}
