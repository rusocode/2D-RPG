package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Oldman extends Mob {

    public Oldman(Game game, World world, int col, int row) {
        super(game, world, col, row);
        dialogue = new Dialogue();
        type = Type.NPC;
        stats.name = "Oldman";
        stats.speed = 1;
        hitbox = new Rectangle(6, 16, tile - 10, tile - 16);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        dialogue.set = -1;
        sheet.loadMovementFrames(oldman, 16, 16, 1);
        initDialogue();
    }

    @Override
    public void doActions() {
        if (flags.following) game.aStar.searchPath(this, getGoalRow(world.player), getGoalCol(world.player));
        else timer.timeDirection(this, INTERVAL_DIRECTION);
    }

    @Override
    public void dialogue() {
        lookPlayer();
        startDialogue(DIALOGUE_STATE, this, dialogue.set);
        dialogue.set++;
        if (dialogue.dialogues[dialogue.set][dialogue.index] == null) dialogue.set = 0;
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "Hello stranger!";
        dialogue.dialogues[0][1] = "To the north there is a small \nlagoon with a beautiful view...";

        dialogue.dialogues[1][0] = "I'm starting to believe that there \nis something stranger in these \nforests.";
        dialogue.dialogues[1][1] = "Everything started happening since \nthat strange guy arrived on the \nisland.";
        dialogue.dialogues[1][2] = "I will continue exploring, until \nthen traveler!";
    }

}
