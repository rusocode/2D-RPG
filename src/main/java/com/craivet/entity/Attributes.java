package com.craivet.entity;

import com.craivet.entity.projectile.Projectile;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.craivet.utils.Global.*;

public class Attributes {

    // Generales
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
    public Rectangle hitbox = new Rectangle(0, 0, 48, 48), attackbox = new Rectangle(0, 0, 0, 0);
    public int hitboxDefaultX, hitboxDefaultY;
    public Projectile projectile;
    public Entity weapon, shield;
    public Entity light;

    // Item attributes
    public Entity loot;
    public String description;
    public int price;
    public int attackValue, defenseValue, knockbackValue;
    public int amount = 1;
    public int lightRadius = 350;
    public boolean solid, stackable;
    public boolean opened, empty; // chest

}
