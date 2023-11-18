package com.tigres810.usclb.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tigres810.usclb.Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class ClipboardButton extends Button {

	final ResourceLocation gui = new ResourceLocation( Main.MODID, "textures/gui/clipboardgui.png" );

	private static final int buttonwidth = 12;
	private static final int buttonheight = 11;

	private static final int u = 49; // right to left
	private static final int v = 206; // up to down
	private static final int v1 = 194;

	private int oldstate;
	private int state;

	public ClipboardButton ( int pX, int pY, OnPress pOnPress ) {
		super( pX, pY, buttonwidth, buttonheight, Component.literal( "" ), pOnPress, Button.DEFAULT_NARRATION );
	}

	@Override
	public void onPress () {
		changestate();
		super.onPress();
	}

	public int getOldState () {
		return this.oldstate;
	}

	public int getState () {
		return this.state;
	}

	public void setOldState ( int newstate ) {
		oldstate = newstate;
	}

	public void setState ( int newstate ) {
		state = newstate;
	}

	private void changestate () {
		if ( state == 0 ) {
			state = 1;
		} else if ( state == 1 ) {
			state = 2;
		} else if ( state == 2 ) {
			state = 0;
		}
	}

	@Override
	public void render ( GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick ) {
		RenderSystem.setShaderTexture( 0, gui );

		if ( state == 1 ) {
			guiGraphics.blit( gui, getX(), getY(), u, v, buttonwidth, buttonheight );
		} else if ( state == 2 ) {
			guiGraphics.blit( gui, getX(), getY(), u, v1, buttonwidth, buttonheight );
		}
	}

	@Override
	public void playDownSound ( SoundManager pHandler ) {
		pHandler.play( SimpleSoundInstance.forUI( SoundEvents.UI_BUTTON_CLICK, 1.0F ) );
	}

}
