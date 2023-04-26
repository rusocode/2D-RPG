package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Boots extends Item {

	public static final String item_name = "Boots";

	public Boots(Game game, World world) {
		super(game, world);
		name = item_name;
		image = Utils.scaleImage(item_boots, tile_size, tile_size);
	}

}
