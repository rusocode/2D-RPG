package com.craivet.entity;

import com.craivet.entity.projectile.Projectile;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

/**
 * Tiene pinta de que esta clase se va a convertir en la clase Entity, y cada funcion y atributo de la clase Entity
 * va a volar a sus respectivas clases.
 */

public class Attributes {

    // Generales attributes
    public Entity weapon, shield, light;
    public Projectile projectile;
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48), attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;
    public int x, y;
    public int width, height;
    public String name;
    public int type = TYPE_MOB;
    public BufferedImage image, image2, mobImage; // Imagenes estaticas para los items y mobs
    public int direction = DOWN;
    public int speed, defaultSpeed;
    public int HP, maxHP; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
    public int mana, maxMana;
    public int ammo;
    public int lvl, exp, nextLvlExp;
    public int gold;
    public int strength, dexterity;
    public int attack, defense;
    public int motion1, motion2; // Velocidad de movimiento de la espada

    // Item attributes
    public Entity loot;
    public String description;
    public int price;
    public int attackValue, defenseValue, knockbackValue;
    public int amount;
    public int lightRadius = 350;
    public boolean solid, stackable;
    public boolean opened, empty; // chest

    /**
     * Obtiene la posicion superior de la hitbox.
     *
     * @return la posicion superior de la hitbox.
     */
    public int getTopHitbox() {
        return y + hitbox.y;
    }

    /**
     * Obtiene la posicion inferior de la hitbox.
     *
     * @return la posicion inferior de la hitbox.
     */
    public int getBottomHitbox() {
        return y + hitbox.y + hitbox.height;
    }

    /**
     * Obtiene la posicion izquierda de la hitbox.
     *
     * @return la posicion izquierda de la hitbox.
     */
    public int getLeftHitbox() {
        return x + hitbox.x;
    }

    /**
     * Obtiene la posicion derecha de la hitbox.
     *
     * @return la posicion derecha de la hitbox.
     */
    public int getRightHitbox() {
        return x + hitbox.x + hitbox.width;
    }

    /**
     * Obtiene la fila de la entidad.
     *
     * @return la fila de la entiad.
     */
    public int getRow() {
        return (y + hitbox.y) / tile_size;
    }

    /**
     * Obtiene la columna de la entidad.
     *
     * @return la columna de la entiad.
     */
    public int getCol() {
        return (x + hitbox.x) / tile_size;
    }

}
