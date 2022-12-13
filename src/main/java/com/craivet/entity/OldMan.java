package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;

import java.awt.*;
import java.util.Random;

public class OldMan extends Entity {

	public OldMan(Game game) {
		super(game);
		direction = "down";
		speed = 1;

		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		initImages(Assets.oldman, 16, 16);
		initDialogue();
	}

	public void setAction() {
		actionLockCounter++;
		if (actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100) + 1;
			if (i <= 25) direction = "down";
			if (i > 25 && i <= 50) direction = "up";
			if (i > 50 && i <= 75) direction = "left";
			if (i > 75 && i <= 100) direction = "right";
			actionLockCounter = 0;
		}
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
