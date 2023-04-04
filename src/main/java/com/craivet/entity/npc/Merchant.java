package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Merchant extends Npc {

	public Merchant(Game game, int x, int y) {
		super(game);
		worldX = x * tile_size;
		worldY = y * tile_size;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_NPC;
		name = "Merchant";

		hitbox.x = 8;
		hitbox.y = 16;
		hitbox.width = 32;
		hitbox.height = 32;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		image = Utils.scaleImage(entity_merchant, tile_size, tile_size);
		initDialogue();
		setItems();
	}

	public void speak() {
		super.speak();
		entityManager.playSound(sound_trade_open);
		entityManager.gameState = TRADE_STATE;
		entityManager.ui.npc = this;
	}

	private void initDialogue() {
		dialogues[0] = "He he, so you found me. I have some good \nstuff. Do you want to trade?";
	}

	private void setItems() {
		inventory.add(new PotionRed(entityManager));
		inventory.add(new Key(entityManager));
		inventory.add(new SwordNormal(entityManager));
		inventory.add(new Axe(entityManager));
		inventory.add(new ShieldWood(entityManager));
		inventory.add(new ShieldBlue(entityManager));
	}

}
