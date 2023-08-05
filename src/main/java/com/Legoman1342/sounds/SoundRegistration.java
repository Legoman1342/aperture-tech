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

	//IMPORTANT: When adding sounds, make sure to update sounds.json
	public static final RegistryObject<SoundEvent> CHAMBERLOCK_DOOR_OPEN = registerSoundEvent("chamberlock_door_open");
	public static final RegistryObject<SoundEvent> CHAMBERLOCK_DOOR_CLOSE = registerSoundEvent("chamberlock_door_close");
	public static final RegistryObject<SoundEvent> SURFACE_BUTTON_ACTIVATE = registerSoundEvent("surface_button_activate");
	public static final RegistryObject<SoundEvent> SURFACE_BUTTON_DEACTIVATE = registerSoundEvent("surface_button_deactivate");
	public static final RegistryObject<SoundEvent> FIZZLE = registerSoundEvent("fizzle");
	public static final RegistryObject<SoundEvent> PORTAL_GUN_FIRE_PRIMARY = registerSoundEvent("portal_fire_primary");
	public static final RegistryObject<SoundEvent> PORTAL_GUN_FIRE_SECONDARY = registerSoundEvent("portal_fire_secondary");
	public static final RegistryObject<SoundEvent> PORTAL_OPEN_PRIMARY = registerSoundEvent("portal_open_primary");
	public static final RegistryObject<SoundEvent> PORTAL_OPEN_SECONDARY = registerSoundEvent("portal_open_secondary");


	/**
	 * Convenience method used for easier registering.
	 * @param name The registry name of the sound event
	 */
	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(ApertureTech.MODID, name)));
	}
}
