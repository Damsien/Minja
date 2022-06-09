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

	/*********************************************************************
	 * 						Clock TODO : TOUT EXPLIQUER
	 ********************************************************************* */

	private MinecraftClient mc = null;

	private MinecraftClientClock clock = new MinecraftClientClock(10);

	private boolean clockStarted = false;

	private class MinecraftClientClock extends Clock {

		public MinecraftClientClock(long timer) {
			super(timer);
		}
		private World world;
		private PlayerEntity player;
		private Hand hand;
		public void initialise(World world, PlayerEntity player, Hand hand) {
			this.world = world;
			this.player = player;
			this.hand = hand;
		}
		@Override
		public void execute() {
			MinecraftClient currentMc = MinecraftClient.getInstance();
			if(currentMc == null) {
				this.start();
			} else {
				mc = currentMc;
				use(world, player, hand);
			}
		}
	}

	/**
	 * This method is used when the player use right click with the Wand
	 */
	@Override
	public TypedActionResult<ItemStack> onUse(World world, PlayerEntity player, Hand hand, Side side) {
		if(mc != null) {
			GrimoireScreen grimoireScreen = new GrimoireScreen(new GrimoireGui(player));
			((Screen) grimoireScreen).init(mc, 145, 179);
			mc.setScreen(grimoireScreen);
		} else {
			if(!clockStarted) {
				clockStarted = true;
				clock.start();
			}
		}
		return super.onUse(world, player, hand, side);
	}
}
