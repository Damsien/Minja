package net.fabricmc.minja;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.minja.objects.Grimoire;
import net.fabricmc.minja.objects.GroupItemsMinja;
import net.fabricmc.minja.objects.Wand;
import net.fabricmc.minja.spells.*;
import net.fabricmc.minja.spells.entities.LightningBallEntity;
import net.fabricmc.minja.spells.entities.SparkEntity;
import net.fabricmc.minja.spells.items.LightningBallItem;
import net.fabricmc.minja.spells.items.SparkItem;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Minja implements ModInitializer {
	
	//Every item created by Minja
	private static Wand WAND = new Wand(new FabricItemSettings().group(GroupItemsMinja.Minja).maxCount(1).maxDamage(0));
	private static Grimoire GRIMOIRE = new Grimoire(new FabricItemSettings().group(GroupItemsMinja.Minja).maxCount(1).maxDamage(0));
	public static final Item LIGHTNINGBALL = new LightningBallItem(new Item.Settings().group(GroupItemsMinja.Minja).maxCount(1));
	public static final Item SPARK = new SparkItem(new Item.Settings().group(GroupItemsMinja.Minja).maxCount(1));

	public static final EntityType<LightningBallEntity> LightningBallEntityType = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("spells", "lightningball"),
			FabricEntityTypeBuilder.<LightningBallEntity>create(SpawnGroup.MISC, LightningBallEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(4).trackedUpdateRate(10)
					.build()
	);

	public static final EntityType<SparkEntity> SparkEntityType = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("spells", "spark"),
			FabricEntityTypeBuilder.<SparkEntity>create(SpawnGroup.MISC, SparkEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(4).trackedUpdateRate(10)
					.build()
	);

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("LOGS");


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
		Registry.register(Registry.ITEM, new Identifier("objects", "wand"), WAND);
		LOGGER.info("Wand launched");

		Registry.register(Registry.ITEM, new Identifier("objects", "grimoire"), GRIMOIRE);
		LOGGER.info("Grimoire launched");

		Registry.register(Registry.ITEM, new Identifier("spells", "lightningball"), LIGHTNINGBALL);
		LOGGER.info("Lightning Ball launched");

		Registry.register(Registry.ITEM, new Identifier("spells", "spark"), SPARK);
		LOGGER.info("Spark launched");
	}
}
