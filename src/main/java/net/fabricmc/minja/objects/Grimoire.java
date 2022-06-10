package net.fabricmc.minja.objects;

import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.enumerations.Side;
import net.fabricmc.minja.gui.GrimoireGui;
import net.fabricmc.minja.gui.GrimoireScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Item representing a magic Grimoire. <br><br>
 *
 * The Grimoire will be used by the player to show his different Minja information :
 * <ul>
 *		<li> name, mana, available spells, magic level, etc </li>
 * </ul>
 *
 *
 */
public class Grimoire extends MinjaItem {

	/**
	 * The class Grimoire creates an object Grimoire itself.
	 * We should have only one Grimoire object : no other class creates a Grimoire object.
	 */
	private static Grimoire GRIMOIRE;

	/** Constructor of Grimoire, using the constructor of Item
	 */
	public Grimoire(Settings settings) {
		super(settings);
		GRIMOIRE = this;
	}

	/** A getter to access the Grimoire object
	 *
	 * @return the static instance of Grimoire
	 */
	public static Item getGrimoire(){
		return GRIMOIRE;
	}

	@Override
	public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, Side side) {
		if(side == Side.CLIENT) {
			GrimoireScreen grimoireScreen = new GrimoireScreen(new GrimoireGui(user));
			MinecraftClient mc = MinecraftClient.getInstance();
			((Screen) grimoireScreen).init(mc, 144, 198);
			mc.setScreen(grimoireScreen);
		}
		return TypedActionResult.success(user.getStackInHand(hand));
	}
}
