package com.Legoman1342.blockentities;

import com.Legoman1342.blockentities.custom.ChamberlockDoorBE;
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
			"animated_block_entity",
			() -> BlockEntityType.Builder.of(ChamberlockDoorBE::new,
					BlockRegistration.CHAMBERLOCK_DOOR.get())
					.build(null)
	);

	/*TODO Links:
		https://www.youtube.com/watch?v=EpGenTQthvY
		https://docs.minecraftforge.net/en/1.18.x/blockentities/
		https://github.com/bernie-g/geckolib/wiki/Block-Animations
	 */
}
