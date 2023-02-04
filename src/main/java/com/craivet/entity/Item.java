package com.craivet.entity;

import com.craivet.Game;

public class Item extends Entity {

	public String itemDescription;
	public int attackValue, defenseValue;
	public int useCost;

	public Item(Game game) {
		super(game);
	}

}
