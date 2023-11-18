package com.tigres810.usclb.client.onlyinclient;

import com.tigres810.usclb.client.screens.ClipboardScreen;
import com.tigres810.usclb.core.network.message.ClipboardInfo;

import net.minecraft.client.Minecraft;

public class OpenScreen {

	public static void openScreen ( ClipboardInfo message ) {

		Minecraft mc = Minecraft.getInstance( );

		mc.setScreen( new ClipboardScreen( message.nbt ) );
	}

}
