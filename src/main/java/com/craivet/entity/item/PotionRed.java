package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class PotionRed extends Item {

	public PotionRed(Game game, World world) {
		super(game, world);
		name = "Red Potion";
		type = TYPE_CONSUMABLE;
		image = Utils.scaleImage(item_potion_red, tile_size, tile_size);
		value = 5;
		itemDescription = "[" + name + "]\nHeals your life by " + value + ".";
		price = 25;
	}

	public void use(Entity entity) {
		entity.life += value;
		game.ui.addMessage("+" + value + " hp");
		if (entity.life > entity.maxLife) entity.life = entity.maxLife;
		// game.playSound(sound_potion_red);
	}

}
