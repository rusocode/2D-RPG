package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.mob.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

    public static final String NAME = "Door";

    public Door(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.OBSTACLE;
        image = Utils.scaleImage(door, tile_size, tile_size);
        solid = true;
        hitbox.x = 0;
        hitbox.y = 16;
        hitbox.width = 47;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        initDialogue();
    }

    @Override
    public void interact() {
        startDialogue(DIALOGUE_STATE, this, 0);
    }

    private void initDialogue() {
        dialogues[0][0] = "You need a key to open this.";
    }

}
