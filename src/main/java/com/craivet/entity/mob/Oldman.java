package com.craivet.entity.mob;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Type;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Oldman extends Mob {

    public Oldman(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Oldman";
        type = Type.NPC;
        speed = 1;
        hitbox.x = 8;
        hitbox.y = 16;
        hitbox.width = 32;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        dialogueSet = -1;
        loadMovementImages(entity_oldman, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        initDialogue();
    }

    /**
     * Establece la accion especificada.
     */
    public void setAction() {
        if (flags.onPath) searchPath(getGoalRow(world.player), getGoalCol(world.player));
        else timer.timeDirection(this, INTERVAL_DIRECTION);
    }

    public void speak() {
        facePlayer();
        startDialogue(DIALOGUE_STATE, this, dialogueSet);

        dialogueSet++;
        if (dialogues[dialogueSet][dialogueIndex] == null) dialogueSet = 0;
    }

    private void initDialogue() {
        dialogues[0][0] = "Hola forastero!";
        dialogues[0][1] = "Hacia el norte hay una pequenia laguna \ncon una hermosa vista...";

        dialogues[1][0] = "Empiezo a creer que hay algo extranio en \nestos bosques.";
        dialogues[1][1] = "Todo empezo a suceder desde que ese sujeto \nraro llego a la isla.";
        dialogues[1][2] = "Voy a seguir explorando, hasta luego \nviajero!";
    }

}
