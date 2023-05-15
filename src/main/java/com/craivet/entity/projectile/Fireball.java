package com.craivet.entity.projectile;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.World;

import java.awt.*;

import static com.craivet.utils.Global.*;
import static com.craivet.gfx.Assets.*;

public class Fireball extends Projectile {

	public Fireball(Game game, World world) {
		super(game, world);
		setDefaultValues();
	}

	private void setDefaultValues() {
		name = "Fireball";
		speed = 7;
		HP = maxHP = 80;
		attack = 2;
		knockbackValue = 5;
		cost = 1;
		isAlive = false;
		loadMovementImages(entity_fireball, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
	}

	@Override
	public boolean haveResource(Entity entity) {
		return entity.mana >= cost;
	}

	@Override
	public void subtractResource(Entity entity) {
		entity.mana -= cost;
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
