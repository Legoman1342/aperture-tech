//Some code modified from https://github.com/SuperMartijn642/ChunkLoaders

package com.Legoman1342.aperturetech;

import com.Legoman1342.utilities.ColorUtils;

import java.awt.*;
import java.util.UUID;

public class PortalChannel {
	private final int id;
	private final UUID owner;
	private String name; //Default: Channel <ID>
	private boolean isGlobal; //Default: false
	private Color primaryColor; //Default: DEFAULT_PRIMARY_COLOR
	private Color secondaryColor; //Default: DEFAULT_SECONDARY_COLOR

	private static final Color DEFAULT_PRIMARY_COLOR = new Color(0F, 0.5F, 1F);
	private static final Color DEFAULT_SECONDARY_COLOR = ColorUtils.invertColor(DEFAULT_PRIMARY_COLOR);

	public PortalChannel(UUID owner) {
		this (PortalChannelStorage.nextAvailableID(), owner, "Channel " + PortalChannelStorage.nextAvailableID(), false, DEFAULT_PRIMARY_COLOR, DEFAULT_SECONDARY_COLOR);
	}

	public PortalChannel(int id, UUID owner, String name, boolean isGlobal, Color primaryColor, Color secondaryColor) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.isGlobal = isGlobal;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		PortalChannelStorage.addPortalChannel(this);
	}

	public int getId() {
		return id;
	}

	public UUID getOwner() {
		return owner;
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

	public Color[] getColors() {
		return new Color[] {primaryColor, secondaryColor};
	}

	public void setColors(Color primaryColor, Color secondaryColor) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}
}
