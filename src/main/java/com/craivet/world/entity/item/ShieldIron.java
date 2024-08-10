package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.assets.Assets;
import com.craivet.assets.TextureAssets;
import com.craivet.world.World;
import com.craivet.world.entity.Type;
import com.craivet.utils.Utils;

import static com.craivet.utils.Global.*;

public class ShieldIron extends Item {

	public static final String NAME = "Iron Shield";

	public ShieldIron(Game game, World world, int... pos) {
		super(game, world, pos.length > 0 ? pos[0] : -1, pos.length > 1 ? pos[1] : -1);
		type = Type.SHIELD;
		stats.name = NAME;
		description = "[" + stats.name + "]\nA shiny iron shield.";
		price = 250;
		defenseValue = 2;
		sheet.frame = Utils.scaleTexture(Assets.getTexture(TextureAssets.Type.SHIELD_IRON), tile, tile);
	}

}
