package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.Entity;
import com.craivet.gfx.Assets;

import static com.craivet.utils.Constants.*;

public class Oldman extends Entity {

	public Oldman(Game game, int x, int y) {
		super(game);
		worldX = x * TILE_SIZE;
		worldY = y * TILE_SIZE;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_NPC;
		name = "Oldman";
		speed = 1;
		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
		initMovementImages(Assets.oldman, ENTITY_WIDTH, ENTITY_HEIGHT);
		initDialogue();
	}

	/**
	 * Establece la accion especificada.
	 */
	public void setAction() {
		timer.timeDirection(this, INTERVAL_DIRECTION);
	}

	public void speak() {
		super.speak();
	}

	private void initDialogue() {
		dialogues[0] = "Hola quemado!";
		dialogues[1] = "Creo que arriba del bosque hay \nunas plantas de rulo...";
		dialogues[2] = "Ahora el esta re quemado y no \nreacciona, asi que podes aprovechar \ny cortarlas";
		dialogues[3] = "Ojo que pegan fuerte";
	}

}
