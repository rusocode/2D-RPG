package com.craivet.entity.projectile;

import com.craivet.Game;
import com.craivet.entity.Entity;

import java.awt.*;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

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
		initMovementImages(entity_fireball, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
	}

	@Override
	public boolean haveResource(Entity entity) {
		return entity.mana >= useCost;
	}

	@Override
	public void subtractResource(Entity entity) {
		entity.mana -= useCost;
	}

	@Override
	public Color getParticleColor() {
		return new Color(240, 50, 0);
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