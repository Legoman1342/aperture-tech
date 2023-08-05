package com.Legoman1342.event;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blockentities.client.ChamberlockDoorRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class ClientEvents {

	@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
	public static class ClientForgeEvents {
		//Add anything that needs the Forge mod bus here
	}

	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModBusEvents {
		/**
		 * Registers block entity renderers.
		 */
		@SubscribeEvent
		public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
			event.registerBlockEntityRenderer(BlockEntityRegistration.CHAMBERLOCK_DOOR_BE.get(), ChamberlockDoorRenderer::new);
		}

	}
}
