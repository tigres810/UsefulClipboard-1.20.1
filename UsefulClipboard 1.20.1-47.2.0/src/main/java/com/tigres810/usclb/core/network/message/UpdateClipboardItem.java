package com.tigres810.usclb.core.network.message;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class UpdateClipboardItem {

	public CompoundTag nbt;

	public UpdateClipboardItem ( CompoundTag tag ) {
		this.nbt = tag;
	}

	public static void encode ( UpdateClipboardItem message, FriendlyByteBuf buffer ) {
		buffer.writeNbt( message.nbt );
	}

	public static UpdateClipboardItem decode ( FriendlyByteBuf buffer ) {
		return new UpdateClipboardItem( buffer.readNbt( ) );
	}
	
	public static void handle(UpdateClipboardItem message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			ServerPlayer player = context.getSender();
			CompoundTag nbTag = message.nbt;
			
			ItemStack handitem = player.getMainHandItem( );
			handitem.setTag( nbTag );
			player.setItemInHand(InteractionHand.MAIN_HAND, handitem);
            player.swing( InteractionHand.MAIN_HAND );
		});
		context.setPacketHandled(true);
	}

}
