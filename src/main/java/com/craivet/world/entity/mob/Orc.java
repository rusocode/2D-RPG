package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;
import com.craivet.util.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.util.Global.*;

public class Orc extends Mob {

    public Orc(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Orc";
        type = Type.HOSTILE;
        speed = defaultSpeed = 1;
        hp = maxHp = 10;
        exp = 2;
        attack = 8;
        defense = 2;
        hitbox.x = 4;
        hitbox.y = 4;
        hitbox.width = 40;
        hitbox.height = 44;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 44;
        attackbox.height = 48;
        motion1 = 15;
        motion2 = 30;
        loadMovementImages(entity_orc_movement, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        loadWeaponImages(entity_orc_attack, ENTITY_WIDTH, ENTITY_HEIGHT);
        mobImage = movementDown1;
    }

    public void setAction() {
        if (flags.onPath) {
            checkUnfollow(world.player, 15);
            searchPath(getGoalRow(world.player), getGoalCol(world.player));
        } else {
            checkFollow(world.player, 5, 100);
            timer.timeDirection(this, INTERVAL_DIRECTION);
        }

        if (!flags.hitting) checkAttackOrNot(30, tile_size * 2, tile_size);

    }

    public void damageReaction() {
        timer.directionCounter = 0;
        flags.onPath = true;
    }

    /**
     * Comprueba si dropeo un item.
     */
    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILIDAD_DROP_GOLD) dropItem(this, new Gold(game, world));
    }

}