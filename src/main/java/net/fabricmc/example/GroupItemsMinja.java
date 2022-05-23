package net.fabricmc.example;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GroupItemsMinja {

	public static final ItemGroup Minja = FabricItemGroupBuilder.build(
			new Identifier("Minja", "general"),
			() -> new ItemStack(Wand.getWand()));
}
