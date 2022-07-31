package com.Legoman1342.event;

import com.Legoman1342.aperturetech.ApertureTech;
import com.Legoman1342.entities.EntityRegistration;
import com.Legoman1342.entities.custom.CubeEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ApertureTech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
		event.put(EntityRegistration.STORAGE_CUBE.get(), CubeEntity.setAttributes());
	}
}
