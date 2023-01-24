package com.craivet.entity;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase encargada de verificar las colisiones con npcs, mobs y el player.
 *
 * <p>TODO Se podria separar los npcs y mobs por package, y por clases abstractas.
 */

public abstract class Entity {

	public Game game;
	public Timer timer = new Timer();
	public String[] dialogues = new String[20];
	public int dialogueIndex;

	// Atributes
	public String name;
	public String direction = "down";
	public int worldX, worldY;
	public int speed;
	public int type; // 0 = player, 1 = npc, 2 = mob (diferencia entre npcs y mobs, ya que los npcs no atacan)
	public int maxLife;
	public int life; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	public boolean collision;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;

	// Item attributes
	public int attackValue;
	public int defenseValue;
	public String itemDescription;

	// Images
	public BufferedImage movementDown1, movementDown2, movementUp1, movementUp2, movementLeft1, movementLeft2, movementRight1, movementRight2;
	public BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage heartFull, heartHalf, heartBlank;

	// States
	public boolean collisionOn; // Estado que depende de las colisiones con tiles, objetos y entidades
	public boolean invincible;
	public boolean attacking;
	public boolean alive = true;
	public boolean dead;
	public boolean hpBarOn;
	public int movementNum = 1, attackNum = 1;

	public int attackCounter;

	public Entity(Game game) {
		this.game = game;
	}

	public void setAction() {

	}

	public void damageReaction() {
	}

	public void speak() {
		if (dialogues[dialogueIndex] == null) dialogueIndex = 0;
		game.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;

		switch (game.player.direction) {
			case "down":
				direction = "up";
				break;
			case "up":
				direction = "down";
				break;
			case "left":
				direction = "right";
				break;
			case "right":
				direction = "left";
				break;
		}
	}

	public void update() {

		setAction();

		collisionOn = false;
		game.cChecker.checkTile(this);
		game.cChecker.checkObject(this);
		game.cChecker.checkEntity(this, game.npcs);
		game.cChecker.checkEntity(this, game.mobs);
		damagePlayer(game.cChecker.checkPlayer(this));

		// Si no hay colision, la entidad se puede mover dependiendo de la direccion
		if (!collisionOn) {
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

		timer.timeMovement(this, 10);
		if (invincible) timer.timeInvincible(this, 60);

	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int screenX = (worldX - game.player.worldX) + game.player.screenX;
		int screenY = (worldY - game.player.worldY) + game.player.screenY;
		if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
				worldX - game.tileSize < game.player.worldX + game.player.screenX &&
				worldY + game.tileSize > game.player.worldY - game.player.screenY &&
				worldY - game.tileSize < game.player.worldY + game.player.screenY) {
			switch (direction) {
				case "down":
					image = movementNum == 1 || collisionOn ? movementDown1 : movementDown2;
					break;
				case "up":
					image = movementNum == 1 || collisionOn ? movementUp1 : movementUp2;
					break;
				case "left":
					image = movementNum == 1 || collisionOn ? movementLeft1 : movementLeft2;
					break;
				case "right":
					image = movementNum == 1 || collisionOn ? movementRight1 : movementRight2;
					break;
			}

			// TODO Hay un bug con la barra cuando el player tiene mucho damage, la barra del mob se agranda en el ultimo golpe
			// Si la barra de hp esta activada
			if (type == 2 && hpBarOn) {

				double oneScale = (double) game.tileSize / maxLife;
				double hpBarValue = oneScale * life;

				/* En caso de que el valor de la barra de vida calculada sea menor a 0, le asigna 0 para que no se
				 * dibuje como valor negativo hacia la izquierda. */
				if (hpBarValue < 0) hpBarValue = 0;

				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX - 1, screenY + game.tileSize + 4, game.tileSize + 2, 7);

				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY + game.tileSize + 5, (int) hpBarValue, 5);

				timer.timeHpBar(this, 240);
			}

			if (invincible) {
				// Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
				timer.hpBarCounter = 0;
				Utils.changeAlpha(g2, 0.4f);
			}

			if (dead) timer.timeDeadAnimation(this, 10, g2);

			g2.drawImage(image, screenX, screenY, null);
			Utils.changeAlpha(g2, 1);
		}
	}

	/**
	 * Daña al player.
	 */
	private void damagePlayer(boolean contact) {
		// Si el mob hace contacto con el player que no es invencible
		if (this.type == 2 && contact && !game.player.invincible) {
			game.playSound(Assets.receive_damage);

			// Resta la defensa del player al ataque del mob para calcular el daño justo
			int damage = attack - game.player.defense;
			if (damage < 0) damage = 0;

			game.player.life -= damage;
			game.player.invincible = true;
		}
	}

	/**
	 * Inicializa las subimagenes de movimiento del sprite sheet y escala cada una.
	 *
	 * @param image  el sprite sheet.
	 * @param width  el ancho de la subimagen.
	 * @param height el alto de la subimagen.
	 */
	public void initMovementImages(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = SpriteSheet.getMovementSubimages(image, width, height);
		if (subimages.length > 2) { // Npcs
			movementDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementUp1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize);
			movementUp2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize);
			movementLeft1 = Utils.scaleImage(subimages[4], game.tileSize, game.tileSize);
			movementLeft2 = Utils.scaleImage(subimages[5], game.tileSize, game.tileSize);
			movementRight1 = Utils.scaleImage(subimages[6], game.tileSize, game.tileSize);
			movementRight2 = Utils.scaleImage(subimages[7], game.tileSize, game.tileSize);
		} else { // Mobs
			movementDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementUp1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementUp2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementLeft1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementLeft2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
			movementRight1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize);
			movementRight2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize);
		}
	}

	/**
	 * Inicializa las subimagenes de ataque del sprite sheet y escala cada una.
	 *
	 * @param image  el sprite sheet.
	 * @param width  el ancho de la subimagen.
	 * @param height el alto de la subimagen.
	 */
	public void initAttackImages(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = SpriteSheet.getAttackSubimages(image, width, height);
		attackDown1 = Utils.scaleImage(subimages[0], game.tileSize, game.tileSize * 2);
		attackDown2 = Utils.scaleImage(subimages[1], game.tileSize, game.tileSize * 2);
		attackUp1 = Utils.scaleImage(subimages[2], game.tileSize, game.tileSize * 2);
		attackUp2 = Utils.scaleImage(subimages[3], game.tileSize, game.tileSize * 2);
		attackLeft1 = Utils.scaleImage(subimages[4], game.tileSize * 2, game.tileSize);
		attackLeft2 = Utils.scaleImage(subimages[5], game.tileSize * 2, game.tileSize);
		attackRight1 = Utils.scaleImage(subimages[6], game.tileSize * 2, game.tileSize);
		attackRight2 = Utils.scaleImage(subimages[7], game.tileSize * 2, game.tileSize);
	}

}
