package com.craivet.world.entity;

import com.craivet.world.entity.item.Item;
import com.craivet.world.entity.projectile.Projectile;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.util.Global.*;

/**
 * Tiene pinta de que esta clase se va a convertir en la clase Entity, y cada funcion y atributo de la clase Entity
 * va a volar a sus respectivas clases.
 * <p>
 * Es importante aclarar que la posicion de una entidad en el World se define por sus coordenadas x/y. Estas coordenadas
 * no son mas que la suma de pixeles a partir de la esquina superior izquierda del mapa (0, 0). Cuando se divide esa
 * suma por el tamaño del tile, se obtiene la posicion en filas y columnas (row y col). Esto se hace para facilitar el
 * manejo de las coordenadas. Otra cosa a tener en cuenta, es que a las coordenadas x/y se le suma la hitbox para
 * obtener la posicion exacta del rectangulo colisionador y no de la imagen.
 */

public class Attributes {

    // Generals attributes
    public Item weapon, shield, light;
    public Projectile projectile;
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48), attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;
    public int x, y;
    public int width, height;
    public String name;
    public Type type = Type.HOSTILE;
    public BufferedImage image, image2, mobImage; // Imagenes estaticas para los items y mobs
    public int direction = DOWN;
    public int speed, defaultSpeed;
    public int hp, maxHp; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
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

}
