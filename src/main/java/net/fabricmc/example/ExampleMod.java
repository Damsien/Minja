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
	//private static Item WAND = Wand.getWand();
	//private static Wand WAND = new Wand(new FabricItemSettings().group(GroupItemsMinja.Minja).maxCount(1).maxDamage(0));
	private static Wand WAND = new Wand(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).maxDamage(0));


	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");


	public static Map<String, Spell> SPELLS_MAP = initializeAllSpells();

	private static Map<String, Spell> initializeAllSpells() {
		Map<String, Spell> map = new HashMap<String, Spell>();

		Spell lightningBall = new LightningBall();
		map.put(lightningBall.getName()+"/"+lightningBall.getType(), lightningBall);

		return map;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.ITEM, new Identifier("tutorial", "custom_item"), CUSTOM_ITEM);

		Registry.register(Registry.ITEM, new Identifier("wand", "wand"), WAND);
		LOGGER.info("Wand launched");
	}
}
