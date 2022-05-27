package net.fabricmc.minja;

import net.fabricmc.minja.Exceptions.NotEnoughtManaException;
import net.fabricmc.minja.Exceptions.SpellNotFoundException;
import net.fabricmc.minja.spells.Spell;

import java.util.List;

public interface PlayerMinja {

    // Spells

    /**
     * Adding a spell in the to the spell wheel
     * @param pos the position of the placement in the wheel
     * @param spell the spell that will be added
     */
    public void addSpell(int pos, Spell spell);

    /**
     * Adding a spell at the next of the previous positioned spell in the wheel
     * @param spell the spell that will be added
     */
    public void addSpell(Spell spell);

    /**
     * Get a spell in the wheel
     * @param pos the position of the spell in the wheel
     * @return spell
     */
    public Spell getSpell(int pos);

    /**
     * Get a spell by its name and its type
     * @param name the name of the spell eg. LightningBall
     * @param type the type of the spell eg. Basic
     * @return spell
     * @throws SpellNotFoundException when the name and/or the type are incorrect
     */
    public Spell getSpell(String name, String type) throws SpellNotFoundException;

    /**
     * Get all the spells of the player's wheel
     * @return spells
     */
    public List<Spell> getSpells();

    // Active spells

    /**
     * Set active spell the player can directly use
     * @param activeSpell the position of the spell in the wheel
     */
    public void setActiveSpell(int activeSpell);

    /**
     * Set active spell the player can directly use
     * @param name the name of the spell eg. LightningBall
     * @param type the type of the spell eg. Basic
     * @throws SpellNotFoundException when the name and/or the type are incorrect
     */
    public void setActiveSpell(String name, String type) throws SpellNotFoundException;

    public Spell getActiveSpell();

    public int getActiveSpellIndex();

    // Mana

    /**
     * Add the amount of mana to the current amount the player has
     * @param amount between 0 and 100
     */
    public void addMana(int amount);

    /**
     * Remove the amount of mana to the current amount the player has
     * @param amount between 0 and 100
     * @throws NotEnoughtManaException when currentAmount-removedAmount < 0
     */
    public void removeMana(int amount) throws NotEnoughtManaException;

    /**
     * Get the current amount of mana the player has
     * @return mana
     */
    public int getMana();

    /**
     * Get the maximum amount of mana the player can have
     * @return max_mana
     */
    public int getManaMax();

}
