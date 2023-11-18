package com.tigres810.usclb.core.init;

import com.tigres810.usclb.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit {

	public static final DeferredRegister< CreativeModeTab > CREATIVE_TABS = DeferredRegister.create(
			Registries.CREATIVE_MODE_TAB, Main.MODID );

	public static void register ( IEventBus bus ) {
		CREATIVE_TABS.register( bus );
	}

	public static final RegistryObject< CreativeModeTab > TAB = CREATIVE_TABS.register( "usclb_tab",
			() -> CreativeModeTab.builder().icon( () -> new ItemStack( ItemInit.CLIPBOARD_ITEM.get() ) )
					.title( Component.translatable( "itemGroup.usclb_tab" ) )
					.displayItems( ( pParameters, pOutput ) -> {
						pOutput.accept( ItemInit.CLIPBOARD_ITEM.get() );
						pOutput.accept( ItemInit.CLIPBOARD_FOLDER_ITEM.get() );
						pOutput.accept( ItemInit.INK_AND_QUILL.get() );
					} ).build() );
}
