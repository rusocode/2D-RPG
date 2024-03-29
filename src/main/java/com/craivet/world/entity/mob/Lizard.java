package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.io.Progress;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.DoorIron;
import com.craivet.world.entity.item.Gold;

import java.awt.*;

import static com.craivet.gfx.Assets.*;
import static com.craivet.utils.Global.*;

public class Lizard extends Mob {

    public static final String NAME = "Lizard";

    public Lizard(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue(game);
        type = Type.HOSTILE;
        stats.name = NAME;
        stats.speed = stats.defaultSpeed = 1;
        stats.hp = stats.maxHp = 50;
        stats.exp = 50;
        stats.attack = 10;
        stats.defense = 2;
        stats.motion1 = 25;
        stats.motion2 = 50;
        soundHit = sound_mob_hit;
        soundDeath = sound_mob_death;
        flags.boss = true;
        sleep = true;
        int scale = 1;
        int size = tile * scale;
        hitbox = new Rectangle(tile, tile * 2, 170 - tile * 2, 180 - tile * 3);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        attackbox.width = 90;
        attackbox.height = 90;
        sheet.loadMovementFrames(lizard, 170, 180, scale);

        initDialogue();
    }

    @Override
    public void doActions() {
        // TODO Make it follow the player when the Skeleton is stuck on a tile
        // If the distance of the player with respect to the mob is less than 10 tiles
        if (getTileDistance(game.world.entities.player) < 10) moveTowardPlayer(game.world.entities.player, 30);
        else timer.timeDirection(this, INTERVAL_DIRECTION);
        // if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 6, tile * 4, 60); // TODO No se utiliza ya que por ahora el boss no tiene un sprite de ataque
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

        drop(this, new Gold(game, world, 1000));

        Progress.bossDefeated = true;

        // Remove the iron doors
        for (int i = 0; i < world.entities.items[1].length; i++)
            if (world.entities.items[world.map.num][i] != null && world.entities.items[world.map.num][i].stats.name.equals(DoorIron.NAME))
                world.entities.items[world.map.num][i] = null;

    }

}
