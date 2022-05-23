package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Baguette extends Item implements ModInitializer {

	//public static ItemConvertible Baguette;
	public static final Item Baguette = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	public Baguette(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.ITEM, new Identifier("tutorial", "custom_item"), Baguette);
		//LOGGER.info("Hello Fabric world!");
	}

}
