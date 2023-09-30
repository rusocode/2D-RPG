package com.craivet.world.entity.item;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Door extends Item {

    public static final String NAME = "Door";

    public Door(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        dialogue = new Dialogue();
        type = Type.OBSTACLE;
        stats.name = NAME;
        solid = true;
        hitbox = new Rectangle(0, 16, tile, tile - 16);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        sheet.frame = Utils.scaleImage(door, tile, tile);
        initDialogue();
    }

    @Override
    public void interact() {
        startDialogue(DIALOGUE_STATE, this, 0);
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "You need a key to open this.";
    }

}
