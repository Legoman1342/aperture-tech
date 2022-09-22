package com.Legoman1342.event;

import com.Legoman1342.blockentities.BlockEntityRegistration;
import com.Legoman1342.blockentities.client.ChamberlockDoorRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(BlockEntityRegistration.CHAMBERLOCK_DOOR_BE.get(), ChamberlockDoorRenderer::new);
	}
}
