package com.craivet.world.entity;

import com.craivet.Direction;

/**
 * Estadisticas de los mobs y del player.
 *
 * @author Juan Debenedetti
 */

public class Stats {

    public String name;
    public int speed, defaultSpeed; // TODO Deberia ser float
    public int hp, maxHp;
    public int mana, maxMana;
    public int ammo;
    public int lvl, exp, nextLvlExp = 5;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2;

    /**
     * Reinicia las estadisticas.
     *
     * @param fullReset true para reiniciar el lvl, exp, gold, strenght y dexterity; falso en caso contrario.
     */
    public void reset(boolean fullReset) {
        hp = maxHp;
        mana = maxMana;
        if (fullReset) {
            lvl = 1;
            exp = 0;
            gold = 500;
            strength = 1;
            dexterity = 1;
        }
    }

}
