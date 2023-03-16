package com.craivet.object;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Projectile;
import com.craivet.gfx.Assets;

import java.awt.*;

import static com.craivet.utils.Constants.*;

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
		attack = 3;
		useCost = 1;
		alive = false;
		initMovementImages(Assets.rock, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	@Override
	public boolean haveResource(Entity entity) {
		return entity.ammo >= useCost;
	}

	@Override
	public void subtractResource(Entity entity) {
		entity.ammo -= useCost;
	}

	@Override
	public Color getParticleColor() {
		return new Color(40, 50, 0);
	}

	@Override
	public int getParticleSize() {
		return 10;
	}

	@Override
	public int getParticleSpeed() {
		return 1;
	}

	@Override
	public int getParticleMaxLife() {
		return 20;
	}

}
