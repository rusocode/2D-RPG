package com.craivet.world.entity;

import com.craivet.Direction;
import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.projectile.Projectile;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

/**
 * Es importante aclarar que la posicion de una entidad en el World se define por sus coordenadas x-y. Estas coordenadas
 * no son mas que la suma de pixeles a partir de la esquina superior izquierda del mapa (0, 0). Cuando se divide esa
 * suma por el tamaño del tile, se obtiene la posicion en filas y columnas (row y col). Esto se hace para facilitar el
 * manejo de las coordenadas. Otra cosa a tener en cuenta, es que a las coordenadas x'y se le suma la hitbox para
 * obtener la posicion exacta del rectangulo colisionador y no del frame.
 */

public class Stats {

    // Generals attributes
    // TODO Se podria crear en una clase aparte
    public int screenX, screenY;
    public Item weapon, shield, light;
    public Projectile projectile;
    // TODO Se podria crear desde una clase aparte
    public Rectangle hitbox = new Rectangle(0, 0, tile, tile), attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;
    public String name;
    public Type type = Type.HOSTILE;
    public Direction direction = Direction.DOWN;
    public int speed, defaultSpeed;
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
    public Entity loot;
    public String description;
    public int price;
    public int attackValue, defenseValue, knockbackValue;
    public int amount;
    public int lightRadius = 350;
    public boolean solid, stackable;
    public boolean opened, empty;

}
