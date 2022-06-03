package net.fabricmc.minja.objects;

		import net.fabricmc.minja.events.MinjaEvent;
		import net.fabricmc.minja.events.Side;
		import net.fabricmc.minja.exceptions.NotEnoughtManaException;
		import net.fabricmc.minja.PlayerMinja;
		import net.fabricmc.minja.hud.SpellHUD;
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
	public MinjaEvent onLeftClickPressed(PlayerEntity playerEntity, Hand hand, boolean playerFromServer, Side side) {

		PlayerMinja player = (PlayerMinja) playerEntity;

		if(Side.CLIENT == side) {
			playerEntity.sendMessage(new LiteralText(player.getActiveSpell().getName()),true);
			try {
				player.removeMana(player.getActiveSpell().getManaCost());
			} catch (NotEnoughtManaException e) {
				// throw new RuntimeException(e);
				// TODO : A UPGRADE
			}

		}

		if(Side.SERVER == side) {
			playerEntity.sendMessage(new LiteralText(player.getActiveSpell().getName()),false);
			player.getActiveSpell().cast(playerEntity);
		}



		return MinjaEvent.SUCCEED;
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand, Side side) {

		SpellHUD.setVisible(true);

		return TypedActionResult.success(playerEntity.getStackInHand(hand));

	}

	@Override
	public TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand, Side side) {

		// Cancel Event ==> Cancel hand animation
		return TypedActionResult.pass(playerEntity.getStackInHand(hand));
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand, Side side) {
		//Mettre ici l'ouverture de l'HUD
		SpellHUD.setVisible(false);

		return null;
	}



}

