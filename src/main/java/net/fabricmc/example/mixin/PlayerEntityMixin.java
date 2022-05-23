package net.fabricmc.example.mixin;

import net.fabricmc.example.Exceptions.NotEnoughtManaException;
import net.fabricmc.example.Exceptions.SpellNotFoundException;
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

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private static final int MAX_MANA = 100;
    private int mana;
    private List<Spell> spells;
    private int activeSpell;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    // Spells
    public void setSpell(int pos, Spell spell) {
        if(pos >= 0 && pos <= 7) {
            spells.set(pos, spell);
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot have more than 8 spells. Baise ta grosse mere");
        }
    }

    public Spell getSpell(int pos) {
        return spells.get(pos);
    }

    public Spell getSpell(String name, String type) throws SpellNotFoundException {
        for(Spell spell : spells) {
            if(spell.getName().equals(name) && spell.getType().equals(type)) {
                return spell;
            }
        }
        throw new SpellNotFoundException("(" + name + " - " + type + ") spell not found in the user's spells");
    }

    public List<Spell> getSpells() {
        return spells;
    }

    // Active spells
    public void setActiveSpell(int activeSpell) {
        this.activeSpell = activeSpell;
    }

    public void setActiveSplell(String name, String type) throws SpellNotFoundException {
        activeSpell = spells.indexOf(getSpell(name, type));
    }

    // Mana
    private void setMana(int amount) {
        if(amount < 0) mana = 0;
        else mana = Math.min(amount, 100);
    }

    public void addMana(int amount) {
        setMana(mana+amount);
    }

    public void removeMana(int amount) throws NotEnoughtManaException {
        if(mana-amount < 0) {
            throw new NotEnoughtManaException("Not enought mana", amount, mana);
        }
        setMana(mana-amount);
    }

    public int getMana() {
        return mana;
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("mana", mana);
    }

}
