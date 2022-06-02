package net.fabricmc.minja.objects;

import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.events.MinjaItems;
import net.fabricmc.minja.hud.SpellHUD;
import net.fabricmc.minja.spells.LightningBall;
import net.fabricmc.minja.spells.Spark;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Rhune extends MinjaItems {

	/*********************************************************************
	 * 						GENERAL (Constructor + getter)
	 ********************************************************************* */

	/*
	 * The class Rhune creates an object Wand itself.
	 * We should have only one Rhune object : no other class creates a Rhune object.
	 */
	private static Rhune RHUNE;

	/*
	 * Constructor of Rhune, using the constructor of Item.
	 */
	public Rhune(Settings settings) {
		super(settings);
		RHUNE = this;
	}

	/*
	 * A getter to access the Rhune object
	 */
	public static Item getRhune(){
		return RHUNE;
	}

	/*********************************************************************
	 * 						WORLD INTERACTIONS
	 ********************************************************************* */

	/*
	 * This method is used when the player use right click with the Rhune
	 */
	@Override
	public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand) {

		//The spell
		PlayerMinja player = (PlayerMinja)playerEntity;
		if(player.getSpells().size() == 0) {
			player.addSpell(new LightningBall());
			player.addSpell(new Spark());
		}
		SpellHUD.setVisible(true);

		return TypedActionResult.success(playerEntity.getStackInHand(hand));

	}

	/*
	 * This method stays ongoing while the right-click is still pressed
	 */
	@Override
	public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand) {
		// Cancel Event ==> Cancel hand animation
		return TypedActionResult.pass(playerEntity.getStackInHand(hand));
	}

	/*
	 * This method is used when the player releases the right-click after he pressed it
	 */
	@Override
	public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand) {
		//When the right-click on rhune is released, the HUD needs to disappear
		SpellHUD.setVisible(false);

		return null;
	}
}
