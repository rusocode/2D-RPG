package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.object.*;
import com.craivet.utils.Utils;

import java.util.ArrayList;

import static com.craivet.utils.Constants.*;

public class Merchant extends Entity {

	public Merchant(Game game, int x, int y) {
		super(game);
		worldX = x * TILE_SIZE;
		worldY = y * TILE_SIZE;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_NPC;
		name = "Merchant";
		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
		image = Utils.scaleImage(Assets.merchant, TILE_SIZE, TILE_SIZE);
		initDialogue();
		setItems();
	}

	public void speak() {
		super.speak();
		game.gameState = TRADE_STATE;
		game.ui.npc = this;
	}

	private void initDialogue() {
		dialogues[0] = "He he, so you found me.\nI have some good stuff.\nDo you want to trade?";
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
