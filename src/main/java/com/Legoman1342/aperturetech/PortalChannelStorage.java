package com.Legoman1342.aperturetech;

import com.Legoman1342.utilities.ColorUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PortalChannelStorage {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final List<PortalChannel> portalChannels = new ArrayList<PortalChannel>();

	private static boolean dirty = false;

	@SubscribeEvent
	public static void onServerStarting(ServerAboutToStartEvent event) {
		// Clear all values when switching between saves
		portalChannels.clear();
		dirty = false;

		// Load the data from the world folder
		File file = new File(event.getServer().getWorldPath(LevelResource.ROOT).toFile(), "aperturetech/portal_channels.nbt");
		if(!file.exists())
			return;
		try {
			CompoundTag data = NbtIo.read(file);
			if(data != null) {
				read(data);
			}
		} catch(IOException exception){
			LOGGER.error("Failed to load portal channel data!", exception);
		}
	}

	@SubscribeEvent
	public static void onWorldSave(WorldEvent.Save event){
		if(!(event.getWorld() instanceof ServerLevel))
			return;

		// Save everything when world gets saved
		if(dirty){
			CompoundTag data = write();
			File file = new File(((ServerLevel)event.getWorld()).getServer().getWorldPath(LevelResource.ROOT).toFile(), "aperturetech/portal_channels.nbt");
			file.getParentFile().mkdirs();
			try{
				NbtIo.write(data, file);
			}catch(IOException exception){
				LOGGER.error("Failed to write portal channel data!", exception);
				return;
			}
			dirty = false;
		}
	}

	//TODO Add documentation to everything
	public static void addPortalChannel(PortalChannel channel) {
		portalChannels.add(channel);
		dirty = true;
	}

	public static PortalChannel getPortalChannel(int id) {
		return portalChannels.get(id);
	}

	public static int nextAvailableID() {
		return portalChannels.size();
	}

	private static CompoundTag write() {
		ListTag channelInfo = new ListTag();

		// Write portal channels
		for(PortalChannel channel : portalChannels){
			CompoundTag tag = new CompoundTag();
			tag.putInt("id", channel.getId());
			tag.putUUID("owner", channel.getOwner());
			tag.putString("name", channel.getName());
			tag.putBoolean("global", channel.isGlobal());
			tag.putIntArray("primaryColor", ColorUtils.toIntArray(channel.getPrimaryColor()));
			tag.putIntArray("secondaryColor", ColorUtils.toIntArray(channel.getSecondaryColor()));
			channelInfo.add(tag);
		}

		// Write the list to a tag
		CompoundTag tag = new CompoundTag();
		tag.put("portal_channels", channelInfo);

		return tag;
	}

	private static void read(CompoundTag tag) {
		// Get the list from the tag
		ListTag channelInfo = tag.getList("portal_channels", Tag.TAG_COMPOUND);
		for(Tag nbt : channelInfo) {
			if(!(nbt instanceof CompoundTag))
				continue;

			CompoundTag channelTag = (CompoundTag)nbt;
			if(!channelTag.contains("player", Tag.TAG_INT_ARRAY) || !channelTag.contains("time", Tag.TAG_LONG))
				continue;

			int[] primaryColor = channelTag.getIntArray("primary_color");
			int[] secondaryColor = channelTag.getIntArray("secondary_color");

			PortalChannel channel = new PortalChannel(
					channelTag.getInt("id"),
					channelTag.getUUID("owner"),
					channelTag.getString("name"),
					channelTag.getBoolean("global"),
					ColorUtils.toColor(primaryColor),
					ColorUtils.toColor(secondaryColor)
			);
			portalChannels.add(channel);
		}
	}
}
