package com.craivet.world.entity.projectile;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.gfx.Assets.*;

/**
 * TODO Implementar la disminucion de la velocidad del player cuando la bola pegajosa impacte con el player.
 */

public class StickyBall extends Projectile {

    public StickyBall(Game game, World world) {
        super(game, world);
        stats.name = "Sticky Ball";
        sheet.frame = Utils.scaleImage(sticky_ball, 32, 32);
        stats.speed = 5;
        stats.hp = stats.maxHp = 120;
        stats.attack = 3;
        cost = 1;
        flags.alive = false;
        stats.hitbox.x = 8;
        stats.hitbox.y = 8;
        stats.hitbox.width = 15;
        stats.hitbox.height = 15;
        stats.hitboxDefaultX = stats.hitbox.x;
        stats.hitboxDefaultY = stats.hitbox.y;
    }

    @Override
    public boolean haveResource(Entity entity) {
        return entity.stats.ammo >= cost;
    }

    @Override
    public void subtractResource(Entity entity) {
        entity.stats.ammo -= cost;
    }

    @Override
    public Color getParticleColor() {
        return new Color(106, 193, 127);
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
