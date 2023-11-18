package com.tigres810.usclb.common.blockentitys;

import java.util.ArrayList;
import java.util.List;

import com.tigres810.usclb.common.data.Page;
import com.tigres810.usclb.core.init.BlockEntityInit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EntityClipboardBlock extends BlockEntity {

	private String author = "";
	private final List< Page > pages = new ArrayList<>( );
	private Page activePage = null;

	private int activeIndex = 0;

	public EntityClipboardBlock ( BlockPos pos, BlockState state ) {
		super( BlockEntityInit.CLIPBOARD_BLOCK_TILE.get( ), pos, state );
	}

	public void switchToPreviousPage ( ) {

		if ( activeIndex > 0 ) {
			setActivePage( activeIndex - 1 );
		}
	}

	public void switchToNextPage ( ) {

		if ( activeIndex < ( pages.size( ) - 1 ) ) {
			setActivePage( activeIndex + 1 );
		}
	}

	private void setActivePage ( int idx ) {

		if ( idx != activeIndex ) {
			activeIndex = idx;
			setChanged( );

			if ( activeIndex < pages.size( ) ) {
				activePage = pages.get( activeIndex );
				level.sendBlockUpdated( worldPosition, getBlockState( ), getBlockState( ), Block.UPDATE_ALL );
			}
		}
	}

	public Page getActivePage ( ) { return activePage; }

	@Override
	public Packet< ClientGamePacketListener > getUpdatePacket ( ) {
		return ClientboundBlockEntityDataPacket.create( this );
	}

	@Override
	public void onDataPacket ( Connection net, ClientboundBlockEntityDataPacket pkt ) {
		CompoundTag tag = pkt.getTag( );

		if ( tag != null ) {
			handleUpdateTag( tag );
		}
	}

	@Override
	public CompoundTag getUpdateTag ( ) {
		CompoundTag nbtTagCompound = new CompoundTag( );

		nbtTagCompound.putString( "author", author );

		if ( this.activePage != null ) {
			nbtTagCompound.put( "page", activePage.serialize( ) );
		}

		return nbtTagCompound;
	}

	@Override
	public void handleUpdateTag ( CompoundTag tag ) {
		this.author = tag.getString( "author" );

		if ( tag.contains( "page" ) ) {
			this.activePage = Page.deserialize( tag.getList( "page", Tag.TAG_COMPOUND ) );
		} else {
			this.activePage = null;
		}
	}

	@Override
	protected void saveAdditional ( CompoundTag pTag ) {
		super.saveAdditional( pTag );
		ListTag pagesNBT = new ListTag( );

		for ( Page page : pages ) {
			pagesNBT.add( page.serialize( ) );
		}

		pTag.putString( "author", author );
		pTag.put( "pages", pagesNBT );
	}

	@Override
	public void load ( CompoundTag pTag ) {
		super.load( pTag );
		this.author = pTag.getString( "author" );

		this.pages.clear( );

		for ( Tag page : pTag.getList( "pages", Tag.TAG_LIST ) ) {
			this.pages.add( Page.deserialize( ( ListTag ) page ) );
		}

		if ( this.pages.isEmpty( ) ) {
			pages.add( Page.emptyPage( ) );
			activeIndex = 0;
		}
		this.activePage = this.pages.get( activeIndex );
	}

}