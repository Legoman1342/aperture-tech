package com.Legoman1342.items.custom;

import com.Legoman1342.entities.custom.PortalProjectile;
import com.Legoman1342.items.ItemRegistration;
import com.Legoman1342.sounds.SoundRegistration;
import com.Legoman1342.utilities.PortalChannel;
import com.Legoman1342.utilities.PortalChannelStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalGun extends Item {

	public PortalGun(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void onCraftedBy(ItemStack pStack, Level pLevel, @NotNull Player pPlayer) {
		super.onCraftedBy(pStack, pLevel, pPlayer);
		if (!pLevel.isClientSide()) {
			PortalChannel channel = new PortalChannel(pPlayer.getUUID());
			CompoundTag tag = new CompoundTag();
			tag.putInt("channelID", channel.getId());
			pStack.setTag(tag);
		}
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		if (pStack.hasTag()) {
			try {
				int channelId = pStack.getTag().getInt("channelID");
				PortalChannel channel = PortalChannelStorage.getPortalChannel(channelId);
				pTooltipComponents.add(Component.translatable("message.portal_gun.tooltip", channel.getName(), channelId));
			} catch (Exception ignored) {}
		}
	}

	private static void firePortalProjectile(Player player, boolean primary) {
		Level level = player.getLevel();
		PortalProjectile projectile = PortalGun.getPortalProjectile(player, InteractionHand.MAIN_HAND, primary);
		level.addFreshEntity(projectile);
		level.playSound(null, player.getX(), player.getEyeY(), player.getZ(),
				primary ? SoundRegistration.PORTAL_GUN_FIRE_PRIMARY.get() : SoundRegistration.PORTAL_GUN_FIRE_SECONDARY.get(),
				SoundSource.PLAYERS, 0.5F, 1F);
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		if (pIsSelected && pEntity.getType() == EntityType.PLAYER && !pLevel.isClientSide) {
			Player player = (Player) pEntity;
			ItemCooldowns cooldowns = player.getCooldowns();
			final int cooldownTime = 8;

			if (!cooldowns.isOnCooldown(ItemRegistration.PORTAL_GUN.get())) {
				if (Minecraft.getInstance().options.keyAttack.isDown()) {
					firePortalProjectile(player, true);
					cooldowns.addCooldown(ItemRegistration.PORTAL_GUN.get(), cooldownTime);
				} else if (Minecraft.getInstance().options.keyUse.isDown()) {
					firePortalProjectile(player, false);
					cooldowns.addCooldown(ItemRegistration.PORTAL_GUN.get(), cooldownTime);
				}
			}
		}
	}


	@SubscribeEvent
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		Player player = event.getEntity();
		if (portalGunInHand(player) == InteractionHand.MAIN_HAND) {
			event.setCanceled(true);
		}
	}

	/**
	 * Convenience method to determine if a player has a portal gun in either of their hands.
	 * @return <code>MAIN_HAND</code> or <code>OFF_HAND</code> if there is a portal gun in either hand (<code>MAIN_HAND</code>
	 * is prioritized), <code>null</code> if neither hand has a portal gun
	 */
	@Nullable
	private static InteractionHand portalGunInHand(Player player) {
		if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistration.PORTAL_GUN.get())) {
			return InteractionHand.MAIN_HAND;
		} else if (player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistration.PORTAL_GUN.get())) {
			return InteractionHand.OFF_HAND;
		} else {
			return null;
		}
	}

	private static PortalChannel getChannel(ItemStack stack) {
		if (stack.hasTag()){
			int id = stack.getTag().getInt("channelID");
			return PortalChannelStorage.getPortalChannel(id);
		} else {
			throw new NullPointerException("Portal gun's channel hasn't been defined");
		}
	}

	public static PortalProjectile getPortalProjectile(Player player, InteractionHand usedHand, boolean primary) {
		return PortalProjectile.newPortalProjectile(player, getChannel(player.getItemInHand(usedHand)), primary, player.getEyePosition(), player.xRotO, player.yRotO);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.CUSTOM;
	}
}
