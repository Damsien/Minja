package net.fabricmc.minja.objects;

import net.minecraft.item.Item;

public class Rhune extends MinjaItem {
	private static Rhune RHUNE;

	public Rhune(Settings settings) {
		super(settings);
		RHUNE = this;
	}

	public static Item getRhune(){
		return RHUNE;
	}


}
