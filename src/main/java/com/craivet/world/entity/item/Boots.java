package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;

public class Boots extends Item {

	public static final String NAME = "Boots";

	public Boots(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		stats.name = NAME;
		sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.BOOTS), tile, tile);
	}

}
