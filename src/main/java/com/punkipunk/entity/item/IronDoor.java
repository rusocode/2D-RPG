package com.punkipunk.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.config.Config;
import com.punkipunk.config.json.ItemConfig;
import com.punkipunk.core.Game;
import com.punkipunk.states.State;
import com.punkipunk.world.World;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class IronDoor extends Item {

    public static final String NAME = "Iron Door";

    public IronDoor(Game game, World world, int... pos) {
        super(game, world, Config.getInstance().getJsonValue("items.ironDoor", ItemConfig.class), pos);
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
