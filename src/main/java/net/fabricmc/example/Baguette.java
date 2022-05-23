package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Baguette extends Item implements ModInitializer  {

	//public static ItemConvertible Baguette;
	public static final Item Baguette = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final Logger LOGGER = LoggerFactory.getLogger("Baguette");
	//Item texture: /resources/assets/Items/Objects/Baguette.png

	public Baguette(Settings settings)
	{
		super(settings);
	}

	@Override
	//Se lance quand utilisé par un joueur
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
		//Mettre ici l'ouverture de l'HUD
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.ITEM, new Identifier("Baguette", "custom_item"), Baguette);
		//LOGGER.info("Hello Fabric world!");
		LOGGER.info("Baguette launched");
	}
}
