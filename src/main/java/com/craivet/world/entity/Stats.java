package com.craivet.world.entity;

/**
 * Estadisticas de las entidades.
 * <p>
 * Los atributos dependen de la raza (que todavia no esta implementado),
 *
 * @author Juan Debenedetti
 */

public class Stats {

    public String name;
    public int lvl, exp, nextLvlExp;
    public int speed, defaultSpeed; // TODO Deberia ser float
    public int hp, maxHp;
    public int mana, maxMana;
    public int ammo;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2;
    public int knockbackValue;

    /**
     * Inicializa las estadisticas del player.
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
     * Incrementa el hp.
     *
     * @param amount cantidad de hp que se va a incrementar.
     */
    public void increaseHp(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp;
    }

    /**
     * Decrementa el hp.
     *
     * @param amount cantidad de hp que se va a decrementar.
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
     * Llena el hp.
     */
    public void fullHp() {
        hp = maxHp;
    }

    /**
     * Llena la mana.
     */
    public void fullMana() {
        mana = maxMana;
    }

    /**
     * Reinicia las estadisticas.
     *
     * @param fullReset true para reiniciar el lvl, exp, nextLvlExp, strenght y dexterity; falso en caso contrario.
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
