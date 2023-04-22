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
        game.state = DIALOGUE_STATE;
        StringBuilder sb = new StringBuilder();
        if (!opened) {
            game.playSound(sound_chest_opening);
            sb.append("You open the chest and find a ").append(loot.name).append("!");
            image = image2;
            opened = true;
            if (world.player.canObtainItem(loot)) {
                sb.append("\nYou obtain the ").append(loot.name).append("!");
                empty = true;
            } else sb.append("\n...But you cannot carry any more!");
        } else if (!empty) {
            if (world.player.canObtainItem(loot)) {
                sb.append("You obtain the ").append(loot.name).append("!");
                empty = true;
            } else sb.append("You cannot carry any more!");
        } else sb.append("It's empty");
        game.ui.currentDialogue = sb.toString();
    }

    public void setLoot(Entity loot) {
        this.loot = loot;
    }

}
