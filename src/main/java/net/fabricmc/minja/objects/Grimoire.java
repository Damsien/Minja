package net.fabricmc.minja.objects;

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

public class Grimoire extends Item  {

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
	//Se lance quand utilisé par un joueur
	//Click droit
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		playerEntity.playSound(SoundEvents.ENTITY_COW_AMBIENT, 1.0F, 1.0F);
		//Mettre ici l'ouverture de l'HUD
		SpellHUD.setVisible(true);

		//Pas touche en dessous
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

		SpellHUD.setVisible(false);

		return stack;
	}

}
