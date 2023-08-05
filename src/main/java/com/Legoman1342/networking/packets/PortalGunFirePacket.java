package com.Legoman1342.networking.packets;

import com.Legoman1342.entities.custom.PortalProjectile;
import com.Legoman1342.items.ItemRegistration;
import com.Legoman1342.items.custom.PortalGun;
import com.Legoman1342.sounds.SoundRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PortalGunFirePacket {
	private final boolean primary;

	public PortalGunFirePacket(boolean primary) {
		this.primary = primary;
	}

	public PortalGunFirePacket(FriendlyByteBuf buf) {
		this.primary = buf.readBoolean();
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBoolean(primary);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> { //Anything inside this will run on the server

			ServerPlayer player = context.getSender();
			if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistration.PORTAL_GUN.get())) {
				Level level = player.getLevel();
				PortalProjectile projectile = PortalGun.getPortalProjectile(player, InteractionHand.MAIN_HAND, primary);
				level.addFreshEntity(projectile);
				level.playSound(null, player.getX(), player.getEyeY(), player.getZ(),
						primary ? SoundRegistration.PORTAL_GUN_FIRE_PRIMARY.get() : SoundRegistration.PORTAL_GUN_FIRE_SECONDARY.get(),
						SoundSource.PLAYERS, 0.5F, 1F);
			}

		});
		context.setPacketHandled(true);
		return true;
	}
}
