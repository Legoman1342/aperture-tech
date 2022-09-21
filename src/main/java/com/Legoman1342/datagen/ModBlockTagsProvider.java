package com.Legoman1342.datagen;

import com.Legoman1342.aperturetech.ApertureTech;
import com.Legoman1342.blocks.BlockRegistration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, ApertureTech.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        //Adds mod blocks to existing tags
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockRegistration.CATWALK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BlockRegistration.CATWALK_STAIRS.get());
    }
}
