package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Coin extends Item {

	public Coin(Game game, World world) {
		super(game, world);
		name = "Coin";
		type = TYPE_PICKUP_ONLY;
		image = Utils.scaleImage(item_coin, 32, 32);
		value = 1;
	}

	public boolean use(Entity entity) {
		game.playSound(sound_coin);
		game.ui.addMessage("Coin +" + value);
		entity.coin += value;
		return true;
	}

}
