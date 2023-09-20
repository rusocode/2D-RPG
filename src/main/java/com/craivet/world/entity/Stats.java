package com.craivet.world.entity;

import com.craivet.Direction;

/**
 * Estadisticas de los mobs y del player.
 * <p>
 * TODO Se podria nombrar a CharacterStats
 */

public class Stats {

    public String name;
    public int speed, defaultSpeed; // TODO Deberia ser float
    public int hp, maxHp;
    public int mana, maxMana;
    public int ammo;
    public int lvl, exp, nextLvlExp;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2;
    public Type type = Type.HOSTILE;
    public Direction direction = Direction.DOWN;

}
