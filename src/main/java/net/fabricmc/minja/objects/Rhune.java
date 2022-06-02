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
	private static Rhune RHUNE;

	public Rhune(Settings settings) {
		super(settings);
		RHUNE = this;
	}

	public static Item getRhune(){
		return RHUNE;
	}

	@Override
	public TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand) {

		//Mettre ici l'ouverture de l'HUD
		PlayerMinja player = (PlayerMinja)playerEntity;
		if(player.getSpells().size() == 0) {
			player.addSpell(new LightningBall());
			player.addSpell(new Spark());
		}
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
