package com.craivet.world.entity.projectile;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

/**
 * TODO Implementar la disminucion en la velocidad del player cuando la bola pegajosa impacte con el.
 */

public class StickyBall extends Projectile {

    public StickyBall(Game game, World world) {
        super(game, world);
        stats.name = "Sticky Ball";
        stats.speed = 5;
        stats.hp = stats.maxHp = 120;
        stats.attack = 3;
        flags.alive = false;
        cost = 1;
        hitbox = new Rectangle(8, 8, 15, 15);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        sheet.frame = Utils.scaleImage(stickyball, tile, tile);
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
