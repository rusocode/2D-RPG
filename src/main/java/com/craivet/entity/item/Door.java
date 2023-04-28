package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

    public static final String item_name = "Door";

    public Door(Game game, World world) {
        super(game, world);
        name = item_name;
        type = TYPE_OBSTACLE;
        image = Utils.scaleImage(item_door, tile_size, tile_size);
        solid = true;
        hitbox.x = 0;
        hitbox.y = 16;
        hitbox.width = 48;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        initDialogue();
    }

    public Door(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = item_name;
        type = TYPE_OBSTACLE;
        image = Utils.scaleImage(item_door, tile_size, tile_size);
        solid = true;
        hitbox.x = 0;
        hitbox.y = 16;
        hitbox.width = 48;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        initDialogue();
    }

    public void interact() {
        startDialogue(DIALOGUE_STATE, this, 0);
    }

    private void initDialogue() {
        dialogues[0][0] = "You need a key to open this.";
    }

}
