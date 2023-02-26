package com.craivet.tile;

import java.awt.*;

import com.craivet.Game;
import com.craivet.entity.Item;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class DryTree extends InteractiveTile {

	public DryTree(Game game, int x, int y) {
		super(game);
		worldX = TILE_SIZE * x;
		worldY = TILE_SIZE * y;
		image = Utils.scaleImage(Assets.drytree, TILE_SIZE, TILE_SIZE);
		destructible = true;
		life = 3;
	}

	public boolean isCorrectItem(Item item) {
		return item.type == TYPE_AXE;
	}

	public InteractiveTile getDestroyedForm() {
		return new Trunk(game, worldX / TILE_SIZE, worldY / TILE_SIZE);
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
