package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.Gold;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Skeleton extends Mob {

    public Skeleton(Game game, World world, int col, int row) {
        super(game, world, col, row);
        type = Type.HOSTILE;
        stats.name = "Skeleton";
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 50;
        stats.exp = 50;
        stats.attack = 10;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 50;
        soundHit = sound_hit_mob;
        soundDeath = sound_mob_death;
        boss = true;
        int scale = 5;
        int size = tile * scale;
        hitbox = new Rectangle(tile, tile, size - tile * 2, size - tile);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 90;
        attackbox.height = 90;
        sheet.loadMovementFrames(skeleton_movement, tile, tile, scale);
        sheet.loadAttackFrames(skeleton_attack, tile, tile, scale);
    }

    @Override
    public void doActions() {
        // TODO Hacer que siga al player cuando el mob este trabado en un tile
        // Si la distancia del player con respecto al mob es menor a 10 tiles
        if (getTileDistance(game.world.player) < 10) moveTowardPlayer(game.world.player, 30);
        else timer.timeDirection(this, INTERVAL_DIRECTION);
        if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 6, tile * 4, 60);
    }

    @Override
    public void damageReaction() {
        timer.directionCounter = 0;
    }

    @Override
    public void checkDrop() {
        if (Utils.random(100) <= PROBABILITY_GOLD_DROP) drop(this, new Gold(game, world));
    }


}
