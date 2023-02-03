package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Projectile;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

public class Fireball extends Projectile {

	public Fireball(Game game) {
		super(game);
		setDefaultValues();
	}

	private void setDefaultValues() {
		name = "Fireball";
		speed = 7;
		maxLife = 80;
		life = maxLife;

		attack = 2;

		useCost = 1;

		alive = false;

		initMovementImages(Assets.fireball, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

}
