package net.fabricmc.minja.objects;

import net.fabricmc.minja.events.MinjaItems;
import net.fabricmc.minja.hud.SpellHUD;
import net.fabricmc.minja.spells.LightningBall;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Grimoire extends MinjaItems {

	//public static ItemConvertible Baguette;
	//private static Item WAND = new Item(new FabricItemSettings().group(GroupItemsMinja.Minja));
	//private final Rarity;
	//Item texture: /resources/assets/Items/Objects/Baguette.png
	private static Grimoire GRIMOIRE;

	public Grimoire(Settings settings) {
		super(settings);
		GRIMOIRE = this;
	}

	public static Item getGrimoire(){
		return GRIMOIRE;
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand) {

		//Mettre ici l'ouverture de l'HUD
		SpellHUD.setVisible(true);

		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand) {

		// Cancel Event ==> Cancel hand animation
		return TypedActionResult.pass(playerEntity.getStackInHand(hand));
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand) {
		//Mettre ici l'ouverture de l'HUD
		SpellHUD.setVisible(false);

		return null;
	}


}
