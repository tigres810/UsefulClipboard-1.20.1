package com.tigres810.usclb.core.init;

import com.tigres810.usclb.Main;
import com.tigres810.usclb.common.blockentitys.EntityClipboardBlock;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {

	public static final DeferredRegister< BlockEntityType< ? > > TILEENTITYS = DeferredRegister.create(
			ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID );

	public static void register ( IEventBus bus ) {
		TILEENTITYS.register( bus );
	}

	// TILEENTITYS
	public static final RegistryObject< BlockEntityType< EntityClipboardBlock > > CLIPBOARD_BLOCK_TILE = TILEENTITYS.register(
			"clipboard", () -> BlockEntityType.Builder.of( EntityClipboardBlock::new, BlockInit.CLIPBOARD_BLOCK.get() )
					.build( null ) );

}
