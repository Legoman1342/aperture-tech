package com.Legoman1342.entities;

import com.Legoman1342.entities.custom.CubeEntity;
import com.Legoman1342.entities.custom.PortalProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.Legoman1342.aperturetech.ApertureTech.MODID;

public class EntityRegistration {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ENTITY_TYPES.register(bus);
	}

	public static final RegistryObject<EntityType<CubeEntity>> STORAGE_CUBE =
			ENTITY_TYPES.register("storage_cube",
					() -> EntityType.Builder.of(CubeEntity::new, MobCategory.MISC)
							.sized(0.9375f, 0.9375f)
							.build(new ResourceLocation(MODID, "storage_cube").toString()));

	public static final RegistryObject<EntityType<PortalProjectile>> PORTAL_PROJECTILE =
			ENTITY_TYPES.register("portal_projectile",
					() -> EntityType.Builder.of(PortalProjectile::new, MobCategory.MISC)
							.sized(0.125f, 0.125f)
							.build(new ResourceLocation(MODID, "portal_projectile").toString()));

}
