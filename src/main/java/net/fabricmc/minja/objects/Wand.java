package net.fabricmc.minja.objects;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.minja.Exceptions.NotEnoughtManaException;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.hud.SpellHUD;
import net.fabricmc.minja.spells.LightningBall;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends Item  {

	//public static ItemConvertible Baguette;
	//private static Item WAND = new Item(new FabricItemSettings().group(GroupItemsMinja.Minja));
	//private final Rarity;
	//Item texture: /resources/assets/Items/Objects/Baguette.png
	private static Wand WAND;

	public Wand(Settings settings) {
		super(settings);
		//settings.maxCount(1); //Sets the maximum stack count for the item
		//settings.rarity(Rarity.valueOf("epic")); //Sets the rarity of the object (change the color of the name)
		//settings.maxDamage(0); //Sets the maximum damage the item can make
		//settings.maxDamageIfAbsent(0);
		WAND = this;
	}

	public static Item getWand(){
		return WAND;
	}

	@Override
	//Se lance quand utilisé par un joueur
	//Click droit
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

		System.out.println("Mana : " + ((PlayerMinja) playerEntity).getMana());
		((PlayerMinja) playerEntity).addMana(20);
		try {
			((PlayerMinja) playerEntity).removeMana(10);
		} catch (NotEnoughtManaException e) {
			throw new RuntimeException(e);
		}

		playerEntity.playSound(SoundEvents.ENTITY_COW_AMBIENT, 1.0F, 1.0F);
		//Mettre ici l'ouverture de l'HUD

		SpellHUD.setVisible(true);

		//Ci dessous, tests pour arnaud, pas touche
		LightningBall test = new LightningBall();
		test.cast(playerEntity);

		//Pas touche en dessous
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

}
