package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Trader extends Npc {

	public Trader(Game game, World world, int x, int y) {
		super(game, world);
		worldX = x * tile_size;
		worldY = y * tile_size;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_NPC;
		name = "Trader";

		hitbox.x = 8;
		hitbox.y = 16;
		hitbox.width = 32;
		hitbox.height = 32;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		image = Utils.scaleImage(entity_trader, tile_size, tile_size);
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
		inventory.add(new PotionRed(game, world, 1));
		inventory.add(new Key(game, world));
		inventory.add(new SwordNormal(game, world));
		inventory.add(new Axe(game, world));
		inventory.add(new ShieldWood(game, world));
		inventory.add(new ShieldBlue(game, world));
	}

}
