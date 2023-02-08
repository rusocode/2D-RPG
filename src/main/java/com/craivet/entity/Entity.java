package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.Sound;
import com.craivet.gfx.Assets;
import com.craivet.gfx.SpriteSheet;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

/**
 * TODO Los metodos para obtener las subimagenes deberian ir en otra clase
 * <p>TODO Se podria separar los npcs y mobs por package, y por clases abstractas
 * <p>TODO En vez de usar la variable "movementDown1" para representar la imagen de un objeto, se podria usar una
 * var "image"
 * <p>TODO Separar en paquetes por items, objetos y proyectiles
 * <p>TODO Los metodos update() y render() se podrian implementar desde una interfaz
 */

public abstract class Entity {

	public Game game;
	public Timer timer = new Timer();
	public String[] dialogues = new String[20];
	public int dialogueIndex;

	// Atributes
	public int type = TYPE_MOB;
	public String name;
	public String direction = "down";
	public int speed;
	public int maxLife, life; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
	public int maxMana, mana;
	public int ammo;
	public int worldX, worldY;
	public int level, exp, nextLevelExp;
	public int coin;
	public int strength, dexterity;
	public int attack, defense;
	public boolean collision;
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public Rectangle bodyArea = new Rectangle(0, 0, 48, 48);
	public int bodyAreaDefaultX, bodyAreaDefaultY;
	public Projectile projectile;

	// Images
	public BufferedImage movementDown1, movementDown2, movementUp1, movementUp2, movementLeft1, movementLeft2, movementRight1, movementRight2;
	public BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage heartFull, heartHalf, heartBlank;
	public BufferedImage manaFull, manaBlank;

	// States
	public boolean collisionOn; // Estado que depende de las colisiones con tiles, objetos y entidades
	public boolean invincible;
	public boolean attacking;
	public boolean shooting;
	public boolean alive = true;
	public boolean dead;
	public boolean hpBarOn;
	public int movementNum = 1, attackNum = 1;

	public int attackCounter; // TODO Muevo a timer?
	public int projectileCounter;

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

	public void use(Entity entity) {
	}

	public void update() {

		setAction();

		collisionOn = false;
		game.cChecker.checkTile(this);
		game.cChecker.checkObject(this);
		game.cChecker.checkEntity(this, game.npcs);
		game.cChecker.checkEntity(this, game.mobs);
		damagePlayer(game.cChecker.checkPlayer(this), attack);

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

		timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
		if (projectileCounter < 80) projectileCounter++; // Que hace esto aca?

	}

	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		int screenX = (worldX - game.player.worldX) + game.player.screenX;
		int screenY = (worldY - game.player.worldY) + game.player.screenY;
		if (worldX + TILE_SIZE > game.player.worldX - game.player.screenX &&
				worldX - TILE_SIZE < game.player.worldX + game.player.screenX &&
				worldY + TILE_SIZE > game.player.worldY - game.player.screenY &&
				worldY - TILE_SIZE < game.player.worldY + game.player.screenY) {
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

			// Si la barra de hp esta activada
			if (type == TYPE_MOB && hpBarOn) {

				double oneScale = (double) TILE_SIZE / maxLife;
				double hpBarValue = oneScale * life;

				/* En caso de que el valor de la barra de vida calculada sea menor a 0, le asigna 0 para que no se
				 * dibuje como valor negativo hacia la izquierda. */
				if (hpBarValue < 0) hpBarValue = 0;

				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX - 1, screenY + TILE_SIZE + 4, TILE_SIZE + 2, 7);

				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY + TILE_SIZE + 5, (int) hpBarValue, 5);

				timer.timeHpBar(this, INTERVAL_HP_BAR);
			}

			if (invincible) {
				// Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
				timer.hpBarCounter = 0;
				Utils.changeAlpha(g2, 0.4f);
			}

			if (dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

			g2.drawImage(image, screenX, screenY, null);
			Utils.changeAlpha(g2, 1);
		}
	}

	/**
	 * Daña al player.
	 *
	 * @param contact si el mob hace contacto con el player.
	 * @param attack  puntos de ataque.
	 */
	public void damagePlayer(boolean contact, int attack) {
		// Si el mob hace contacto con el player que no es invencible
		if (type == TYPE_MOB && contact && !game.player.invincible) {
			Sound.play(Assets.receive_damage);
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
		if (subimages.length > 2) {
			movementDown1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementDown2 = Utils.scaleImage(subimages[1], TILE_SIZE, TILE_SIZE);
			movementUp1 = Utils.scaleImage(subimages[2], TILE_SIZE, TILE_SIZE);
			movementUp2 = Utils.scaleImage(subimages[3], TILE_SIZE, TILE_SIZE);
			movementLeft1 = Utils.scaleImage(subimages[4], TILE_SIZE, TILE_SIZE);
			movementLeft2 = Utils.scaleImage(subimages[5], TILE_SIZE, TILE_SIZE);
			movementRight1 = Utils.scaleImage(subimages[6], TILE_SIZE, TILE_SIZE);
			movementRight2 = Utils.scaleImage(subimages[7], TILE_SIZE, TILE_SIZE);
		} else if (subimages.length == 2) { // Slime
			movementDown1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementDown2 = Utils.scaleImage(subimages[1], TILE_SIZE, TILE_SIZE);
			movementUp1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementUp2 = Utils.scaleImage(subimages[1], TILE_SIZE, TILE_SIZE);
			movementLeft1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementLeft2 = Utils.scaleImage(subimages[1], TILE_SIZE, TILE_SIZE);
			movementRight1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementRight2 = Utils.scaleImage(subimages[1], TILE_SIZE, TILE_SIZE);
		} else { // Rock
			movementDown1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementDown2 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementUp1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementUp2 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementLeft1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementLeft2 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementRight1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
			movementRight2 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE);
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
		attackDown1 = Utils.scaleImage(subimages[0], TILE_SIZE, TILE_SIZE * 2);
		attackDown2 = Utils.scaleImage(subimages[1], TILE_SIZE, TILE_SIZE * 2);
		attackUp1 = Utils.scaleImage(subimages[2], TILE_SIZE, TILE_SIZE * 2);
		attackUp2 = Utils.scaleImage(subimages[3], TILE_SIZE, TILE_SIZE * 2);
		attackLeft1 = Utils.scaleImage(subimages[4], TILE_SIZE * 2, TILE_SIZE);
		attackLeft2 = Utils.scaleImage(subimages[5], TILE_SIZE * 2, TILE_SIZE);
		attackRight1 = Utils.scaleImage(subimages[6], TILE_SIZE * 2, TILE_SIZE);
		attackRight2 = Utils.scaleImage(subimages[7], TILE_SIZE * 2, TILE_SIZE);
	}

}
