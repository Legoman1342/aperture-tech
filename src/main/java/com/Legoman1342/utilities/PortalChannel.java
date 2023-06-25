//Some code modified from https://github.com/SuperMartijn642/ChunkLoaders

package com.Legoman1342.utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class PortalChannel {
	private final int id;
	private final UUID owner;
	private ArrayList<UUID> users;
	private String name; //Default: Channel <ID>
	private boolean isGlobal; //Default: false
	private Color primaryColor; //Default: DEFAULT_PRIMARY_COLOR
	private Color secondaryColor; //Default: DEFAULT_SECONDARY_COLOR

	private static final Color DEFAULT_PRIMARY_COLOR = new Color(0F, 0.5F, 1F);
	private static final Color DEFAULT_SECONDARY_COLOR = ColorUtils.invertColor(DEFAULT_PRIMARY_COLOR);

	/**
	 * Stripped-down constructor with most parameters set to their defaults.  Used when a new channel is created for the first time.
	 * @param owner The UUID of the player who created the channel and has control over its settings
	 */
	public PortalChannel(UUID owner) {
		this (PortalChannelStorage.nextAvailableID(), owner, new ArrayList<>(), "Channel " + PortalChannelStorage.nextAvailableID(), false, DEFAULT_PRIMARY_COLOR, DEFAULT_SECONDARY_COLOR);
		PortalChannelStorage.addPortalChannel(this);
	}

	/**
	 * Full constructor where all parameters are set manually. Used when loading portal channels from the NBT file.
	 * @param id A unique integer assigned to channels in the order they're created
	 * @param owner The UUID of the player who created the channel and has control over its settings
	 * @param users An <code>ArrayList</code> of entities that use this channel (like portals, portal projectiles, etc.)
	 * @param name A text name for the channel that can be changed for player convenience
	 * @param isGlobal Whether this is a global channel or a personal channel
	 * @param primaryColor The color of the first portal on this channel (blue in the Portal games)
	 * @param secondaryColor The color of the second portal on this channel (orange in the Portal games)
	 */
	public PortalChannel(int id, UUID owner, ArrayList<UUID> users, String name, boolean isGlobal, Color primaryColor, Color secondaryColor) {
		this.id = id;
		this.owner = owner;
		this.users = users;
		this.name = name;
		this.isGlobal = isGlobal;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}

	public int getId() {
		return id;
	}

	public UUID getOwner() {
		return owner;
	}

	public ArrayList<UUID> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<UUID> users) {
		this.users = users;
	}

	public boolean hasUser(UUID user) {
		return users.contains(user);
	}

	public void addUser(UUID user) {
		users.add(user);
	}

	public void removeUser(UUID user) {
		users.remove(user);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGlobal() {
		return isGlobal;
	}

	public void setGlobal(boolean global) {
		isGlobal = global;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
	}
}
