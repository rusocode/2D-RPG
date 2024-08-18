package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.Inventory;
import com.craivet.assets.*;
import com.craivet.states.State;
import com.craivet.world.World;
import com.craivet.utils.*;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.*;

import java.awt.*;

import static com.craivet.utils.Global.*;

public class Trader extends Mob {

    public Trader(Game game, World world, int col, int row) {
        super(game, world, col, row);
        inventory = new Inventory();
        dialogue = new Dialogue(game);
        type = Type.NPC;
        stats.name = "Trader";
        hitbox = new Rectangle(7, 40, 15, 24);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
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
