package com.punkipunk.world.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.Game;
import com.punkipunk.Inventory;
import com.punkipunk.assets.Assets;
import com.punkipunk.assets.AudioAssets;
import com.punkipunk.assets.TextureAssets;
import com.punkipunk.states.State;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;
import com.punkipunk.world.entity.Type;
import com.punkipunk.world.entity.item.*;
import javafx.scene.shape.Rectangle;

import static com.punkipunk.utils.Global.tile;

public class Trader extends Mob {

    public Trader(Game game, World world, int col, int row) {
        super(game, world, col, row);
        inventory = new Inventory();
        dialogue = new Dialogue(game);
        type = Type.NPC;
        stats.name = "Trader";
        hitbox = new Rectangle(7, 40, 15, 24);
        hitboxDefaultX = hitbox.getX();
        hitboxDefaultY = hitbox.getY();
        sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.TRADER), tile, 64);
        initDialogue();
        addItemsToInventory();
    }

    @Override
    public void dialogue() {
        game.playSound(Assets.getAudio(AudioAssets.TRADE_OPENING));
        dialogue.startDialogue(State.TRADE, this, 0);
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "He he, so you found me. I have some \ngood stuff. Do you want to trade?";
    }

    private void addItemsToInventory() {
        inventory.add(new PotionRed(game, world, 1));
        inventory.add(new PotionBlue(game, world, 1));
        inventory.add(new SwordIron(game, world));
        inventory.add(new ShieldWood(game, world));
        inventory.add(new ShieldIron(game, world));
    }

}
