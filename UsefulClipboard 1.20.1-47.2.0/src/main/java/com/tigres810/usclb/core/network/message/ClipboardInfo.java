package com.tigres810.usclb.core.network.message;

import java.util.function.Supplier;

import com.tigres810.usclb.client.onlyinclient.OpenScreen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

public class ClipboardInfo {

	public CompoundTag nbt;

	public ClipboardInfo ( CompoundTag tag ) {
		this.nbt = tag;
	}

	public static void encode ( ClipboardInfo message, FriendlyByteBuf buffer ) {
		buffer.writeNbt( message.nbt );
	}

	public static ClipboardInfo decode ( FriendlyByteBuf buf ) {
		return new ClipboardInfo( buf.readNbt( ) );
	}

	public static void handle ( ClipboardInfo message, Supplier< NetworkEvent.Context > contextSupplier ) {
		NetworkEvent.Context context = contextSupplier.get( );
		context.enqueueWork( ( ) -> {

			// Serverside
			if ( FMLEnvironment.dist == Dist.CLIENT ) {
				OpenScreen.openScreen( message );
			}
		} );
		context.setPacketHandled( true );
	}

}
