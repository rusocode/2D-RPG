package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.item.*;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Merchant extends Entity {

	public Merchant(Game game, int x, int y) {
		super(game);
		worldX = x * tile_size;
		worldY = y * tile_size;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_NPC;
		name = "Merchant";

		tileArea.x = 8;
		tileArea.y = 16;
		tileArea.width = 32;
		tileArea.height = 32;

		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
		image = Utils.scaleImage(entity_merchant, tile_size, tile_size);
		initDialogue();
		setItems();
	}

	public void speak() {
		super.speak();
		game.playSound(sound_trade_open);
		game.gameState = TRADE_STATE;
		game.ui.npc = this;
	}

	private void initDialogue() {
		dialogues[0] = "He he, so you found me. I have some good \nstuff. Do you want to trade?";
	}

	private void setItems() {
		inventory.add(new PotionRed(game));
		inventory.add(new Key(game));
		inventory.add(new SwordNormal(game));
		inventory.add(new Axe(game));
		inventory.add(new ShieldWood(game));
		inventory.add(new ShieldBlue(game));
	}

}
