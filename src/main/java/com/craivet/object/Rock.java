package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Projectile;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.ENTITY_HEIGHT;
import static com.craivet.utils.Constants.ENTITY_WIDTH;

public class Rock extends Projectile {

	public Rock(Game game) {
		super(game);
		setDefaultValues();
	}

	private void setDefaultValues() {
		name = "Rock";
		speed = 8;
		maxLife = 80;
		life = maxLife;

		attack = 2;

		useCost = 1;

		alive = false;

		initMovementImages(Assets.rock, ENTITY_WIDTH, ENTITY_HEIGHT);
	}
}
