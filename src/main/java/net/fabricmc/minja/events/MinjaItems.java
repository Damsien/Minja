package net.fabricmc.minja.events;

import net.fabricmc.minja.Exceptions.NotEnoughtManaException;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.clocks.Clock;
import net.fabricmc.minja.hud.SpellHUD;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public abstract class MinjaItems extends Item {

    private long lastEvent;
    private boolean firstTime = true;

    private final int TIMER = 3;


    public MinjaItems(Settings settings) {
        super(settings);
    }

    @Override
    //Used when the player use right click with the Wand
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        TypedActionResult state = null;

        //Mettre ici l'ouverture de l'HUD
        lastEvent = world.getTime();

        if(firstTime) {
            firstTime = false;
            state = onRightClickPressed(world, playerEntity, hand);
            Clock clock = new Clock(TIMER, world) {

                @Override
                public void execute() {
                    if(world.getTime() - lastEvent > TIMER) {
                        onRightClickReleased(world, playerEntity, hand);
                        firstTime = true;
                    } else {
                        Thread thread = new Thread(this);
                        thread.start();
                    }
                }
            };
            clock.start();
        }

        else {
            state = onRightClickMaintained(world, playerEntity, hand);
        }

        if(state == null) state = TypedActionResult.success(playerEntity.getStackInHand(hand));

        return state;
    }

    public abstract TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand);

    public abstract TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand);

    public abstract TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand);

}
