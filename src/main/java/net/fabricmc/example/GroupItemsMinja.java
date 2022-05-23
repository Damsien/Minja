package net.fabricmc.example;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GroupItemsMinja {
	//private static Wand WAND = new Wand(new FabricItemSettings().group(GroupItemsMinja.Minja));
	public static final ItemGroup Minja = FabricItemGroupBuilder.build(
			new Identifier("minja", "general"),
			() -> new ItemStack(Wand.getWand()),
			() -> new ItemStack((ItemConvertible) SpellProjectile.getSpell("lightningball")));
}
