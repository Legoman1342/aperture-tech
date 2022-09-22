package com.Legoman1342.aperturetech;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blockentities.client.ChamberlockDoorRenderer;
import com.Legoman1342.blocks.BlockRegistration;
import com.Legoman1342.entities.EntityRegistration;
import com.Legoman1342.entities.client.StorageCubeRenderer;
import com.Legoman1342.items.ItemRegistration;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
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
    
        //Initializes GeckoLib
        GeckoLib.initialize();
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        // Register the setup event for modloading
        bus.addListener(this::setup);
        // Register the clientSetup event for modloading
        bus.addListener(this::clientSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        //Sets how to render different blocks, needed for any block with transparency
        ItemBlockRenderTypes.setRenderLayer(BlockRegistration.CATWALK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistration.CATWALK_STAIRS.get(), RenderType.cutout());

        //Registers the renderers for entities and block entities
        EntityRenderers.register(EntityRegistration.STORAGE_CUBE.get(), StorageCubeRenderer::new);

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
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
