package com.tigres810.usclb.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.tigres810.usclb.common.blockentitys.EntityClipboardBlock;
import com.tigres810.usclb.common.blocks.ClipboardBlock;
import com.tigres810.usclb.common.data.ClipboardOrientation;
import com.tigres810.usclb.common.data.Page;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RenderClipboardBlock implements BlockEntityRenderer< EntityClipboardBlock > {

	//private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation( Main.MODID, "block/page_buttons" );
	private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation( "block/stone" );
	private static final Quaternionf FLOOR_ROTATION = new Quaternionf().rotateAxis( -90, 1, 0, 0 );

	private final Font font;

	public RenderClipboardBlock ( BlockEntityRendererProvider.Context context ) {
		this.font = context.getFont();
	}

	@Override
	public void render ( EntityClipboardBlock tileEntityIn, float partialTicks, PoseStack matrixStackIn,
			MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn ) {
		BlockState state = tileEntityIn.getBlockState();
		ClipboardOrientation orientation = state.getValue( ClipboardBlock.PROPERTY );

		matrixStackIn.pushPose();

		matrixStackIn.translate( .5, .5, .5 );
		if ( orientation.isFloor() ) {
			matrixStackIn.mulPose( FLOOR_ROTATION );
			matrixStackIn.mulPose( new Quaternionf().rotateZ( orientation.toYRot() ) );
		} else {
			matrixStackIn.mulPose( new Quaternionf().rotateY( orientation.toYRot() ) );
		}
		matrixStackIn.translate( -.5, -.5, -.5 );

		drawPageText( tileEntityIn.getActivePage(), matrixStackIn, bufferIn );
		drawButtons( tileEntityIn.getBlockPos(), state, bufferIn, matrixStackIn, combinedOverlayIn, combinedLightIn );

		matrixStackIn.popPose();
	}

	private void drawPageText ( Page page, PoseStack poseStack, MultiBufferSource bufferIn ) {
		if ( page != null ) {
			poseStack.pushPose();

			poseStack.translate( 5.45 / 16.0, 11.1D / 16D, 0.01D );
			poseStack.scale( 0.003F, -0.003F, 0.003F );

			Matrix4f matrix4f = poseStack.last().pose();

			for ( int i = 0; i < Page.LINE_COUNT; i++ ) {
				this.font.drawInBatch( page.line( i ).getString(), 0F, 11F * i, 0, false, matrix4f, bufferIn,
						Font.DisplayMode.NORMAL, 0, 0 );
			}

			poseStack.popPose();
		}
	}

	@SuppressWarnings( { "deprecation", "resource" } )
	private static void drawButtons ( BlockPos pos, BlockState state, MultiBufferSource buffer, PoseStack poseStack,
			int overlay, int light ) {
		VertexConsumer builder = buffer.getBuffer( Sheets.cutoutBlockSheet() );
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas( TextureAtlas.LOCATION_BLOCKS )
				.apply( BUTTON_TEXTURE );

		HitResult hit = Minecraft.getInstance().hitResult;
		drawButtonTexture( builder, poseStack, false, ClipboardBlock.isHoveringButton( pos, state, hit, false ), sprite,
				overlay, light );
		drawButtonTexture( builder, poseStack, true, ClipboardBlock.isHoveringButton( pos, state, hit, true ), sprite,
				overlay, light );
	}

	private static void drawButtonTexture ( VertexConsumer builder, PoseStack poseStack, boolean right, boolean hovered,
			TextureAtlasSprite sprite, int overlay, int light ) {
		Matrix4f matrix = poseStack.last().pose();
		Matrix3f normal = poseStack.last().normal();

		float minX = right ? ClipboardBlock.BTN_RIGHT_MIN_X : ClipboardBlock.BTN_LEFT_MIN_X;
		float maxX = right ? ClipboardBlock.BTN_RIGHT_MAX_X : ClipboardBlock.BTN_LEFT_MAX_X;

		float minU = sprite.getU( right ? 8 : 0 );
		float maxU = sprite.getU( right ? 16 : 8 );
		float minV = sprite.getV( hovered ? 8 : 0 );
		float maxV = sprite.getV( hovered ? 16 : 8 );

		builder.vertex( matrix, minX, ClipboardBlock.BTN_MAX_Y, 0.01F ).color( 255, 255, 255, 255 ).uv( maxU, maxV )
				.overlayCoords( overlay ).uv2( light ).normal( normal, 0, 0, -1 ).endVertex();
		builder.vertex( matrix, minX, ClipboardBlock.BTN_MIN_Y, 0.01F ).color( 255, 255, 255, 255 ).uv( maxU, minV )
				.overlayCoords( overlay ).uv2( light ).normal( normal, 0, 0, -1 ).endVertex();
		builder.vertex( matrix, maxX, ClipboardBlock.BTN_MIN_Y, 0.01F ).color( 255, 255, 255, 255 ).uv( minU, minV )
				.overlayCoords( overlay ).uv2( light ).normal( normal, 0, 0, -1 ).endVertex();
		builder.vertex( matrix, maxX, ClipboardBlock.BTN_MAX_Y, 0.01F ).color( 255, 255, 255, 255 ).uv( minU, maxV )
				.overlayCoords( overlay ).uv2( light ).normal( normal, 0, 0, -1 ).endVertex();
	}
}