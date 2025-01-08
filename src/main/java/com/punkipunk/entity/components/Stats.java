package com.punkipunk.entity.components;

import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.PlayerData;

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
 * TODO speed deberia ser float
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

    public void increaseHp(int points) {
        hp += points;
        if (hp > maxHp) hp = maxHp;
    }

    public void decreaseHp(int points) {
        hp -= points;
        if (hp < 0) hp = 0;
    }

    public void increaseMaxHp(int points) {
        maxHp += points;
        fullHp();
    }

    public void increaseMana(int points) {
        mana += points;
        if (mana > maxMana) mana = maxMana;
    }

    public void increaseMaxMana(int points) {
        maxMana += points;
        fullMana();
    }

    public void fullHp() {
        hp = maxHp;
    }

    public void fullMana() {
        mana = maxMana;
    }

    public void reset(boolean fullReset) {
        PlayerData playerData = JsonLoader.getInstance().deserialize("player", PlayerData.class);
        hp = maxHp;
        mana = maxMana;
        ammo = 0;
        gold = 0;
        if (fullReset) {
            lvl = playerData.lvl();
            exp = playerData.exp();
            nextExp = playerData.nextExp();
            hp = maxHp = playerData.hp();
            mana = maxMana = playerData.mana();
            ammo = playerData.ammo();
            gold = playerData.gold();
            strength = playerData.strength();
            dexterity = playerData.dexterity();
        }
    }

}
