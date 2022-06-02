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


}
