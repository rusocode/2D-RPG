package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.input.KeyHandler;

import static com.craivet.utils.Constants.*;

public class Player extends Entity {

	private final KeyHandler key;

	// El player permanece fijo en el centro de la pantalla
	public final int screenX;
	public final int screenY;

	public Player(Game game, KeyHandler key) {
		super(game);

		this.key = key;

		// Posiciona al player en el centro de la pantalla
		screenX = game.screenWidth / 2 - (game.tileSize / 2);
		screenY = game.screenHeight / 2 - (game.tileSize / 2);

		setDefaultValues();

	}

	private void setDefaultValues() {

		// Posicion en el mundo
		worldX = game.tileSize * 23;
		worldY = game.tileSize * 21;
		speed = PLAYER_SPEED;
		maxLife = 6;
		life = maxLife; // 1 = heart_half, 2 = heart_full
		direction = "down";

		solidArea.x = 8;
		solidArea.y = 16;
		solidArea.width = 32;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		attackArea.width = 36;
		attackArea.height = 36;

		initMovementImages(Assets.player_movement, ENTITY_WIDTH, ENTITY_HEIGHT);
		initAttackImages(Assets.player_attack, ENTITY_WIDTH, ENTITY_HEIGHT);
	}

	public void update() {

		if (attacking) attacking();

		// Evita que el player se mueva cuando no se presiono ninguna tecla
		if (key.s || key.w || key.a || key.d || key.enter) {

			// Obtiene la direccion dependiendo de la tecla seleccionada
			if (key.s) direction = "down";
			else if (key.w) direction = "up";
			else if (key.a) direction = "left";
			else if (key.d) direction = "right";

			// Verifica las colisiones
			collisionOn = false;
			game.cChecker.checkTile(this);
			pickUpObject(game.cChecker.checkObject(this));
			interactNPC(game.cChecker.checkEntity(this, game.npcs));
			contactMOB(game.cChecker.checkEntity(this, game.mobs));
			game.eHandler.checkEvent();

			// Si no hay colision y si no presiono la tecla enter, el player se puede mover dependiendo de la direccion
			if (!collisionOn && !key.enter) {
				switch (direction) {
					case "down":
						worldY += speed;
						break;
					case "up":
						worldY -= speed;
						break;
					case "left":
						worldX -= speed;
						break;
					case "right":
						worldX += speed;
						break;
				}
			}

			game.keyHandler.enter = false; // TODO No tendria que hacerlo desde el KeyHandler?

			// Timer para las animaciones de movimiento (TODO hacer en un metodo separado)
			movementCounter++;
			if (movementCounter > 10 - speed) {
				if (movementNum == 1) movementNum = 2;
				else if (movementNum == 2) movementNum = 1;
				movementCounter = 0;
			}

		} else movementNum = 1; // Vuelve al sprite inicial (movimiento natural)

		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}

	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int tempScreenX = screenX, tempScreenY = screenY;
		switch (direction) {
			case "down":
				if (!attacking) image = movementNum == 1 || collisionOn ? movementDown1 : movementDown2;
				if (attacking) image = attackNum == 1 ? attackDown1 : attackDown2;
				break;
			case "up":
				if (!attacking) image = movementNum == 1 || collisionOn ? movementUp1 : movementUp2;
				if (attacking) {
					tempScreenY -= game.tileSize; // Soluciona el bug para las imagenes de ataque up y left, ya que la posiciom 0,0 de estan son tiles transparentes
					image = attackNum == 1 ? attackUp1 : attackUp2;
				}
				break;
			case "left":
				if (!attacking) image = movementNum == 1 || collisionOn ? movementLeft1 : movementLeft2;
				if (attacking) {
					tempScreenX -= game.tileSize;
					image = attackNum == 1 ? attackLeft1 : attackLeft2;
				}
				break;
			case "right":
				if (!attacking) image = movementNum == 1 || collisionOn ? movementRight1 : movementRight2;
				if (attacking) {
					// tempScreenX = screenX + attackArea.width;
					image = attackNum == 1 ? attackRight1 : attackRight2;
				}
				break;
		}

		if (invincible) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // Reset alpha
		// g2.setColor(Color.red);
		// g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

	}

	/**
	 * Timer para las animaciones de ataque.
	 */
	public void attacking() {
		attackCounter++;
		if (attackCounter <= 5) attackNum = 1; // (0-5 frame)
		if (attackCounter > 5 && attackCounter <= 25) {
			attackNum = 2; // (6-25 frame)

			// Guarda la posicion actual de worldX, worldY y solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;

			// Ajusta la posicion del player X/Y para el area de ataque
			switch (direction) {
				case "down":
					worldY += attackArea.height;
				case "up":
					worldY -= attackArea.height;
					break;
				case "left":
					worldX -= attackArea.width;
					break;
				case "right":
					worldX += attackArea.width;
					break;
			}

			// attackArea se convierte en solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;

			// Verifica la colision con el mob con la posicion X/Y y solidArea actualizados
			int mobIndex = game.cChecker.checkEntity(this, game.mobs);
			damageMob(mobIndex);

			// Despues de verificar la colision, resetea los datos originales
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;


		}
		if (attackCounter > 25) {
			attackNum = 1;
			attackCounter = 0;
			attacking = false;
		}
	}

	/**
	 * Recoge el objeto si el indice de este es distinto a -1.
	 *
	 * @param objIndex indice del objeto.
	 */
	private void pickUpObject(int objIndex) {
		if (objIndex != -1) {
			System.out.println("Object picked!");
		}
	}

	/**
	 * Interactua con el npc si el indice de este es distinto a -1.
	 *
	 * @param npcIndex indice del npc.
	 */
	private void interactNPC(int npcIndex) {
		if (game.keyHandler.enter) {
			if (npcIndex != -1) {
				game.gameState = game.dialogueState;
				game.npcs[npcIndex].speak();
			} else attacking = true;
		}
	}

	private void contactMOB(int mobIndex) {
		if (mobIndex != -1) {
			if (!invincible) {
				life--;
				invincible = true;
			}
		}
	}

	private void damageMob(int mobIndex) {
		if (mobIndex != -1) {
			if (!game.mobs[mobIndex].invincible) {
				game.mobs[mobIndex].life--;
				game.mobs[mobIndex].invincible = true;
				if (game.mobs[mobIndex].life <= 0) game.mobs[mobIndex] = null;
			}
		}
	}

}
