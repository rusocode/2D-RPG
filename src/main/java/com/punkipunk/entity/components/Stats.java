package com.punkipunk.entity.components;

/**
 * Estadisticas de las entidades.
 * <p>
 * La variable {@code baseSpeed} cumple una funcion importante en el manejo de la velocidad de las entidades, especialmente en
 * relacion con el efecto de "knockback" (retroceso). Veamos por que:
 * <ol>
 * <li>{@code baseSpeed} actua como la velocidad base o normal de una entidad. Se usa como punto de referencia al cual volver
 * despues de efectos temporales que modifican la velocidad.
 * <li>Por otro lado, {@code speed} es la velocidad actual de la entidad, que puede variar temporalmente debido a diferentes
 * efectos del juego.
 * </ol>
 * La separacion entre speed y baseSpeed es necesaria porque:
 * <ol>
 * <li>Permite modificar temporalmente la velocidad de una entidad sin perder la referencia a su velocidad base
 * <li>Facilita restaurar la velocidad normal despues de efectos temporales
 * <li>Mantiene la consistencia del movimiento en el juego
 * </ol>
 * Si solo se usara speed, seria mas dificil:
 * <ul>
 * <li>Recordar cual era la velocidad original de la entidad
 * <li>Restaurar la velocidad correcta despues de efectos temporales
 * <li>Manejar multiples efectos que afectan la velocidad
 * </ul>
 * Por lo tanto, aunque parezca redundante, mantener ambas variables es una buena practica de diseño que facilita el manejo de 
 * estados temporales en el juego.
 * <p>
 * TODO speed deberia ser flotante
 */

public class Stats {

    public String name;
    public int lvl, exp, nextExp;
    public int speed;
    public int baseSpeed;
    public int hp, maxHp;
    public int mana, maxMana;
    public int ammo;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2;
    public int knockback;

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
        hp = maxHp;
        mana = maxMana;
        ammo = 5;
        gold = 0;
        if (fullReset) {
            lvl = 1;
            exp = 0;
            nextExp = 10;
            hp = maxHp = 6;
            mana = maxMana = 15;
            strength = 1;
            dexterity = 1;
        }
    }

}
