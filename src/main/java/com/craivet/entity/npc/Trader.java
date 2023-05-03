package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Trader extends Npc {

	public Trader(Game game, World world, int x, int y) {
		super(game, world, x, y);
		initDefaultValues();
	}

	private void initDefaultValues() {
		name = "Trader";
		type = TYPE_NPC;
		image = Utils.scaleImage(entity_trader, tile_size, tile_size);
		hitbox.x = 8;
		hitbox.y = 16;
		hitbox.width = 32;
		hitbox.height = 32;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;
		initDialogue();
		setItems();
	}

	public void speak() {
		game.playSound(sound_trade_open);
		startDialogue(TRADE_STATE, this, 0);
	}

	private void initDialogue() {
		dialogues[0][0] = "He he, so you found me. I have some good \nstuff. Do you want to trade?";
	}

	private void setItems() {
		inventory.add(new PotionRed(game, world, 1));
		inventory.add(new Key(game, world, 1));
		inventory.add(new SwordNormal(game, world));
		inventory.add(new Axe(game, world));
		inventory.add(new ShieldWood(game, world));
		inventory.add(new ShieldBlue(game, world));
	}

}
