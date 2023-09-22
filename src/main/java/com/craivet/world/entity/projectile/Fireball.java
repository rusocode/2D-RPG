package com.craivet.world.entity.projectile;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;

import java.awt.*;

import static com.craivet.gfx.Assets.*;

public class Fireball extends Projectile {

    public Fireball(Game game, World world) {
        super(game, world);
        stats.name = "Fireball";
        stats.speed = 5;
        stats.hp = stats.maxHp = 80;
        stats.attack = 2;
        knockbackValue = 5;
        cost = 1;
        flags.alive = false;
        sheet.loadMovementFrames(fireball, 16, 16, 1);
        sheet.frame = sheet.movement[0];
    }

    @Override
    public boolean haveResource(Entity entity) {
        return entity.stats.mana >= cost;
    }

    @Override
    public void subtractResource(Entity entity) {
        entity.stats.mana -= cost;
    }

    @Override
    public Color getParticleColor() {
        return new Color(240, 50, 0);
    }

    @Override
    public int getParticleSize() {
        return 10;
    }

    @Override
    public int getParticleSpeed() {
        return 1;
    }

    @Override
    public int getParticleMaxLife() {
        return 20;
    }

}
