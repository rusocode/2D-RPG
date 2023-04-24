package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Chest extends Item {

    public Chest(Game game, World world) {
        super(game, world);
        name = "Chest";
        type = TYPE_OBSTACLE;
        image = Utils.scaleImage(item_chest_closed, tile_size, tile_size);
        image2 = Utils.scaleImage(item_chest_opened, tile_size, tile_size);
        solid = true;
        hitbox.x = 4;
        hitbox.y = 16;
        hitbox.width = 40;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public Chest(Game game, World world, int x, int y) {
        super(game, world);
        this.x = x * tile_size;
        this.y = y * tile_size;
        name = "Chest";
        type = TYPE_OBSTACLE;
        image = Utils.scaleImage(item_chest_closed, tile_size, tile_size);
        image2 = Utils.scaleImage(item_chest_opened, tile_size, tile_size);
        solid = true;
        hitbox.x = 4;
        hitbox.y = 16;
        hitbox.width = 40;
        hitbox.height = 32;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
    }

    public void interact() {
        if (!opened) {
            game.playSound(sound_chest_opening);
            image = image2;
            opened = true;
            if (world.player.canObtainItem(loot)) {
                startDialogue(this, 0);
                empty = true;
            } else startDialogue(this, 1);
        } else if (!empty) {
            if (world.player.canObtainItem(loot)) {
                startDialogue(this, 2);
                empty = true;
            } else startDialogue(this, 3);
        } else startDialogue(this, 4);
    }

    public void setLoot(Entity loot) {
        this.loot = loot;
        initDialogue();
    }

    private void initDialogue() {
        dialogues[0][0] = "You open the chest and find a " + loot.name + "!\nYou obtain the " + loot.name + "!";
        dialogues[1][0] = "You open the chest and find a " + loot.name + "!\n...But you cannot carry any more!";
        dialogues[2][0] = "You obtain the " + loot.name + "!";
        dialogues[3][0] = "You cannot carry any more!";
        dialogues[4][0] = "It's empty.";
    }

}
