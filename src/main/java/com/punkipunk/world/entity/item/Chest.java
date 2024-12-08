package com.punkipunk.world.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.SpriteSheetAssets;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class Chest extends Item {

    public static final String NAME = "Chest";

    public Chest(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        dialogue = new Dialogue(game);
        type = Type.OBSTACLE;
        stats.name = NAME;
        solid = true;
        hitbox = new Rectangle(3, 16, tile - 7, tile - 20);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.loadItemFrames(Assets.getSpriteSheet(SpriteSheetAssets.CHEST), 32, 32, 1);
    }

    @Override
    public void interact() {
        if (!opened) {
            game.system.audio.playSound(Assets.getAudio(AudioAssets.CHEST_OPENING));
            sheet.frame = sheet.item[1];
            opened = true;
            if (world.entities.player.inventory.canAddItem(loot)) {
                dialogue.dialogues[0][0] = "You open the chest and find a \n" + loot.stats.name + "!";
                dialogue.startDialogue(State.DIALOGUE, this, 0);
                empty = true;
            } else {
                dialogue.dialogues[1][0] = "You open the chest and find a \n" + loot.stats.name + "! But you cannot carry \nany more!";
                dialogue.startDialogue(State.DIALOGUE, this, 1);
            }
        } else if (!empty) {
            if (world.entities.player.inventory.canAddItem(loot)) {
                dialogue.dialogues[2][0] = "You obtain the " + loot.stats.name + "!";
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

}
