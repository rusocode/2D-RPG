package com.craivet.entity.item;

import com.craivet.Game;
import com.craivet.entity.EntityManager;
import com.craivet.tile.World;
import com.craivet.utils.Utils;

import java.util.WeakHashMap;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class SwordNormal extends Item {

	public SwordNormal(Game game, World world, EntityManager entityManager) {
		super(game, world, entityManager);
		name = "Normal Sword";
		type = TYPE_SWORD;
		image = Utils.scaleImage(item_sword_normal, tile_size, tile_size);
		itemDescription = "[" + name + "]\nAn old sword.";
		price = 20;
		attackValue = 2;
		knockBackPower = 2;
	}

}
