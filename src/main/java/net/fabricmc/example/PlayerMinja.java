package net.fabricmc.example;

import net.fabricmc.example.Exceptions.NotEnoughtManaException;
import net.fabricmc.example.Exceptions.SpellNotFoundException;

import java.util.List;

public interface PlayerMinja {

    public void setSpell(int pos, Spell spell);

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
