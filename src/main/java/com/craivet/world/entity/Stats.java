package com.craivet.world.entity;

import com.craivet.Direction;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.projectile.Projectile;

/**
 * Estadisticas generales.
 */

public class Stats {

    public Item weapon, shield, light;
    public Projectile projectile;
    public String name;
    public Type type = Type.HOSTILE;
    public Direction direction = Direction.DOWN;
    public int speed, defaultSpeed; // TODO Deberia ser float
    public int hp, maxHp;
    public int mana, maxMana;
    public int ammo;
    public int lvl, exp, nextLvlExp;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2; // Velocidad de movimiento del arma

    // TODO Se podria crear una clase aparte
    // Item attributes
    public Item loot;
    public String description;
    public int price;
    public int attackValue, defenseValue, knockbackValue;
    public int amount;
    public int lightRadius = 350;
    public boolean solid, stackable;
    public boolean opened, empty;

}
