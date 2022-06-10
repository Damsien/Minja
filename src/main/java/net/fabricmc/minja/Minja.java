package net.fabricmc.minja;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.minja.objects.Grimoire;
import net.fabricmc.minja.objects.Wand;
import net.fabricmc.minja.spells.*;
import net.fabricmc.minja.spells.entities.LightningBallEntity;
import net.fabricmc.minja.spells.entities.SoulSparkEntity;
import net.fabricmc.minja.spells.entities.SparkEntity;
import net.fabricmc.minja.spells.items.LightningBallItem;
import net.fabricmc.minja.spells.items.SoulSparkItem;
import net.fabricmc.minja.spells.items.SparkItem;
import net.fabricmc.minja.world.structure.ModStructures;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;

/**
 * Minja is the class that if first used when the MOD is launching.
 * It is used for initialisation of minja items, spells, etc
 */
public class Minja implements ModInitializer {

	/*********************************************************************
	 * 						GENERAL
	 ********************************************************************* */

	/**
	 * That String defines how we will refer to the entire project.
	 * Whenever we need to find something that our MOD add to the game, it will be under that name
	 */
	private static final String MOD_ID = "minja";

	/**
	 * This logger is used to write text to the console and the log file.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	/********************************************************************
	* 						ITEMS
	********************************************************************* */

	/**
	 * The ItemGroup Minja is creating an inventory group of all the minja objects.
	 * Any Minja object will be find in the inventory (creative gamemode) under the Minja Group
	 */
	public static final ItemGroup MinjaItemGroup = FabricItemGroupBuilder.create(
					new Identifier("minja"))
			.icon(() -> new ItemStack(Wand.getWand())).build();

	/**
	 * Every item created by Minja has to be instancied one here
	 */
	private static Wand WAND = new Wand(new FabricItemSettings().group(MinjaItemGroup).maxCount(1).maxDamage(0));
	private static Grimoire GRIMOIRE = new Grimoire(new FabricItemSettings().group(MinjaItemGroup).maxCount(1).maxDamage(0));
	/* ****************************************************************** */

	/**
	 * Every Spell that is linked to an item has is item instancied here
	 */
	public static final Item LIGHTNINGBALL = new LightningBallItem(new Item.Settings().group(MinjaItemGroup).maxCount(1));
	public static final Item SPARK = new SparkItem(new Item.Settings().group(MinjaItemGroup).maxCount(1));
	public static final Item SOUL_SPARK = new SoulSparkItem(new Item.Settings().group(MinjaItemGroup).maxCount(1));
	/* ****************************************************************** */

	/**
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

	public static final EntityType<SoulSparkEntity> SoulSparkEntityType = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("spells", "soul_spark"),
			FabricEntityTypeBuilder.<SoulSparkEntity>create(SpawnGroup.MISC, SoulSparkEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(4).trackedUpdateRate(10)
					.build()
	);
	/* ****************************************************************** */

	/*********************************************************************
	 * 						SPELLS INITIALISATION
	 ********************************************************************* */

	/**
	 * CETTE LIGNE DOIT ETRE EXPLIQUEE : ARNAUD ???
	 * En plus spells_map n'est jamais utilisé, normal ?
	 * TODO : A EXPLIQUER
	 */
	public static Map<String, Spell> SPELLS_MAP = initializeAllSpells();

	/**
	 * CETTE FINCTION DOIT ETRE EXPLIQUEE : DAMIEN
	 * TODO : A EXPLIQUER
	 */
	private static Map<String, Spell> initializeAllSpells() {
		Map<String, Spell> map = new HashMap<String, Spell>();

		Spell lightningBall = new LightningBall();
		map.put(lightningBall.getName()+"/"+lightningBall.getType(), lightningBall);
		Spell spark = new Spark();
		map.put(spark.getName()+"/"+spark.getType(), spark);
		Spell soulSpark = new SoulSpark();
		map.put(soulSpark.getName()+"/"+soulSpark.getType(), soulSpark);

		return map;
	}

	/*********************************************************************
	 * 						ITEMS INITIALISATION
	 ********************************************************************* */

	/**
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

		Registry.register(Registry.ITEM, new Identifier("spells", "lightningball"), LIGHTNINGBALL);
		LOGGER.info("Lightning Ball launched");

		Registry.register(Registry.ITEM, new Identifier("spells", "spark"), SPARK);
		LOGGER.info("Spark launched");

		Registry.register(Registry.ITEM, new Identifier("spells", "soul_spark"), SOUL_SPARK);
		LOGGER.info("Spark launched");

		//Register every Minja structure
		ModStructures.RegisterStructureFeatures();
	}
}
