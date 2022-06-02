package net.fabricmc.minja;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.minja.objects.Grimoire;
import net.fabricmc.minja.objects.GroupItemsMinja;
import net.fabricmc.minja.objects.Rhune;
import net.fabricmc.minja.objects.Wand;
import net.fabricmc.minja.spells.*;
import net.fabricmc.minja.spells.entities.LightningBallEntity;
import net.fabricmc.minja.spells.entities.SparkEntity;
import net.fabricmc.minja.spells.items.LightningBallItem;
import net.fabricmc.minja.spells.items.SparkItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Minja implements ModInitializer {

	public static final String MOD_ID = "minja";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/*
	 * Every item created by Minja has to be instancied one here
	 */
	private static Wand WAND = new Wand(new FabricItemSettings().group(GroupItemsMinja.Minja).maxCount(1).maxDamage(0));
	private static Grimoire GRIMOIRE = new Grimoire(new FabricItemSettings().group(GroupItemsMinja.Minja).maxCount(1).maxDamage(0));
	private static Rhune RHUNE = new Rhune(new FabricItemSettings().group(GroupItemsMinja.Minja).maxCount(1).maxDamage(0));
	/* ****************************************************************** */

	/*
	 * Every Spell that is linked to an iten has is item instancied here
	 */
	public static final Item LIGHTNINGBALL = new LightningBallItem(new Item.Settings().group(GroupItemsMinja.Minja).maxCount(1));
	public static final Item SPARK = new SparkItem(new Item.Settings().group(GroupItemsMinja.Minja).maxCount(1));
	/* ****************************************************************** */

	/*
	 * Regardless of if a spell is linked to an item, every spell has to be an 'Entity' in the game.
	 * Every spell Entity is registered here
	 */
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
	/* ****************************************************************** */

	/*
	 * CETTE LIGNE DOIT ETRE EXPLIQUEE : ARNAUD ???
	 * En plus spells_map n'est jamais utilisé, normal ?
	 */
	public static Map<String, Spell> SPELLS_MAP = initializeAllSpells();

	/*
	 * CETTE FINCTION DOIT ETRE EXPLIQUEE : DAMIEN
	 */
	private static Map<String, Spell> initializeAllSpells() {
		Map<String, Spell> map = new HashMap<String, Spell>();

		Spell lightningBall = new LightningBall();
		map.put(lightningBall.getName()+"/"+lightningBall.getType(), lightningBall);

		return map;
	}

	/*
	 * This code runs as soon as Minecraft is in a mod-load-ready state.
	 * It is used to initialize (register) every item we created so it can be in the game.
	 * Warning : some things (like resources) may still be uninitialized.
	 */
	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("objects", "wand"), WAND);
		LOGGER.info("Wand launched");

		Registry.register(Registry.ITEM, new Identifier("objects", "grimoire"), GRIMOIRE);
		LOGGER.info("Grimoire launched");

		Registry.register(Registry.ITEM, new Identifier("objects", "rhune"), RHUNE);
		LOGGER.info("Rhune launched");

		Registry.register(Registry.ITEM, new Identifier("spells", "lightningball"), LIGHTNINGBALL);
		LOGGER.info("Lightning Ball launched");

		Registry.register(Registry.ITEM, new Identifier("spells", "spark"), SPARK);
		LOGGER.info("Spark launched");
	}
}
