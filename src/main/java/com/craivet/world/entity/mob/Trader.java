package com.craivet.world.entity.mob;

import com.craivet.Dialogue;
import com.craivet.Game;
import com.craivet.Inventory;
import com.craivet.world.World;
import com.craivet.utils.*;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.*;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Trader extends Mob {

    public Trader(Game game, World world, int col, int row) {
        super(game, world, col, row);
        inventory = new Inventory();
        dialogue = new Dialogue(game);
        type = Type.NPC;
        stats.name = "Trader";
        hitbox = new Rectangle(7, 16, 15, 48);
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        sheet.frame = Utils.scaleImage(trader, tile, 64);
        initDialogue();
        addItemsToInventory();
    }

    @Override
    public void dialogue() {
        game.playSound(sound_trade_opening);
        dialogue.startDialogue(TRADE_STATE, this, 0);
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
