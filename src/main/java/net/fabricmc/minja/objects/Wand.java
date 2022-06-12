package net.fabricmc.minja.objects;

import net.fabricmc.minja.enumerations.MinjaEvent;
import net.fabricmc.minja.exceptions.NotEnoughManaException;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.hud.SpellHUD;
import net.fabricmc.minja.spells.SoulSpark;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Item representing a magic wand. <br><br>
 *
 * The wand allows a player to :
 * <ul>
 *		<li> select a spell from their quick spell list with a right click </li>
 *		<li> cast it if he/she has enough mana with a left click (when pressing or releasing depending on the spell) </li>
 * </ul>
 *
 *
 */

public class Wand extends MinjaItem {

	/**
	 * The class Wand creates an object Wand itself.
	 * We should have only one Wand object : no other class creates a Wand object.
	 */
	private static Wand WAND;

	/** Constructor of Wand, using the constructor of Item
	 */
	public Wand(Settings settings) {
		super(settings);
		WAND = this;
	}

	/** Obtain an instance of the wand
	 *
	 * @return the wand
	 */
	public static Item getWand(){
		return WAND;
	}


	@Override
	public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean isOtherClickActivated, Side side) {


		if(!isOtherClickActivated) {
			PlayerMinja player = (PlayerMinja) playerEntity;
			Spell courant = player.getActiveSpell();
			if(!(courant instanceof SoulSpark)) {
				try {
					player.removeMana(courant.getManaCost());
					courant.cast(playerEntity);
				} catch (NotEnoughManaException e) {
					System.out.println("Not enought mana");
				}
			}
			return MinjaEvent.SUCCEED;
		}



		return MinjaEvent.CANCELED;
	}

	@Override
	public MinjaEvent onLeftClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean isOtherClickActivated, Side side) {

		if(!isOtherClickActivated) {
			//playerEntity.sendMessage(new LiteralText("LEFT CLICK MAINTAINED"), false);
			PlayerMinja player = (PlayerMinja) playerEntity;
			Spell courant = player.getActiveSpell();
			if(courant instanceof SoulSpark) {
				((SoulSpark) courant).precast(playerEntity);
			}

		}

		return MinjaEvent.CANCELED;
	}

	@Override
	public MinjaEvent onLeftClickReleased(World world, PlayerEntity playerEntity, Hand hand, boolean isOtherClickActivated, Side side) {

		if(!isOtherClickActivated) {
			PlayerMinja player = (PlayerMinja) playerEntity;
			Spell courant = player.getActiveSpell();
			if(courant instanceof SoulSpark) {
				courant.cast(playerEntity);
			}
			return MinjaEvent.SUCCEED;
		}

		return MinjaEvent.CANCELED;

	}

	@Override
	public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand,  boolean isOtherClickActivated, Side side) {

		if(side == Side.CLIENT) {
			SpellHUD.setVisible(true);
		}

		return TypedActionResult.pass(playerEntity.getStackInHand(hand));

	}

	@Override
	public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand,  boolean isOtherClickActivated, Side side) {




		// Cancel Event ==> Cancel hand animation
		return TypedActionResult.pass(playerEntity.getStackInHand(hand));
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand,  boolean isOtherClickActivated, Side side) {

		PlayerMinja player = (PlayerMinja)playerEntity;

		//Mettre ici l'ouverture de l'HUD
		if(side == Side.CLIENT) {
			SpellHUD.setVisible(false);
			int spellIndex = SpellHUD.getSelectedIndex();
			player.setActiveSpell(spellIndex);

			NetworkEvent.updateSpellIndex(spellIndex);

		}

		return null;
	}




}

