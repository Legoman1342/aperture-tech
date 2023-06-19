//Some code modified from https://github.com/SuperMartijn642/ChunkLoaders

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
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Mod.EventBusSubscriber
public class PortalChannelStorage {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * List of all of a world's portal channels and their data. Saved in an NBT file while the world isn't open.
	 * A channel's ID is the same as its index in this list.
	 */
	private static final List<PortalChannel> portalChannels = new ArrayList<>();

	/**
	 * Used to mark when the saved list has been modified and needs to be saved to the NBT file again.
	 */
	private static boolean dirty = false;

	/**
	 * Loads the portal channel data from the NBT file into the saved list when the world starts up.
	 */
	@SubscribeEvent
	public static void onServerStarting(ServerAboutToStartEvent event) {
		// Clear portalChannels list when switching between saves
		portalChannels.clear();
		dirty = false;

		// Load the data from the world folder
		File file = new File(event.getServer().getWorldPath(LevelResource.ROOT).toFile(), "aperturetech/portal_channels.nbt");
		if(!file.exists()) {
			return;
		}
		try {
			CompoundTag data = NbtIo.read(file);
			if(data != null) {
				read(data);
			}
		} catch(IOException exception){
			LOGGER.error("Failed to load portal channel data!", exception);
		}
	}

	/**
	 * Saves the data from the saved list into the NBT file whenever the world saves (autosaves or shutting down).
	 */
	@SubscribeEvent
	public static void onWorldSave(WorldEvent.Save event) {
		if (!(event.getWorld() instanceof ServerLevel)) {
			return;
		}

		// Save everything when world gets saved
		if (dirty) {
			CompoundTag data = write();
			File file = new File(((ServerLevel)event.getWorld()).getServer().getWorldPath(LevelResource.ROOT).toFile(), "aperturetech/portal_channels.nbt");
			file.getParentFile().mkdirs();
			try {
				NbtIo.write(data, file);
			} catch(IOException exception) {
				LOGGER.error("Failed to write portal channel data!", exception);
				return;
			}
			dirty = false;
		}
	}

	/**
	 * Marks the saved list as dirty. Should be called whenever a channel is modified in any way.
	 */
	public static void setDirty() {
		dirty = true;
	}

	/**
	 * Adds a new channel to the saved list. Used whenever new portal channels are created.
	 * @param channel A <code>portalChannel</code> to add to the list
	 */
	public static void addPortalChannel(PortalChannel channel) {
		portalChannels.add(channel);
		dirty = true;
	}

	/**
	 * Returns a portal channel from the saved list.
	 * @param id The ID (or index in the list) of the channel to access
	 */
	public static PortalChannel getPortalChannel(int id) {
		return portalChannels.get(id);
	}

	/**
	 * Returns the lowest ID number that doesn't already have a channel assigned to it.
	 */
	public static int nextAvailableID() {
		return portalChannels.size();
	}

	/**
	 * Gets all channel users from all portal channels and returns them, along with their channel.
	 * @return A <code>Map</code> containing channel user UUIDs (keys) and their respective portal channels (values)
	 */
	public static Map<UUID, PortalChannel> getAllChannelUsers() {
		Map<UUID, PortalChannel> toReturn = new HashMap<>();
		for (PortalChannel i : portalChannels) {
			for (UUID j : i.getUsers()) {
				toReturn.put(j, i);
			}
		}
		return toReturn;
	}

	/**
	 * Compiles the saved list into a tag to put into the NBT file.
	 * @return An NBT tag containing the entire list of portal channels and all of their data
	 */
	private static CompoundTag write() {
		ListTag channelInfo = new ListTag();

		// Write portal channels
		for (PortalChannel channel : portalChannels) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("id", channel.getId());
			tag.putUUID("owner", channel.getOwner());
			for (int i = 0; i < channel.getUsers().size(); i++) {
				tag.putUUID("user" + i, channel.getUsers().get(i));
			}
			tag.putString("name", channel.getName());
			tag.putBoolean("global", channel.isGlobal());
			tag.putIntArray("primaryColor", ColorUtils.toIntArray(channel.getPrimaryColor()));
			tag.putIntArray("secondaryColor", ColorUtils.toIntArray(channel.getSecondaryColor()));
			channelInfo.add(tag);
		}

		// Write the list to a tag
		CompoundTag toReturn = new CompoundTag();
		toReturn.put("portal_channels", channelInfo);
		return toReturn;
	}

	/**
	 * Takes a tag from the NBT file and loads all of its data into the saved list.
	 */
	private static void read(CompoundTag tag) {
		// Get the portal channels from the tag
		ListTag channelInfo = tag.getList("portal_channels", Tag.TAG_COMPOUND);

		//Write each portal channel to the list
		for(Tag nbt : channelInfo) {
			if (!(nbt instanceof CompoundTag)) {
				continue;
			}

			CompoundTag channelTag = (CompoundTag)nbt;

			ArrayList<UUID> users = new ArrayList<>();
			boolean moreUsers = true;
			int i = 0;
			while (moreUsers) {
				if (channelTag.contains("user" + i)) {
					users.add(channelTag.getUUID("user" + i));
					i++;
				} else {
					moreUsers = false;
				}
			}

			int[] primaryColor = channelTag.getIntArray("primaryColor");
			int[] secondaryColor = channelTag.getIntArray("secondaryColor");

			PortalChannel channel = new PortalChannel(
					channelTag.getInt("id"),
					channelTag.getUUID("owner"),
					users,
					channelTag.getString("name"),
					channelTag.getBoolean("global"),
					ColorUtils.toColor(primaryColor),
					ColorUtils.toColor(secondaryColor)
			);
			portalChannels.add(channel);
		}
	}
}
