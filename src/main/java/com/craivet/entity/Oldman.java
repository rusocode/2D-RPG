package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;

import java.util.Random;

import static com.craivet.utils.Constants.*;

public class Oldman extends Entity {

	public Oldman(Game game) {
		super(game);
		setDefaultValues();
	}

	private void setDefaultValues() {
		name = "Oldman";
		direction = "down";
		speed = 1;
		type = 1;

		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		initMovementImages(Assets.oldman, ENTITY_WIDTH, ENTITY_HEIGHT);
		initDialogue();
	}

	public void setAction() {
		timer.actionLock(this, 120);
	}

	public void initDialogue() {
		dialogues[0] = "Hola quemado!";
		dialogues[1] = "Creo que arriba del bosque hay \nunas plantas de rulo...";
		dialogues[2] = "Ahora el esta re quemado y no \nreacciona, asi que podes aprovechar \ny cortarlas";
		dialogues[3] = "Ojo que pegan fuerte";
	}

	public void speak() {
		super.speak();
	}

}
