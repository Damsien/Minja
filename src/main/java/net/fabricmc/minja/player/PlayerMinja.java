package net.fabricmc.minja.player;

import net.fabricmc.minja.clocks.ManaClock;
import net.fabricmc.minja.exceptions.NotEnoughManaException;
import net.fabricmc.minja.exceptions.SpellNotFoundException;
import net.fabricmc.minja.spells.Spell;

import java.util.List;

public interface PlayerMinja {

    /**
     * MAX_SPELLS is the maximum spells that the player can use
     */
    public static final int MAX_SPELLS = 8;

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

    public void swapSpells(int indexSpell1, int indexSpell2);

    // Mana

    /**
     * Add the amount of mana to the current amount the player has
     * @param amount between 0 and 100
     */
    public void addMana(int amount);

    /**
     * Set the amount of mana the player have
     * @param amount between 0 and 100
     */
    public void setMana(int amount);

    /**
     * Remove the amount of mana to the current amount the player has
     * @param amount between 0 and 100
     * @throws NotEnoughManaException when currentAmount-removedAmount < 0
     */
    public void removeMana(int amount) throws NotEnoughManaException;

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

    /**
     * Stop the player's mana regeneration
    */
    public void stopManaRegeneration();

    /**
     * Start the player's mana regeneration
     */
    public void runManaRegeneration();

}
