package com.tigres810.usclb.core.network;

import com.tigres810.usclb.Main;
import com.tigres810.usclb.core.network.message.ClipboardInfo;
import com.tigres810.usclb.core.network.message.UpdateClipboardItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class MainNetwork {

	private static SimpleChannel NETWORK;

	private static int packetId = 0;

	private static int id () {
		return packetId++;
	}

	public static void init () {
		SimpleChannel net = NetworkRegistry.ChannelBuilder.named( new ResourceLocation( Main.MODID, "network" ) )
				.networkProtocolVersion( () -> "1.0" ).clientAcceptedVersions( s -> true )
				.serverAcceptedVersions( s -> true ).simpleChannel();

		NETWORK = net;

		// Server messages

		net.messageBuilder( UpdateClipboardItem.class, id(), NetworkDirection.PLAY_TO_SERVER )
				.decoder( UpdateClipboardItem::decode ).encoder( UpdateClipboardItem::encode )
				.consumerMainThread( UpdateClipboardItem::handle ).add();

		// Client messages

		net.messageBuilder( ClipboardInfo.class, id(), NetworkDirection.PLAY_TO_CLIENT )
				.decoder( ClipboardInfo::decode ).encoder( ClipboardInfo::encode )
				.consumerMainThread( ClipboardInfo::handle ).add();
	}

	public static < MSG > void sendToServer ( MSG message ) {
		NETWORK.sendToServer( message );
	}

	public static < MSG > void sendToPlayer ( MSG message, ServerPlayer player ) {
		NETWORK.send( PacketDistributor.PLAYER.with( () -> player ), message );
	}

}
