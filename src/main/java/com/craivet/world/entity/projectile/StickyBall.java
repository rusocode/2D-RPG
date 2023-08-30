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
        name = "Sticky Ball";
        image = Utils.scaleImage(sticky_ball, 32, 32);
        speed = 5;
        hp = maxHp = 120;
        attack = 3;
        cost = 1;
        flags.alive = false;
        hitbox.x = 8;
        hitbox.y = 8;
        hitbox.width = 15;
        hitbox.height = 15;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    @Override
    public boolean haveResource(Entity entity) {
        return entity.ammo >= cost;
    }

    @Override
    public void subtractResource(Entity entity) {
        entity.ammo -= cost;
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
