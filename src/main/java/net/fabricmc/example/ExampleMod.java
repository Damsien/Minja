package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ExampleMod implements ModInitializer {
	// an instance of our new item
	public static final Item CUSTOM_ITEM = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");


	public static Map<Map<String, String>, Spell> SPELLS_MAP = initializeAllSpells();

	private static Map<Map<String, String>, Spell> initializeAllSpells() {
		Map<Map<String, String>, Spell> map = new HashMap<Map<String, String>, Spell>();
		Map<String, String> mapEntry = new HashMap<String, String>();

		Spell lightningBall = new LightningBall();
		mapEntry.put(lightningBall.getName(), lightningBall.getType());
		map.put(mapEntry, lightningBall);
		mapEntry.clear();

		return map;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.ITEM, new Identifier("tutorial", "custom_item"), CUSTOM_ITEM);

		Registry.register(Registry.ITEM, new Identifier("wand", "custom_item"), Wand.getWand());
		LOGGER.info("Wand launched");
	}
}
