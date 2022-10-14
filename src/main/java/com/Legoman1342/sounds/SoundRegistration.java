package com.Legoman1342.sounds;

import com.Legoman1342.aperturetech.ApertureTech;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistration {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ApertureTech.MODID);

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		SOUND_EVENTS.register(bus);
	}

	public static final RegistryObject<SoundEvent> CHAMBERLOCK_DOOR_OPEN = registerSoundEvent("chamberlock_door_open");
	public static final RegistryObject<SoundEvent> CHAMBERLOCK_DOOR_CLOSE = registerSoundEvent("chamberlock_door_close");

	/**
	 * Convenience method used for easier registering.
	 * @param name The registry name of the sound event
	 */
	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(ApertureTech.MODID, name)));
	}
}
