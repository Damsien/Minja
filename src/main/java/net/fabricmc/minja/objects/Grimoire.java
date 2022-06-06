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

/*
 * Grimoire creates a Grimoire object and defines its interactions to the minecraft world.
 * The Grimoire will be used to show the different Minja informations of the player :
 * 	- name, mana, available spells, magic level, etc.
 */
public class Grimoire extends MinjaItem {

	/*********************************************************************
	 * 						GENERAL (Constructor + getter)
	 ********************************************************************* */

	/*
	 * The class Grimoire creates an object Grimoire itself.
	 * We should have only one Grimoire object : no other class creates a Grimoire object.
	 */
	private static Grimoire GRIMOIRE;

	/*
	 * Constructor of Grimoire, using the constructor of Item.
	 */
	public Grimoire(Settings settings) {
		super(settings);
		GRIMOIRE = this;
	}

	/*
	 * A getter to access the Grimoire object
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

	/*********************************************************************
	 * 						WORLD INTERACTIONS : Right-click
	 ********************************************************************* */

	/*
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
