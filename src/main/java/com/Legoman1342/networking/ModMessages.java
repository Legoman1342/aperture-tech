package com.Legoman1342.networking;

import com.Legoman1342.aperturetech.ApertureTech;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
	private static SimpleChannel INSTANCE;
	private static int packetId = 0;

	/**
	 * Increments the packet ID whenever it's requested.
	 * @return The current packet ID
	 */
	private static int id() {
		return packetId++;
	}

	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(ApertureTech.MODID, "messages"))
				.networkProtocolVersion(() -> "1.0") //Increment version when the packet protocol is changed
				.clientAcceptedVersions(s -> true)
				.serverAcceptedVersions(s -> true)
				.simpleChannel();
		INSTANCE = net;
	}

	/**
	 * Sends a message to the server.
	 */
	public static <MSG> void sendToServer(MSG message) {
		INSTANCE.sendToServer(message);
	}

	/**
	 * Sends a message to a specified player.
	 */
	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}
}
