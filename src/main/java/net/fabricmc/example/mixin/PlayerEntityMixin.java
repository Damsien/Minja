package net.fabricmc.example.mixin;

import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.Exceptions.NotEnoughtManaException;
import net.fabricmc.example.Exceptions.SpellNotFoundException;
import net.fabricmc.example.PlayerMinja;
import net.fabricmc.example.Spell;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerMinja {

    private static final int MAX_MANA = 100;
    private int mana = 0;
    private final List<Spell> spells = new ArrayList<Spell>();
    private int activeSpell = 0;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    // Spells
    @Override
    public void setSpell(int pos, Spell spell) {
        if(pos >= 0 && pos <= 7) {
            spells.set(pos, spell);
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot have more than 8 spells. Baise ta grosse mere");
        }
    }

    @Override
    public Spell getSpell(int pos) {
        return spells.get(pos);
    }

    @Override
    public Spell getSpell(String name, String type) throws SpellNotFoundException {
        for(Spell spell : spells) {
            if(spell.getName().equals(name) && spell.getType().equals(type)) {
                return spell;
            }
        }
        throw new SpellNotFoundException("(" + name + " - " + type + ") spell not found in the user's spells");
    }

    @Override
    public List<Spell> getSpells() {
        return spells;
    }

    // Active spells
    @Override
    public void setActiveSpell(int activeSpell) {
        this.activeSpell = activeSpell;
    }

    @Override
    public void setActiveSpell(String name, String type) throws SpellNotFoundException {
        activeSpell = spells.indexOf(getSpell(name, type));
    }

    // Mana
    private void setMana(int amount) {
        if(amount < 0) mana = 0;
        else mana = Math.min(amount, 100);
    }

    @Override
    public void addMana(int amount) {
        setMana(mana+amount);
    }

    @Override
    public void removeMana(int amount) throws NotEnoughtManaException {
        if(mana-amount < 0) {
            throw new NotEnoughtManaException("Not enought mana", amount, mana);
        }
        setMana(mana-amount);
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public int getManaMax() {
        return MAX_MANA;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("mana", mana);
        for(int i = 0; i < 8; i++) {
            nbt.putString("spell"+i, spells.get(i).getName() + "/" + spells.get(i).getType() + "/" + i);
        }
        nbt.putInt("activeSpell", activeSpell);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        mana = nbt.getInt("mana");
        for(int i = 0; i < 8; i++) {
            spells.add(ExampleMod.SPELLS_MAP.get(
                    nbt.getString("spell"+i)+"/"+nbt.getString("spell"+i))
            );
        }
        activeSpell = nbt.getInt("activeSpell");
    }

}
