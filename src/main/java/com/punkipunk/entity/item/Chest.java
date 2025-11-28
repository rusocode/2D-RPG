package com.punkipunk.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

public class Chest extends Item {

    public Chest(IGame game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        sheet.loadItemFrames(new SpriteSheet(Utils.loadTexture(itemData.spriteSheetPath())), itemData.frameWidth(), itemData.frameHeight(), itemData.frameScale());
    }

    @Override
    public void interact() {
        if (!opened) {
            game.getGameSystem().audio.playSound(AudioID.Sound.CHEST_OPENING);
            sheet.frame = sheet.item[1];
            opened = true;
            if (world.entitySystem.player.inventory.canAddItem(loot)) {
                dialogue.dialogues[0][0] = "You open the chest and find a \n" + loot.stats.name + "!";
                dialogue.startDialogue(State.DIALOGUE, this, 0);
                world.entitySystem.player.hotbar.add(loot);
                empty = true;
            } else {
                dialogue.dialogues[1][0] = "You open the chest and find a \n" + loot.stats.name + "! But you cannot carry \nany more!";
                dialogue.startDialogue(State.DIALOGUE, this, 1);
            }
        } else if (!empty) {
            if (world.entitySystem.player.inventory.canAddItem(loot)) {
                dialogue.dialogues[2][0] = "You obtain the " + loot.stats.name + "!";
                world.entitySystem.player.hotbar.add(loot);
                dialogue.startDialogue(State.DIALOGUE, this, 2);
                empty = true;
            } else {
                dialogue.dialogues[3][0] = "You cannot carry any more!";
                dialogue.startDialogue(State.DIALOGUE, this, 3);
            }
        } else {
            dialogue.dialogues[4][0] = "It's empty.";
            dialogue.startDialogue(State.DIALOGUE, this, 4);
        }
    }

    @Override
    public void setLoot(Item loot) {
        this.loot = loot;
    }

    @Override
    public ItemID getID() {
        return ItemID.CHEST;
    }

}
