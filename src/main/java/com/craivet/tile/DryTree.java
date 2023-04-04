package com.craivet.tile;

import java.awt.*;

import com.craivet.Game;
import com.craivet.World;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class DryTree extends InteractiveTile {

	public DryTree(Game game, World world, int x, int y) {
		super(game, world);
		worldX = tile_size * x;
		worldY = tile_size * y;
		image = Utils.scaleImage(Assets.itile_drytree, tile_size, tile_size);
		destructible = true;
		life = 3;
	}

	public boolean isCorrectItem(Entity item) {
		return item.type == TYPE_AXE;
	}

	public InteractiveTile getDestroyedForm() {
		return new Trunk(game, world, worldX / tile_size, worldY / tile_size);
	}

	public Color getParticleColor() {
		return new Color(65, 50, 30);
	}

	public int getParticleSize() {
		return 6;
	}

	public int getParticleSpeed() {
		return 1;
	}

	public int getParticleMaxLife() {
		return 20;
	}

}
