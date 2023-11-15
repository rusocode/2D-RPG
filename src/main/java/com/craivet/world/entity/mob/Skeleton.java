package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.io.Progress;
import com.craivet.utils.Utils;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.DoorIron;
import com.craivet.world.entity.item.Gold;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Skeleton extends Mob {

    public static final String NAME = "Skeleton";

    public Skeleton(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue(game);
        type = Type.HOSTILE;
        stats.name = NAME;
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 5;
        stats.exp = 50;
        stats.attack = 10;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 50;
        soundHit = sound_hit_mob;
        soundDeath = sound_mob_death;
        boss = true;
        sleep = true;
        int scale = 5;
        int size = tile * scale;
        hitbox = new Rectangle(tile, tile, size - tile * 2, size - tile);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 90;
        attackbox.height = 90;
        sheet.loadMovementFrames(skeleton_movement, tile, tile, scale);
        sheet.loadAttackFrames(skeleton_attack, tile, tile, scale);

        initDialogue();
    }

    @Override
    public void doActions() {
        // TODO Make it follow the player when the Skeleton is stuck on a tile
        // If the distance of the player with respect to the mob is less than 10 tiles
        if (getTileDistance(game.world.player) < 10) moveTowardPlayer(game.world.player, 30);
        else timer.timeDirection(this, INTERVAL_DIRECTION);
        if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 6, tile * 4, 60);
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "No one can steal my treasure.";
        dialogue.dialogues[0][1] = "You will die here.";
        dialogue.dialogues[0][2] = "WELCOME TO YOUR DOOM!";
    }

    @Override
    public void damageReaction() {
        // timer.directionCounter = 0;
    }

    @Override
    public void checkDrop() {

        world.bossBattleOn = false;
        Progress.skeletonDefeated = true;

        // Remove the iron doors
        for (int i = 0; i < world.items[1].length; i++)
            if (world.items[world.map][i] != null && world.items[world.map][i].stats.name.equals(DoorIron.NAME))
                world.items[world.map][i] = null;

    }

}
