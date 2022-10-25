package com.Legoman1342.blockentities;

import com.Legoman1342.blockentities.custom.ChamberlockDoorBE;
import com.Legoman1342.blockentities.custom.SurfaceButtonBE;
import com.Legoman1342.blocks.BlockRegistration;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class BlockEntityRegistration {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCK_ENTITIES.register(bus);
	}

	public static final RegistryObject<BlockEntityType<ChamberlockDoorBE>> CHAMBERLOCK_DOOR_BE = BLOCK_ENTITIES.register(
			"chamberlock_door",
			() -> BlockEntityType.Builder.of(ChamberlockDoorBE::new,
					BlockRegistration.CHAMBERLOCK_DOOR.get())
					.build(null)
	);
	public static final RegistryObject<BlockEntityType<SurfaceButtonBE>> SURFACE_BUTTON_BE = BLOCK_ENTITIES.register(
			"surface_button",
			() -> BlockEntityType.Builder.of(SurfaceButtonBE::new,
					BlockRegistration.SURFACE_BUTTON.get())
					.build(null)
	);
}
