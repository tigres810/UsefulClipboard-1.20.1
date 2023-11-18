package com.tigres810.usclb.core.init;

import com.tigres810.usclb.Main;
import com.tigres810.usclb.common.items.ClipboardItem;
import com.tigres810.usclb.common.items.ItemBase;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {

	public static final DeferredRegister< Item > ITEMS = DeferredRegister.create( ForgeRegistries.ITEMS, Main.MODID );

	public static void register ( IEventBus bus ) {
		ITEMS.register( bus );
	}

	// ITEMS
	public static final RegistryObject< Item > CLIPBOARD_ITEM = ITEMS.register( "clipboard",
			( ) -> new ClipboardItem( BlockInit.CLIPBOARD_BLOCK.get( ), new Item.Properties( ).stacksTo( 1 ) ) );
	public static final RegistryObject< Item > CLIPBOARD_FOLDER_ITEM = ITEMS.register( "clipboardfolder",
			( ) -> new ItemBase( new Item.Properties( ).stacksTo( 10 ) ) );
	public static final RegistryObject< Item > INK_AND_QUILL = ITEMS.register( "inkandquill",
			( ) -> new ItemBase( new Item.Properties( ).stacksTo( 1 ) ) );

}
