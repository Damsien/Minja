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

public class Wand extends Item  {

	//public static ItemConvertible Baguette;
	private static Item WAND = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	//Item texture: /resources/assets/Items/Objects/Baguette.png

	public Wand(Settings settings) {
		super(settings);
	}

	public static Item getWand(){
		return WAND;
	}

	/*@Override
	//Se lance quand utilisé par un joueur
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
		//Mettre ici l'ouverture de l'HUD
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}*/
}
