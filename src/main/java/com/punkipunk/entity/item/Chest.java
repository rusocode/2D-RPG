package com.punkipunk.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.audio.AudioID;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class Chest extends Item {

    public static final String NAME = "Chest";

    public Chest(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.chest", ItemData.class), pos);
        itemType = ItemType.OBSTACLE;
        dialogue = new Dialogue(game);
        hitbox = new Rectangle(3, 16, tile - 7, tile - 20);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.loadItemFrames(new SpriteSheet(Utils.loadTexture(itemData.spriteSheetPath())), itemData.frameWidth(), itemData.frameHeight(), itemData.frameScale());
    }

    @Override
    public void interact() {
        if (!opened) {
            game.system.audio.playSound(AudioID.Sound.CHEST_OPENING);
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
