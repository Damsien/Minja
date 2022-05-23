package net.fabricmc.example;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.swing.text.StyledEditorKit;

public class Wand extends Item  {

	//public static ItemConvertible Baguette;
	//private static Item WAND = new Item(new FabricItemSettings().group(GroupItemsMinja.Minja));
	//private final Rarity;
	//Item texture: /resources/assets/Items/Objects/Baguette.png
	private static Wand WAND;

	public Wand(Settings settings) {
		super(settings);
		WAND = this;
	}

	public static Item getWand(){
		return WAND;
	}

	@Override
	//Se lance quand utilisé par un joueur
	//Click droit
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		playerEntity.playSound(SoundEvents.ENTITY_COW_AMBIENT, 1.0F, 1.0F);
		//Mettre ici l'ouverture de l'HUD


		//Ci dessous, tests pour arnaud, pas touche
		LightningBall test = new LightningBall();
		test.cast(playerEntity);

		//Pas touche en dessous
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}


	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks){
		playerEntity.playSound(SoundEvents.ENTITY_SILVERFISH_AMBIENT, 1.0F, 1.0F);
		LightningBall test = new LightningBall();
		test.cast(user);
	}

	@Override
	//Click gauche
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
	{
		playerEntity.playSound(SoundEvents.ENTITY_WOLF_AMBIENT, 1.0F, 1.0F);
		LightningBall test = new LightningBall();
		test.cast(player);
		return true;
	}
}
