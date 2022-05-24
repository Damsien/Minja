package net.fabricmc.minja.objects;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class GroupItemsMinja {

    public static final ItemGroup Minja = FabricItemGroupBuilder.create(
                    new Identifier("minja", "general"))
            .icon(() -> new ItemStack(Wand.getWand())).build();
}
