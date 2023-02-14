package com.craivet.tile;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.entity.Player;

import static com.craivet.utils.Constants.*;

public class InteractiveTile extends Entity {

	public boolean destructible;

	public InteractiveTile(Game game) {
		super(game);
	}

	public boolean isCorrectItem(Player player) {
		return false;
	}

	public InteractiveTile getDestroyedForm() {
		return null;
	}

	public void update() {
		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE_TREE);
	}

}
