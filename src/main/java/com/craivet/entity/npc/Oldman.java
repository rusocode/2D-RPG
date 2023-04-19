package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.World;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Oldman extends Npc {

    public Oldman(Game game, World world, int x, int y) {
        super(game, world);
        this.x = x * tile_size;
        this.y = y * tile_size;
        initDefaultValues();
    }

    private void initDefaultValues() {
        type = TYPE_NPC;
        name = "Oldman";
        speed = 1;

        hitbox.x = 8;
        hitbox.y = 16;
        hitbox.width = 32;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        loadMovementImages(entity_oldman, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
        initDialogue();
    }

    /**
     * Establece la accion especificada.
     */
    public void setAction() {
        if (onPath) searchPath(getGoalRow(world.player), getGoalCol(world.player));
        else timer.timeDirection(this, INTERVAL_DIRECTION);
    }

    public void speak() {
        super.speak();
        onPath = true;
    }

    private void initDialogue() {
        dialogues[0] = "Hola quemado!";
        dialogues[1] = "Creo que arriba del bosque hay \nunas plantas de rulo...";
        dialogues[2] = "Ahora el esta re quemado y no \nreacciona, asi que podes aprovechar \ny cortarlas";
        dialogues[3] = "Ojo que pegan fuerte";
    }

}
