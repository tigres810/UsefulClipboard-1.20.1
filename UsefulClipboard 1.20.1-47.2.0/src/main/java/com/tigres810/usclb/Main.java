package com.tigres810.usclb;

import com.mojang.logging.LogUtils;
import com.tigres810.usclb.client.renders.RenderClipboardBlock;
import com.tigres810.usclb.core.init.BlockEntityInit;
import com.tigres810.usclb.core.init.BlockInit;
import com.tigres810.usclb.core.init.CreativeTabInit;
import com.tigres810.usclb.core.init.ItemInit;
import com.tigres810.usclb.core.network.MainNetwork;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod( Main.MODID )
public class Main {

	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final String MODID = "usclb";

	public Main () {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener( this::setup );

		CreativeTabInit.register( modEventBus );
		ItemInit.register( modEventBus );
		BlockInit.register( modEventBus );
		BlockEntityInit.register( modEventBus );

		MinecraftForge.EVENT_BUS.register( this );
		modEventBus.addListener( this::addCreative );
	}

	private void addCreative ( BuildCreativeModeTabContentsEvent event ) {
		if ( event.getTabKey() == CreativeModeTabs.INGREDIENTS ) {
			event.accept( ItemInit.CLIPBOARD_ITEM );
			event.accept( ItemInit.CLIPBOARD_FOLDER_ITEM );
			event.accept( ItemInit.INK_AND_QUILL );
		}
	}

	// CLIENT SETUP
	@Mod.EventBusSubscriber( modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
	public static class ClientModEvents {

		@SubscribeEvent
		public static void onClientSetup ( FMLClientSetupEvent event ) {
			BlockEntityRenderers.register( BlockEntityInit.CLIPBOARD_BLOCK_TILE.get(), RenderClipboardBlock::new );
		}
	}

	private void setup ( final FMLCommonSetupEvent event ) // Server registration
	{
		MainNetwork.init();
	}
}
