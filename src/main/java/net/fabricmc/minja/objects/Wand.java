package net.fabricmc.minja.objects;

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
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends Item  {

	private static Wand WAND;

	public Wand(Settings settings) {
		super(settings);
		WAND = this;
	}

	public static Item getWand(){
		return WAND;
	}

	@Override
	//Used when the player use right click with the Wand
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

		//Mettre ici l'ouverture de l'HUD
		//SpellHUD.setVisible(true);

		//Ci dessous, tests pour arnaud, pas touche
		LightningBall test = new LightningBall();
		test.cast(playerEntity);

		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	@Override
	//Click gauche
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
	{
		if(clickType == ClickType.LEFT) {
			player.playSound(SoundEvents.ENTITY_COW_AMBIENT, 1.0F, 1.0F);

			((PlayerMinja) player).addMana(20);
			try {
				((PlayerMinja) player).removeMana(10);
			} catch (NotEnoughtManaException e) {
				System.out.println(e.getMessage());
			}
			return true;
		} else {
			return false;
		}
	}
}
