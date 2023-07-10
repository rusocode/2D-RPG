package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;
import com.craivet.util.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.util.Global.*;

public class Bat extends Mob {

    public Bat(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Bat";
        type = Type.HOSTILE;
        speed = defaultSpeed = 4;
        hp = maxHp = 7;
        exp = 7;
        attack = 4;
        defense = 1;
        hitbox.x = 3;
        hitbox.y = 15;
        hitbox.width = 42;
        hitbox.height = 21;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        loadMovementImages(entity_bat, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        mobImage = movementDown1;
    }

    public void setAction() {
        timer.timeDirection(this, INTERVAL_DIRECTION);
    }

    public void damageReaction() {
        timer.directionCounter = 0;
    }

    /**
     * Comprueba si dropeo un item.
     */
    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILIDAD_DROP_GOLD) dropItem(this, new Gold(game, world));
    }

}