package com.craivet.entity.npc;

import com.craivet.Game;
import com.craivet.entity.Entity;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

public class Oldman extends Entity {

	public Oldman(Game game, int x, int y) {
		super(game);
		worldX = x * tile_size;
		worldY = y * tile_size;
		initDefaultValues();
	}

	private void initDefaultValues() {
		type = TYPE_NPC;
		name = "Oldman";
		speed = 1;

		tileArea.x = 8;
		tileArea.y = 16;
		tileArea.width = 32;
		tileArea.height = 32;

		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;
		initMovementImages(entity_oldman, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
		initDialogue();
	}

	/**
	 * Establece la accion especificada.
	 */
	public void setAction() {
		if (onPath) searchPath(9, 12);
		else timer.timeDirection(this, INTERVAL_DIRECTION);
	}

	public void speak() {
		super.speak();
		onPath = true;
	}

	private void initDialogue() {
		dialogues[0] = "Hola quemado!";
		dialogues[1] = "Creo que arriba del bosque hay \nunas plantas de rulo...";
		dialogues[2] = "Ahora el esta re quemado y no \nreacciona, asi que podes aprovechar \ny cortarlas";
		dialogues[3] = "Ojo que pegan fuerte";
	}

}
