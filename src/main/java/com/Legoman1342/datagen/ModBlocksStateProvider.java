package com.Legoman1342.datagen;

import com.Legoman1342.aperturetech.ApertureTech;
import com.Legoman1342.blocks.custom.Catwalk;
import com.Legoman1342.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.Legoman1342.blocks.custom.Catwalk.CATWALK_END;

public class ModBlocksStateProvider extends BlockStateProvider {
    public ModBlocksStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ApertureTech.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.getMultipartBuilder(Registration.catwalk.get())
                //TODO Implement catwalk multipart models (https://mcforge.readthedocs.io/en/1.18.x/datagen/client/modelproviders/)
    }
}
