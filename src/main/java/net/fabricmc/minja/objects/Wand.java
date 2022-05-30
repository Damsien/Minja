package net.fabricmc.minja.objects;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.minja.Exceptions.NotEnoughtManaException;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.hud.SpellHUD;
import net.fabricmc.minja.mixin.PlayerEntityMixin;
import net.fabricmc.minja.spells.LightningBall;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.world.World;

public class Wand extends Item {
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

		// Cast the spell associated
		PlayerMinja player = (PlayerMinja) playerEntity;
		player.addSpell(new LightningBall());
		player.getActiveSpell().cast(playerEntity);

		// System.out.println("Mana1 : " + ((PlayerMinja) playerEntity).getMana());
		try {
			((PlayerMinja) playerEntity).removeMana(player.getActiveSpell().getManaCost());

		} catch (NotEnoughtManaException e) {
			// throw new RuntimeException(e);
			// TODO : A UPGRADE

		}
		// System.out.println("Mana2 : " + ((PlayerMinja) playerEntity).getMana());

		// TODO : A SUPPRIMER ??
		// LightningBall test = new LightningBall();
		// test.cast(playerEntity);

		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	@Override
	//Click gauche
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {

		if (clickType == ClickType.LEFT) {
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
