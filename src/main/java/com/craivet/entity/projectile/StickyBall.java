package com.craivet.entity.projectile;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.utils.Utils;

import java.awt.*;

import static com.craivet.gfx.Assets.*;

public class StickyBall extends Projectile {

	public StickyBall(Game game) {
		super(game);
		initDefaultValues();
	}

	private void initDefaultValues() {
		name = "Sticky Ball";
		speed = 5;
		maxLife = 120;
		life = maxLife;
		attack = 3;
		useCost = 1;
		alive = false;

		hitbox.x = 8;
		hitbox.y = 8;
		hitbox.width = 15;
		hitbox.height = 15;
		hitboxDefaultX = hitbox.x;
		hitboxDefaultY = hitbox.y;

		image = Utils.scaleImage(entity_sticky_ball, 32, 32);
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
		return new Color(106, 193, 127);
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
