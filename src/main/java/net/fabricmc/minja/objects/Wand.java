package net.fabricmc.minja.objects;

import net.fabricmc.minja.events.MinjaEvent;
import net.fabricmc.minja.exceptions.NotEnoughtManaException;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.hud.SpellHUD;
import net.fabricmc.minja.spells.LightningBall;
import net.fabricmc.minja.spells.Spark;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends MinjaItem {
	private static Wand WAND;

	public Wand(Settings settings) {
		super(settings);
		WAND = this;
	}

	public static Item getWand(){
		return WAND;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		return stack;
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand) {

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

	@Override
	public MinjaEvent onLeftClickPressed(Hand hand, boolean playerFromServer) {

		PlayerEntity playerEntity = MinecraftClient.getInstance().player;
		PlayerMinja player = (PlayerMinja)playerEntity;


		try {

			// Cast the spell associated
			Spell spell = player.getActiveSpell();

			spell.cast(playerEntity);

			// Remove the current spell's mana cost to the mana of the player
			player.removeMana(spell.getManaCost());



		} catch (NotEnoughtManaException e) {
			// throw new RuntimeException(e);
			// TODO : A UPGRADE
		}



		return MinjaEvent.SUCCEED;
	}

}
