package net.fabricmc.minja.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.minja.Minja;
import net.fabricmc.minja.events.PlayerEvent;
import net.fabricmc.minja.exceptions.NotEnoughManaException;
import net.fabricmc.minja.exceptions.SpellNotFoundException;
import net.fabricmc.minja.network.NetworkEvent;
import net.fabricmc.minja.player.PlayerMinja;
import net.fabricmc.minja.objects.MinjaItem;
import net.fabricmc.minja.spells.LightningBall;
import net.fabricmc.minja.spells.SoulSpark;
import net.fabricmc.minja.spells.Spark;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Injection to the class PlayerEntity
 *
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerMinja, PlayerEvent {

    /**
     * MAX_MANA is the maximum mana that the player can use in one time
     */
    private static final int MAX_MANA = 100;

    /**
     * Mana is a fuel for using spells
     */
    private int mana;

    /**
     * Global variable used to control the mana regeneration
     */
    private int tickSecond = 0;

    /**
     * The time between each mana regeneration (in ms)
     */
    private final static int MANA_REGENERATION_TIME = 3000;

    /**
     * The amount of mana added to the player for each mana regeneration
     */
    private final static int MANA_REGENERATION_AMOUNT = 5;

    /**
     * Spells are all the spells that the player can use. It's representing by a wheel
     */
    private final List<Spell> spells = new ArrayList<Spell>(MAX_SPELLS);

    /**
     * Active spell is the current selected spell by the player
     */
    private int activeSpell = 0;

    // Constructor

    /**
     * Injection of the player's spells into the constructor of PlayerEntity. <br><br>
     *
     *
     * @param world
     * @param pos
     * @param yaw
     * @param profile
     * @param info
     */
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo info) {
        for(Spell spell : Minja.SPELLS_MAP.values()) {
            this.addSpell(spell);
        }

        ServerPlayerEntity serverPlayer = null;

        // Get the player server side
        if(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().size() != 0) {
             serverPlayer = (MinecraftClient.getInstance().getServer()
                    .getPlayerManager().getPlayer(
                    MinecraftClient.getInstance().getSession().getUsername()));
        }

        setMana(0);

        // Client synchronization with the server
        if(world.isClient && serverPlayer != null) {
            setMana(((PlayerMinja)serverPlayer).getMana());
            setActiveSpell(((PlayerMinja)serverPlayer).getActiveSpellIndex());
            spells.clear();
            spells.addAll(((PlayerMinja) serverPlayer).getSpells());
        }


    }

    // Spells

    /**
     * Adding a spell in the to the spell wheel
     * @param pos the position of the placement in the wheel
     * @param spell the spell that will be added
     */
    @Override
    public void addSpell(int pos, Spell spell) {
        if(pos >= 0 && pos <= MAX_SPELLS-1) {
            spells.set(pos, spell);
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot have more than " + MAX_SPELLS + " spells");
        }
    }

    /**
     * Adding a spell at the next of the previous positioned spell in the wheel
     * @param spell the spell that will be added
     */
    @Override
    public void addSpell(Spell spell) {
        if(spells.size() < MAX_SPELLS-1) {
            spells.add(spell);
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot have more than " + MAX_SPELLS + " spells");
        }
    }

    /**
     * Get a spell in the wheel
     * @param pos the position of the spell in the wheel
     * @return spell
     */
    @Override
    public Spell getSpell(int pos) {
        return spells.get(pos);
    }

    /**
     * Get a spell by its name and its type
     * @param name the name of the spell eg. LightningBall
     * @param type the type of the spell eg. Basic
     * @return spell
     * @throws SpellNotFoundException when the name and/or the type are incorrect
     */
    @Override
    public Spell getSpell(String name, String type) throws SpellNotFoundException {
        for(Spell spell : spells) {
            if(spell.getName().equals(name) && spell.getType().equals(type)) {
                return spell;
            }
        }
        throw new SpellNotFoundException("(" + name + " - " + type + ") spell not found in the user's spells");
    }

    /**
     * Get all the spells of the player's wheel
     * @return spells
     */
    @Override
    public List<Spell> getSpells() {
        return spells;
    }

    // Active spells

    /**
     * Set active spell the player can directly use
     * @param activeSpell the position of the spell in the wheel
     */
    @Override
    public void setActiveSpell(int activeSpell) {
        this.activeSpell = activeSpell;
    }

    /**
     * Set active spell the player can directly use
     * @param name the name of the spell eg. LightningBall
     * @param type the type of the spell eg. Basic
     * @throws SpellNotFoundException when the name and/or the type are incorrect
     */
    @Override
    public void setActiveSpell(String name, String type) throws SpellNotFoundException {
        activeSpell = spells.indexOf(getSpell(name, type));
    }


    /**
     * Get the current active spell
     *
     * @return the active spell
     */
    public Spell getActiveSpell() {
        return this.spells.get(this.activeSpell);
    }


    /**
     * Get the current active spell index
     *
     * @return the position of the spell in the wheel
     */
    public int getActiveSpellIndex() {
        return this.activeSpell;
    }

    /**
     * Switch two spells index of the spells list
     * @param indexSpell1
     * @param indexSpell2
     */
    public void swapSpells(int indexSpell1, int indexSpell2) {
        Spell spell1 = spells.get(indexSpell1);
        Spell spell2 = spells.get(indexSpell2);
        spells.remove(indexSpell1);
        spells.add(indexSpell1, spell2);
        spells.remove(indexSpell2);
        spells.add(indexSpell2, spell1);
    }


    // Mana

    /**
     * Set the amount of mana the player have
     * @param amount between 0 and 100
     */
    public void setMana(int amount) {
        if(amount < 0) mana = 0;
        else mana = Math.min(amount, MAX_MANA);
    }

    /**
     * Add the amount of mana to the current amount the player has
     * @param amount between 0 and 100
     */
    @Override
    public void addMana(int amount) {
        setMana(mana+amount);
    }

    /**
     * Remove the amount of mana to the current amount the player has
     * @param amount between 0 and 100
     * @throws NotEnoughManaException when currentAmount-removedAmount is under 0
     */
    @Override
    public void removeMana(int amount) throws NotEnoughManaException {
        if(mana-amount < 0) {
            throw new NotEnoughManaException("Not enought mana", amount, mana);
        }
        setMana(mana-amount);
    }

    /**
     * Get the current amount of mana the player has
     * @return mana
     */
    @Override
    public int getMana() {
        return mana;
    }

    /**
     * Get the maximum amount of mana the player can have
     * @return max_mana
     */
    @Override
    public int getManaMax() {
        return MAX_MANA;
    }

    /**
     * Event triggered when the player is swinging its hand (left-clicking) <br>
     *
     * It's a "false" Override since this method already exists in the class PlayerEntity
     * (which is the target of the injection), but we can't directly access it,
     * as the interpreter does not know that we are going to realize an injection.
     * This inheritance is simulated by the PlayerEvent interface allowing the cast. <br><br>
     *
     * In fact, this function is a representation of injection, since we cannot directly inject code into an inherited method.
     *
     * @see LivingEntityMixin#swingHand(Hand, boolean, CallbackInfo) The injection at the level above
     *
     * @param hand Hand holding the item
     * @param fromServerPlayer
     *
     * @return the success of the event
     *
     */
    @Override
    public boolean onSwingItem(Hand hand, boolean fromServerPlayer) {
        PlayerEntity player = (PlayerEntity) (Object) (this);
        Item item = player.getStackInHand(hand).getItem();
        if(item instanceof MinjaItem) {
            return ((MinjaItem)player.getStackInHand(hand).getItem()).interact(player.getWorld(), player, hand, fromServerPlayer);
        }
        return true;

    }


    /**
     * DO NOT USE
     * Write the persistent data of the MinjaPlayer : mana, spells and activeSpell
     * @param nbt
     * @param ci
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("mana", mana);
        int i = 0;
        for(Spell spell : spells) {
            if(spell != null) {
                nbt.putString("spell"+i, spell.getName() + "/" + spell.getType());
                i++;
            }
        }
        nbt.putInt("activeSpell", activeSpell);
    }

    /**
     * DO NOT USE
     * Read the persistent data of the MinjaPlayer : mana, spells and activeSpell
     * @param nbt
     * @param ci
     */
    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        setMana(nbt.getInt("mana"));
        spells.clear();
        for(int i = 0; i < MAX_SPELLS; i++) {
            if(nbt.contains("spell"+i)) {
                addSpell(Minja.SPELLS_MAP.get(
                        nbt.getString("spell"+i)
                ));
            }
        }
        if(nbt.contains("activeSpell")) {
            setActiveSpell(nbt.getInt("activeSpell"));
        }
    }

    /**
     * Tick is triggering every 1/20 second.
     * It allows the implementation of a clock sync with the game.
     * @param ci
     */
    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) (this);
        Item item = player.getStackInHand(Hand.MAIN_HAND).getItem();
        if(item instanceof MinjaItem) {
            ((MinjaItem)player.getStackInHand(Hand.MAIN_HAND).getItem()).tick(player.getWorld());
        }

        tickSecond++;
        // Triggered every 3 seconds
        // Mana regeneration
        if(tickSecond == (20 * (MANA_REGENERATION_TIME / 1000))) {
            tickSecond = 0;
            addMana(MANA_REGENERATION_AMOUNT);
        }
    }

}
