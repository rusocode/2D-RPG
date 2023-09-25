package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Skeleton extends Mob {

    public Skeleton(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Skeleton";
        soundHit = sound_hit_mob;
        soundDeath = sound_mob_death;
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 50;
        stats.exp = 50;
        stats.attack = 10;
        stats.defense = 2;

        int scale = 5;
        int size = tile * scale;
        hitbox.x = tile;
        hitbox.y = tile;
        hitbox.width = size - tile * 2;
        hitbox.height = size - tile;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 90;
        attackbox.height = 90;
        stats.motion1 = 25;
        stats.motion2 = 50;
        sheet.loadMovementFrames(skeleton_movement, tile, tile, scale);
        sheet.loadAttackFrames(skeleton_attack, tile, tile, scale);
    }

    @Override
    public void doActions() {
        // TODO Hacer que siga al player cuando el mob este trabado en un tile
        // Si la distancia del player con respecto al mob es menor a 10 tiles
        if (getTileDistance(game.world.player) < 10) moveTowardPlayer(30);
        else timer.timeDirection(this, INTERVAL_DIRECTION);
        if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 6, tile * 4, 60);
    }

    @Override
    public void damageReaction() {
        timer.directionCounter = 0;
    }

    @Override
    public void checkDrop() {
        if (Utils.azar(100) <= PROBABILITY_GOLD_DROP) dropItem(this, new Gold(game, world));
    }


}
