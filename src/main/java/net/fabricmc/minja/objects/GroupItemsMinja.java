package net.fabricmc.minja.objects;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/*
 * GroupItemsMinja is creating an inventory group of all the minja objects.
 */
public class GroupItemsMinja {

    /*
     * The ItemGroup Minja is creating an inventory group of all the minja objects.
     * Any Minja object will be find in the inventory (creative gamemode) under the Minja Group
     */
    public static final ItemGroup Minja = FabricItemGroupBuilder.create(
                    new Identifier("minja", "general"))
            .icon(() -> new ItemStack(Wand.getWand())).build();
}
