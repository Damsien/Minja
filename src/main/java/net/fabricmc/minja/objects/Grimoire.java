package net.fabricmc.minja.objects;

import net.fabricmc.minja.clocks.Clock;
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

public class Grimoire extends Item {

	private static Grimoire GRIMOIRE;

	private MinecraftClient mc = null;

	private MinecraftClientClock clock = new MinecraftClientClock(10);

	private boolean clockStarted = false;

	public Grimoire(Settings settings) {
		super(settings);
		GRIMOIRE = this;
	}

	public static Item getGrimoire(){
		return GRIMOIRE;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(mc != null) {
			GrimoireScreen grimoireScreen = new GrimoireScreen(new GrimoireGui());
			((Screen) grimoireScreen).init(mc, 145, 179);
			mc.setScreen(grimoireScreen);
		} else {
			if(!clockStarted) {
				clockStarted = true;
				clock.start();
			}
		}
		return super.use(world, player, hand);
	}

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
}
