package com.Legoman1342.items.custom;

import com.Legoman1342.aperturetech.PortalChannel;
import com.Legoman1342.aperturetech.PortalChannelStorage;
import com.Legoman1342.entities.custom.PortalProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
				pTooltipComponents.add(new TranslatableComponent("message.portal_gun.tooltip", channel.getName(), channelId));
			} catch (Exception ignored) {}
		}
	}
	/**
	 * Shoots a primary projectile when the player left-clicks.
	 */
	@Override
	public boolean onEntitySwing(ItemStack pStack, LivingEntity pEntity) {
		Level level = pEntity.getLevel();
		PortalProjectile projectile = getPortalProjectile((Player) pEntity, InteractionHand.MAIN_HAND, true);
		if (!level.isClientSide) {
			level.addFreshEntity(projectile);
		}
		return true;
	}

	/**
	 * Shoots a secondary projectile when the player right-clicks.
	 */
	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		PortalProjectile projectile = getPortalProjectile(pPlayer, InteractionHand.MAIN_HAND, false);
		if (!pLevel.isClientSide) {
			pLevel.addFreshEntity(projectile);
		}
		return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
	}

	private PortalChannel getChannel(ItemStack stack) {
		if (stack.hasTag()){
			int id = stack.getTag().getInt("channelID");
			return PortalChannelStorage.getPortalChannel(id);
		} else {
			throw new NullPointerException("Portal gun's channel hasn't been defined");
		}
	}

	private PortalProjectile getPortalProjectile(Player player, InteractionHand usedHand, boolean primary) {
		return PortalProjectile.newPortalProjectile(player, getChannel(player.getItemInHand(usedHand)), primary, player.getEyePosition(), player.xRotO, player.yRotO);
	}
}
