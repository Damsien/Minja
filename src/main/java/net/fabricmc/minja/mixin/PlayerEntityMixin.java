package net.fabricmc.minja.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.minja.Minja;
import net.fabricmc.minja.clocks.ManaClock;
import net.fabricmc.minja.events.PlayerEvent;
import net.fabricmc.minja.exceptions.NotEnoughtManaException;
import net.fabricmc.minja.exceptions.SpellNotFoundException;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.objects.MinjaItem;
import net.fabricmc.minja.spells.LightningBall;
import net.fabricmc.minja.spells.Spark;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerMinja, PlayerEvent {

    /**
     * MAX_MANA is the maximum mana that the player can use in one time
     */
    private static final int MAX_MANA = 100;

    /**
     * MAX_SPELLS is the maximum spells that the player can use
     */
    private static final int MAX_SPELLS = 8;

    /**
     * Mana is a fuel for using spells
     */
    private int mana;

    /**
     * Spells are all the spells that the player can use. It's representing by a wheel
     */
    private final List<Spell> spells = new ArrayList<Spell>(MAX_SPELLS);

    /**
     * Active spell is the current selected spell by the player
     */
    private int activeSpell = 0;

    private ManaClock manaClock;

    // Constructor

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo info) {
        this.addSpell(new LightningBall());
        this.addSpell(new Spark());
        System.out.println("=============");
        if(world.isClient) {
            System.out.println(MinecraftClient.getInstance().getSession().getUsername());
            setMana(
                    ((PlayerMinja)MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(
                            MinecraftClient.getInstance().getSession().getUsername())).getMana()
            );
        } else {
            setMana(0);
        }
        manaClock = new ManaClock(1500, this);
        runManaRegeneration();
        System.out.println("init " + this.getClass());
        System.out.println("=============");
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


    public Spell getActiveSpell() {
        return this.spells.get(this.activeSpell);
    }


    public int getActiveSpellIndex() {
        return this.activeSpell;
    }


    // Mana

    /**
     * Set the amount of mana the player have
     * @param amount between 0 and 100
     */
    private void setMana(int amount) {
        System.out.println("Instance : " + this.getClass());
        System.out.println("Set mana : " + mana);
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
     * @throws NotEnoughtManaException when currentAmount-removedAmount < 0
     */
    @Override
    public void removeMana(int amount) throws NotEnoughtManaException {
        if(mana-amount < 0) {
            throw new NotEnoughtManaException("Not enought mana", amount, mana);
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

    @Override
    public boolean onSwingItem(Hand hand, boolean fromServerPlayer) {
        PlayerEntity player = (PlayerEntity) (Object) (this);
        Item item = player.getStackInHand(hand).getItem();
        if(item instanceof MinjaItem) {
            return ((MinjaItem)player.getStackInHand(hand).getItem()).interact(player.getWorld(), player, hand, fromServerPlayer);
        }
        return true;

    }

    private void runManaRegeneration() {
        manaClock.start();
    }

    private void stopManaRegeneration() {
        manaClock.stop();
    }

    /**
     * DO NOT USE
     * Write the persistent data of the MinjaPlayer : mana, spells and activeSpell
     * @param nbt
     * @param ci
     */
    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("mana", mana);
        System.out.println("Instance : " + this.getClass());
        System.out.println("Write mana : " + mana);
        int i = 0;
        for(Spell spell : spells) {
            if(spell != null) {
                nbt.putString("spell"+i, spell + "/" + spell.getType() + "/" + i);
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
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        setMana(nbt.getInt("mana"));
        System.out.println("Instance : " + this.getClass());
        System.out.println("Read mana " + mana);
        for(int i = 0; i < MAX_SPELLS; i++) {
            if(nbt.contains("spell"+i)) {
                spells.add(Minja.SPELLS_MAP.get(
                        nbt.getString("spell"+i)+"/"+nbt.getString("spell"+i))
                );
            }
        }
        if(nbt.contains("activeSpell")) {
            activeSpell = nbt.getInt("activeSpell");
        }
    }

}
