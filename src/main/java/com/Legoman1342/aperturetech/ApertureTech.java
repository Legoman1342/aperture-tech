package com.Legoman1342.aperturetech;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blockentities.client.ChamberlockDoorRenderer;
import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.entities.EntityRegistration;
import com.Legoman1342.entities.client.PortalProjectileRenderer;
import com.Legoman1342.entities.client.StorageCubeRenderer;
import com.Legoman1342.items.ItemRegistration;
import com.Legoman1342.items.custom.PortalGun;
import com.Legoman1342.networking.ModMessages;
import com.Legoman1342.sounds.SoundRegistration;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ApertureTech.MODID)
public class ApertureTech {
	public static final String MODID = "aperturetech";

	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	public ApertureTech() {
		//Runs the init code in registration classes
		BlockRegistration.init();
		ItemRegistration.init();
		EntityRegistration.init();
		BlockEntityRegistration.init();
		SoundRegistration.init();

		//Initializes GeckoLib
		GeckoLib.initialize();

		//Add listeners to the Forge event bus
		IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
		forgeEventBus.addListener(PortalGun::onLeftClickBlock);

		//Add listeners to the mod event bus
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		// Register the setup method for modloading
		modEventBus.addListener(this::commonSetup);
		// Register the enqueueIMC method for modloading
		modEventBus.addListener(this::enqueueIMC);
		// Register the processIMC method for modloading
		modEventBus.addListener(this::processIMC);
		// Register the clientSetup event for modloading
		modEventBus.addListener(this::clientSetup);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			ModMessages.register();
		});
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		//Registers the renderers for entities and block entities
		EntityRenderers.register(EntityRegistration.STORAGE_CUBE.get(), StorageCubeRenderer::new);
		EntityRenderers.register(EntityRegistration.PORTAL_PROJECTILE.get(), PortalProjectileRenderer::new);

		BlockEntityRenderers.register(BlockEntityRegistration.CHAMBERLOCK_DOOR_BE.get(), ChamberlockDoorRenderer::new);
	}

	private void enqueueIMC(final InterModEnqueueEvent event)
	{
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("aperturetech", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
	}

	private void processIMC(final InterModProcessEvent event)
	{
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m->m.messageSupplier().get()).
				collect(Collectors.toList()));
	}
}
