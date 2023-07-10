package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Chest extends Item {

    public static final String NAME = "Chest";

    public Chest(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        name = NAME;
        type = Type.OBSTACLE;
        image = Utils.scaleImage(item_chest_closed, tile_size, tile_size);
        image2 = Utils.scaleImage(item_chest_opened, tile_size, tile_size);
        solid = true;
        hitbox.x = 2;
        hitbox.y = 16;
        hitbox.width = 42;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public void interact() {
        if (!opened) {
            game.playSound(sound_chest_opening);
            image = image2;
            opened = true;
            if (world.player.canPickup(loot)) {
                dialogues[0][0] = "You open the chest and find a " + loot.name + "!\nYou obtain the " + loot.name + "!";
                startDialogue(DIALOGUE_STATE, this, 0);
                empty = true;
            } else {
                dialogues[1][0] = "You open the chest and find a " + loot.name + "!\n...But you cannot carry any more!";
                startDialogue(DIALOGUE_STATE, this, 1);
            }
        } else if (!empty) {
            if (world.player.canPickup(loot)) {
                dialogues[2][0] = "You obtain the " + loot.name + "!";
                startDialogue(DIALOGUE_STATE, this, 2);
                empty = true;
            } else {
                dialogues[3][0] = "You cannot carry any more!";
                startDialogue(DIALOGUE_STATE, this, 3);
            }
        } else {
            dialogues[4][0] = "It's empty.";
            startDialogue(DIALOGUE_STATE, this, 4);
        }
    }

    public void setLoot(Entity loot) {
        this.loot = loot;
    }

}
