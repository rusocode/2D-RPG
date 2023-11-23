package com.craivet.world.entity.item;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.world.entity.Type;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Chest extends Item {

    public static final String NAME = "Chest";

    public Chest(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        dialogue = new Dialogue(game);
        type = Type.OBSTACLE;
        stats.name = NAME;
        solid = true;
        hitbox = new Rectangle(3, 16, tile - 7, tile - 20);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        sheet.loadItemFrames(chest, 32, 32, 1);
    }

    @Override
    public void interact() {
        if (!opened) {
            game.playSound(sound_chest_opening);
            sheet.frame = sheet.item[1];
            opened = true;
            if (world.entities.player.inventory.canPickup(loot)) {
                dialogue.dialogues[0][0] = "You open the chest and find a \n" + loot.stats.name + "!";
                dialogue.startDialogue(DIALOGUE_STATE, this, 0);
                empty = true;
            } else {
                dialogue.dialogues[1][0] = "You open the chest and find a \n" + loot.stats.name + "! But you cannot carry \nany more!";
                dialogue.startDialogue(DIALOGUE_STATE, this, 1);
            }
        } else if (!empty) {
            if (world.entities.player.inventory.canPickup(loot)) {
                dialogue.dialogues[2][0] = "You obtain the " + loot.stats.name + "!";
                dialogue.startDialogue(DIALOGUE_STATE, this, 2);
                empty = true;
            } else {
                dialogue.dialogues[3][0] = "You cannot carry any more!";
                dialogue.startDialogue(DIALOGUE_STATE, this, 3);
            }
        } else {
            dialogue.dialogues[4][0] = "It's empty.";
            dialogue.startDialogue(DIALOGUE_STATE, this, 4);
        }
    }

    @Override
    public void setLoot(Item loot) {
        this.loot = loot;
    }

}
