package net.fabricmc.minja.objects;

		import net.fabricmc.minja.enumerations.MinjaEvent;
		import net.fabricmc.minja.network.NetworkEvent;
		import net.fabricmc.minja.enumerations.Side;
		import net.fabricmc.minja.PlayerMinja;
		import net.fabricmc.minja.hud.SpellHUD;
		import net.fabricmc.minja.spells.SoulSpark;
		import net.fabricmc.minja.spells.Spell;
		import net.minecraft.entity.player.PlayerEntity;
		import net.minecraft.item.Item;
		import net.minecraft.item.ItemStack;
		import net.minecraft.text.LiteralText;
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
	public MinjaEvent onLeftClickPressed(World world, PlayerEntity playerEntity, Hand hand, boolean isOtherClickActivated, Side side) {


		if(!isOtherClickActivated) {
			PlayerMinja player = (PlayerMinja) playerEntity;
			Spell courant = player.getActiveSpell();
			if(!(courant instanceof SoulSpark)) {
				courant.cast(playerEntity);
			}
			return MinjaEvent.SUCCEED;
		}



		return MinjaEvent.CANCELED;
	}

	@Override
	public MinjaEvent onLeftClickMaintained(World world, PlayerEntity playerEntity, Hand hand, boolean isOtherClickActivated, Side side) {

		if(!isOtherClickActivated) {
			playerEntity.sendMessage(new LiteralText("LEFT CLICK MAINTAINED"), false);
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

