package com.Legoman1342.items.custom;

import com.Legoman1342.aperturetech.PortalChannel;
import com.Legoman1342.aperturetech.PortalChannelStorage;
import com.Legoman1342.entities.custom.PortalProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
		PortalProjectile projectile = getPortalProjectile(pPlayer, pUsedHand);
		projectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0F, 0.5F, 0F);
		pLevel.addFreshEntity(projectile);
		return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
	}

	private PortalChannel getChannel(ItemStack stack) {
		if (stack.hasTag()){
			int id = stack.getTag().getInt("channelID");
			return PortalChannelStorage.getPortalChannel(id);
		} else {
			throw new NullPointerException("Portal gun's channel hasn't been defined");
		}
	}

	private PortalProjectile getPortalProjectile(Player player, InteractionHand usedHand) {
		return PortalProjectile.newPortalProjectile(player, getChannel(player.getItemInHand(usedHand)));
	}
}
