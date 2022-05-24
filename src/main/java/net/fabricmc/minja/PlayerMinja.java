package net.fabricmc.minja;

import net.fabricmc.minja.Exceptions.NotEnoughtManaException;
import net.fabricmc.minja.Exceptions.SpellNotFoundException;
import net.fabricmc.minja.spells.Spell;

import java.util.List;

public interface PlayerMinja {

    public void addSpell(int pos, Spell spell);

    public void addSpell(Spell spell);

    public Spell getSpell(int pos);

    public Spell getSpell(String name, String type) throws SpellNotFoundException;

    public List<Spell> getSpells();

    // Active spells
    public void setActiveSpell(int activeSpell);

    public void setActiveSpell(String name, String type) throws SpellNotFoundException;

    // Mana

    public void addMana(int amount);

    public void removeMana(int amount) throws NotEnoughtManaException;

    public int getMana();

    public int getManaMax();

}
