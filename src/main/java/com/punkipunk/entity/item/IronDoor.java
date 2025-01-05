package com.punkipunk.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.json.JsonLoader;
import com.punkipunk.json.model.ItemData;
import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class IronDoor extends Item {

    public static final String NAME = "Iron Door";

    public IronDoor(Game game, World world, int... pos) {
        super(game, world, JsonLoader.getInstance().deserialize("items.ironDoor", ItemData.class), pos);
        itemType = ItemType.OBSTACLE;
        dialogue = new Dialogue(game);
        hitbox = new Rectangle(1, 16, tile - 2, tile - 16);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        initDialogue();
    }

    @Override
    public void interact() {
        dialogue.startDialogue(State.DIALOGUE, this, 0);
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "It won't budge.";
    }

}
