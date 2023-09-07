package com.craivet.world.entity.mob;

import com.craivet.Game;
import com.craivet.world.World;
import com.craivet.utils.*;
import com.craivet.world.entity.Type;
import com.craivet.world.entity.item.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Trader extends Mob {

    public Trader(Game game, World world, int x, int y) {
        super(game, world, x, y);
        name = "Trader";
        type = Type.NPC;
        image = Utils.scaleImage(trader, tile, tile);
        hitbox.x = 8;
        hitbox.y = 16;
        hitbox.width = (tile - hitbox.x) - 9;
        hitbox.height = tile - hitbox.y;
        hitboxDefaultX = hitbox.x;
        hitboxDefaultY = hitbox.y;
        initDialogue();
        setItems();
    }

    @Override
    public void dialogue() {
        game.playSound(sound_trade_opening);
        startDialogue(TRADE_STATE, this, 0);
    }

    private void initDialogue() {
        dialogues[0][0] = "He he, so you found me. I have some \ngood stuff. Do you want to trade?";
    }

    private void setItems() {
        inventory.add(new PotionRed(game, world, 1));
        inventory.add(new Key(game, world, 1));
        inventory.add(new SwordIron(game, world));
        inventory.add(new Axe(game, world));
        inventory.add(new ShieldWood(game, world));
        inventory.add(new ShieldBlue(game, world));
    }

}
