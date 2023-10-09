package com.craivet.world.entity;

/**
 * Stats of the entities.
 * <p>
 * Attributes depend on race (which is not yet implemented).
 *
 * @author Juan Debenedetti
 */

public class Stats {

    public String name;
    public int lvl, exp, nextLvlExp;
    public int speed, defaultSpeed; // TODO It should be float
    public int hp, maxHp;
    public int mana, maxMana;
    public int ammo;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2;
    public int knockbackValue;

    /**
     * Initializes the player stats.
     */
    public void init() {
        lvl = 1;
        exp = 0;
        nextLvlExp = 10;
        speed = defaultSpeed = 2;
        hp = maxHp = 6;
        mana = maxMana = 4;
        ammo = 5;
        gold = 0;
        strength = 1;
        dexterity = 1;
        motion1 = 5;
        motion2 = 18;
    }

    /**
     * Increase hp.
     *
     * @param amount amount of hp to increase.
     */
    public void increaseHp(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }

    /**
     * Decreases hp.
     *
     * @param amount amount of hp to be decreased.
     */
    public void decreaseHp(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
    }

    public void increaseMaxHp(int amount) {
        maxHp += amount;
    }

    public void increaseMaxMana(int amount) {
        maxMana += amount;
    }

    /**
     * Fills the hp.
     */
    public void fullHp() {
        hp = maxHp;
    }

    /**
     * Fills the mana.
     */
    public void fullMana() {
        mana = maxMana;
    }

    /**
     * Reset the stats.
     *
     * @param fullReset true to reset the lvl, exp, nextLvlExp, strength and dexterity; false otherwise.
     */
    public void reset(boolean fullReset) {
        hp = maxHp = 6;
        mana = maxMana = 4;
        ammo = 5;
        gold = 0;
        if (fullReset) {
            lvl = 1;
            exp = 0;
            nextLvlExp = 10;
            strength = 1;
            dexterity = 1;
        }
    }

}
