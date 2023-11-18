package com.tigres810.usclb.client.screens;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

public class CustomEditBox extends EditBox {

	private String oldstring = "";

	public CustomEditBox ( Font pFont, int pX, int pY, int pWidth, int pHeight, Component pMessage ) {
		super( pFont, pX, pY, pWidth, pHeight, pMessage );
	}

	@Override
	public void renderWidget ( GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick ) {
		if ( this.isVisible() ) {
			if ( this.isBordered() ) {
				int i = this.isFocused() ? -1 : -6250336;
				guiGraphics.fill( this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1,
						this.getY() + this.height + 1, i );
				guiGraphics.fill( this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height,
						-16777216 );
			}

			int i2 = this.isEditable ? this.textColor : this.textColorUneditable;
			int j = this.cursorPos - this.displayPos;
			int k = this.highlightPos - this.displayPos;
			String s = this.font.plainSubstrByWidth( this.getValue().substring( this.displayPos ),
					this.getInnerWidth() );
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = this.isFocused() && this.frame / 6 % 2 == 0 && flag;
			int l = this.isBordered() ? this.getX() + 4 : this.getX();
			int i1 = this.isBordered() ? this.getY() + ( this.height - 8 ) / 2 : this.getY();
			int j1 = l;

			if ( k > s.length() ) {
				k = s.length();
			}

			if ( !s.isEmpty() ) {
				String s1 = flag ? s.substring( 0, j ) : s;
				j1 = guiGraphics.drawString( this.font, this.formatter.apply( s1, this.displayPos ), l, i1, i2, false );
			}

			boolean flag2 =
					this.cursorPos < this.getValue().length() || this.getValue().length() >= this.getMaxLength();
			int k1 = j1;

			if ( !flag ) {
				k1 = j > 0 ? l + this.width : l;
			} else if ( flag2 ) {
				k1 = j1 - 1;
				--j1;
			}

			if ( !s.isEmpty() && flag && j < s.length() ) {
				guiGraphics.drawString( this.font, this.formatter.apply( s.substring( j ), this.cursorPos ), j1, i1, i2,
						false );
			}

			if ( !flag2 && this.suggestion != null ) {
				guiGraphics.drawString( this.font, this.suggestion, k1 - 1, i1, -8355712, false );
			}

			if ( flag1 ) {
				if ( flag2 ) {
					guiGraphics.fill( RenderType.guiOverlay(), k1, i1 - 1, k1 + 1, i1 + 1 + 9, -3092272 );
				} else {
					guiGraphics.drawString( this.font, "_", k1, i1, i2, false );
				}
			}

			if ( k != j ) {
				int l1 = l + this.font.width( s.substring( 0, k ) );
				this.renderHighlight( guiGraphics, k1, i1 - 1, l1 - 1, i1 + 1 + 9 );
			}
		}
	}

	public String getOldString () {
		return oldstring;
	}

	public void setOldString ( String newstring ) {
		this.oldstring = newstring;
	}

}
