package com.tigres810.usclb.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tigres810.usclb.Main;
import com.tigres810.usclb.common.data.Page;
import com.tigres810.usclb.core.network.MainNetwork;
import com.tigres810.usclb.core.network.message.UpdateClipboardItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ClipboardScreen extends Screen {

	private static final int WIDTH = 192;
	private static final int HEIGHT = 192;

	private static final int OFFSETX = 64;
	private static final int OFFSETY = 15;

	private static final int OFFSETBUTTON = 12;

	private static final int OFFSETTEXTBOXX = 1;
	private static final int OFFSETTEXTBOXY = 3;

	private static final int OFFSETTEXTAUTHORX = 35;
	private static final int OFFSETTEXTAUTHORY = 164;

	private static final int OFFSETTEXTPAGES = 50;

	private static final int MAX_PAGES = 5;

	private final ResourceLocation GUI = new ResourceLocation( Main.MODID, "textures/gui/clipboardgui.png" );

	private final CustomEditBox[] txtboxes = new CustomEditBox[ Page.LINE_COUNT ];
	private final ClipboardButton[] buttons = new ClipboardButton[ Page.LINE_COUNT ];

	private CompoundTag nbt;
	private String author = "";
	private List< Page > pages = new ArrayList<>();
	private int currentpage = 0;

	public ClipboardScreen ( CompoundTag newnbt ) {
		super( Component.literal( "" ) );

		if ( newnbt != null ) {
			this.nbt = newnbt.getCompound( "BlockEntityTag" );
			this.author = this.nbt.getString( "author" );
		}
	}

	@Override
	protected void init () {
		this.minecraft.keyboardHandler.tick();
		this.clearWidgets();
		this.children().clear();

		// int centerX = ( this.width - WIDTH ) / 2;
		int txtX = this.width / 2 - OFFSETX + OFFSETBUTTON + OFFSETTEXTBOXX;
		int centerY = ( this.height - HEIGHT ) / 2;

		for ( int i = 0; i < Page.LINE_COUNT; i++ ) {
			int btnY = centerY + OFFSETY + ( OFFSETBUTTON * i );
			this.buttons[ i ] = new ClipboardButton( ( this.width / 2 - OFFSETX ), btnY, button -> {
			} );
			this.addRenderableWidget( buttons[ i ] );

			int txtY = centerY + OFFSETY + OFFSETTEXTBOXY + ( OFFSETBUTTON * i );
			this.txtboxes[ i ] = new CustomEditBox( this.font, txtX, txtY, 109, 9, Component.literal( "" ) );
			this.configureTxtBox( this.txtboxes[ i ] );
		}

		for ( int i = 0; i < Page.LINE_COUNT; i++ ) {
			this.addRenderableWidget( this.txtboxes[ i ] );
		}

		ClipboardArrowButton pagebuttonleft = new ClipboardArrowButton(
				( ( width / 2 ) - this.font.width( "test" ) / 2 ) + 41, centerY + 9, false,
				button -> changePageBack() );
		ClipboardArrowButton pagebuttonright = new ClipboardArrowButton(
				( ( width / 2 ) - this.font.width( "test" ) / 2 ) + 56, centerY + 9, true,
				button -> changePageForward() );
		this.addRenderableWidget( pagebuttonleft );
		this.addRenderableWidget( pagebuttonright );

		loadValuesNbt();
	}

	@Override
	public void tick () {

		for ( CustomEditBox editBox : this.txtboxes ) {
			editBox.tick();
		}
		checkValueChanged();
		super.tick();
	}

	@Override
	public boolean isPauseScreen () {
		return false;
	}

	private void checkValueChanged () {

		boolean changed = false;

		for ( int i = 0; i < Page.LINE_COUNT; i++ ) {

			if ( this.buttons[ i ].getOldState() != this.buttons[ i ].getState() ) {
				this.buttons[ i ].setOldState( this.buttons[ i ].getState() );
				changed = true;
			}

			if ( !this.txtboxes[ i ].getOldString().equals( this.txtboxes[ i ].getValue() ) ) {
				this.txtboxes[ i ].setOldString( this.txtboxes[ i ].getValue() );
				changed = true;
			}
		}

		if ( changed ) {

			if ( pages.isEmpty() ) {
				makePages();
			}

			Page page = pages.get( currentpage );

			for ( int i = 0; i < Page.LINE_COUNT; i++ ) {
				page.setLine( i, this.txtboxes[ i ].getValue() );
				page.setButton( i, ( byte ) this.buttons[ i ].getState() );
			}
		}
	}

	private void makePages () {

		if ( pages.isEmpty() ) {
			for ( int r = 0; r < MAX_PAGES + 1; r++ ) {
				this.pages.add( Page.emptyPage() );
			}
		}
		saveValuesNbt();
	}

	private void changePageBack () {

		if ( currentpage > 0 ) {
			currentpage--;
		}

		if ( currentpage < 0 ) {
			currentpage = 0;
		}
		loadPage();
	}

	private void changePageForward () {

		if ( currentpage < MAX_PAGES ) {
			currentpage++;
		}

		if ( currentpage >= MAX_PAGES ) {
			currentpage = MAX_PAGES - 1;
		}
		loadPage();
	}

	private void configureTxtBox ( EditBox txtEditBox ) {
		txtEditBox.setMaxLength( 21 );
		txtEditBox.setBordered( false );
		txtEditBox.setVisible( true );
		txtEditBox.setTextColor( 0x000000 );
		txtEditBox.setValue( "" );
	}

	private void loadPage () {
		saveValuesNbt();

		if ( this.pages.size() > 0 ) {
			Page page = this.pages.get( this.currentpage );

			for ( int i = 0; i < Page.LINE_COUNT; i++ ) {
				this.buttons[ i ].setState( page.button( i ) );
				this.txtboxes[ i ].setValue( page.line( i ).getString() );
			}
		}
	}

	private void loadValuesNbt () {

		if ( this.nbt != null ) {

			for ( Tag page : this.nbt.getList( "pages", Tag.TAG_LIST ) ) {
				this.pages.add( Page.deserialize( ( ListTag ) page ) );
			}

			loadPage();
		} else {
			makePages();
		}
	}

	private void saveValuesNbt () {

		CompoundTag copynbt = new CompoundTag();
		ListTag pagesNBT = new ListTag();

		if ( this.nbt == null ) {
			this.author = this.minecraft.player.getName().getString();
		}
		this.nbt = new CompoundTag();
		copynbt.putString( "author", this.author );

		for ( Page page : pages ) {
			pagesNBT.add( page.serialize() );
		}

		copynbt.put( "pages", pagesNBT );
		this.nbt.put( "BlockEntityTag", copynbt );

		MainNetwork.sendToServer( new UpdateClipboardItem( this.nbt ) );
	}

	@Override
	public void render ( GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick ) {
		RenderSystem.colorMask( true, true, true, true );
		RenderSystem.setShaderTexture( 0, GUI );
		int relX = ( this.width - WIDTH ) / 2;
		int relY = ( this.height - HEIGHT ) / 2;
		guiGraphics.blit( GUI, relX, relY, 0, 0, WIDTH, HEIGHT );
		guiGraphics.drawString( this.font, I18n.get( "gui.title" ), ( ( width / 2 ) - this.font.width( "test" ) / 2 ),
				relY + 8, 0x000000, false );

		if ( this.nbt != null ) {
			guiGraphics.drawString( this.font, I18n.get( "tooltip.usclb.clipboard.tooltip.notesby" ) + this.author,
					relX + OFFSETTEXTAUTHORX, relY + OFFSETTEXTAUTHORY, 0x000000, false );
		}
		guiGraphics.drawString( this.font, Component.literal( Integer.toString( currentpage ) ),
				( ( width / 2 ) - this.font.width( "test" ) / 2 ) + OFFSETTEXTPAGES, relY + 9, 0x000000, false );

		for ( int i = 0; i < Page.LINE_COUNT; i++ ) {
			this.txtboxes[ i ].render( guiGraphics, pMouseX, pMouseY, pPartialTick );
		}

		super.render( guiGraphics, pMouseX, pMouseY, pPartialTick );
	}

	@Override
	public void resize ( Minecraft pMinecraft, int pWidth, int pHeight ) {
		List< Page > oldlist = pages;
		super.resize( pMinecraft, pWidth, pHeight );
		pages = oldlist;
		loadPage();
	}

	@Override
	public void onClose () {
		saveValuesNbt();
		this.minecraft.keyboardHandler.tick();
		super.onClose();
	}

}