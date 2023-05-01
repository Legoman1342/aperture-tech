package com.Legoman1342.items.custom;

import com.Legoman1342.aperturetech.PortalChannel;
import com.Legoman1342.aperturetech.PortalChannelStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortalGun extends Item {

	public PortalGun(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
		super.onCraftedBy(pStack, pLevel, pPlayer);
		PortalChannel channel = new PortalChannel(pPlayer.getUUID());
		CompoundTag tag = new CompoundTag();
		tag.putInt("channelID", channel.getId());
		pStack.setTag(tag);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		if (pStack.hasTag()) {
			try {
				int channelId = pStack.getTag().getInt("channel");
				PortalChannel channel = PortalChannelStorage.getPortalChannel(channelId);
				pTooltipComponents.add(new TranslatableComponent("message.portal_gun.tooltip", channel.getName(), channelId));
			} catch (Exception ignored) {}
		}
	}
}
