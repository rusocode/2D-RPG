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
        dialogue.dialogues[0][0] = "Hola forastero!";
        dialogue.dialogues[0][1] = "Hacia el norte hay una pequenia \nlaguna con una hermosa vista...";

        dialogue.dialogues[1][0] = "Empiezo a creer que hay algo \nextranio en estos bosques.";
        dialogue.dialogues[1][1] = "Todo empezo a suceder desde que \nese sujeto raro llego a la isla.";
        dialogue.dialogues[1][2] = "Voy a seguir explorando, hasta \nluego viajero!";
    }

}
