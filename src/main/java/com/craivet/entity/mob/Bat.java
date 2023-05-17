package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.item.Gold;
import com.craivet.utils.Utils;

import static com.craivet.gfx.Assets.entity_bat;
import static com.craivet.utils.Global.*;

public class Bat extends Mob {

    public Bat(Game game, World world, int x, int y) {
        super(game, world, x, y);
        initDefaultValues();
    }

    private void initDefaultValues() {
        name = "Bat";
        type = TYPE_MOB;
        speed = defaultSpeed = 4;
        HP = maxHP = 7;
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
        if (Utils.azar(100) <= PROBABILIDAD_DROP_ORO) dropItem(this, new Gold(game, world));
    }

}