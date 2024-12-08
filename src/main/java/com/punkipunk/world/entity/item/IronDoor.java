package com.punkipunk.world.entity.item;

import com.punkipunk.Dialogue;
import com.punkipunk.Game;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class IronDoor extends Item {

    public static final String NAME = "Iron Door";

    public IronDoor(Game game, World world, int... pos) {
        super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
        dialogue = new Dialogue(game);
        type = Type.OBSTACLE;
        stats.name = NAME;
        solid = true;
        hitbox = new Rectangle(1, 16, tile - 2, tile - 16);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.DOOR_IRON), tile, tile);
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
