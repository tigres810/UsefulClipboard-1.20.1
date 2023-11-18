package com.tigres810.usclb.core.init;

import com.tigres810.usclb.Main;
import com.tigres810.usclb.common.blocks.ClipboardBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	
	public static final DeferredRegister< Block > BLOCKS = DeferredRegister.create( ForgeRegistries.BLOCKS, Main.MODID );

	public static void register ( IEventBus bus ) {
		BLOCKS.register( bus );
	}
	
	// BLOCKS
	public static final RegistryObject< Block > CLIPBOARD_BLOCK = BLOCKS.register( "clipboard", () -> new ClipboardBlock( BlockBehaviour.Properties.copy( Blocks.OAK_BUTTON ) ) );

}
