package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Oldman extends Mob {

    public Oldman(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Oldman";
        type = Type.NPC;
        speed = 1;
        hitbox.x = 6;
        hitbox.y = 16;
        hitbox.width = (tile - hitbox.x) - 4;
        hitbox.height = tile - hitbox.y;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        dialogueSet = -1;
        ss.loadMovementFrames(oldman, 16, 16, 1);
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
        startDialogue(DIALOGUE_STATE, this, dialogueSet);
        dialogueSet++;
        if (dialogues[dialogueSet][dialogueIndex] == null) dialogueSet = 0;
    }

    private void initDialogue() {
        dialogues[0][0] = "Hola forastero!";
        dialogues[0][1] = "Hacia el norte hay una pequenia \nlaguna con una hermosa vista...";

        dialogues[1][0] = "Empiezo a creer que hay algo \nextranio en estos bosques.";
        dialogues[1][1] = "Todo empezo a suceder desde que \nese sujeto raro llego a la isla.";
        dialogues[1][2] = "Voy a seguir explorando, hasta \nluego viajero!";
    }

}
