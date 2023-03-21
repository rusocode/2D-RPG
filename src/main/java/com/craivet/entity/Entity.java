package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.Game;
import com.craivet.entity.item.Item;
import com.craivet.gfx.Assets;
import com.craivet.gfx.SpriteSheet;
import com.craivet.tile.InteractiveTile;
import com.craivet.utils.Timer;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

/**
 * TODO Los metodos para obtener las subimagenes deberian ir en otra clase
 * <p>TODO items y objetos son lo mismo?
 * <p>TODO Se podrian separar los npcs, mobs, items, objetos y proyectiles por paquetes y clases abstractas
 * <p>TODO En vez de usar la variable "movementDown1" para representar la imagen de un objeto, se podria usar una
 * var "image"
 * <p>TODO Los metodos update() y render() se podrian implementar desde una interfaz
 * <p>TODO Algun de estos metodos no tendrian que funcionar en sus respectivas clases?
 */

public abstract class Entity {

	public Game game;
	public Timer timer = new Timer();
	public ArrayList<Entity> inventory = new ArrayList<>();
	public String[] dialogues = new String[20];
	public int dialogueIndex;

	// Atributes
	public int worldX, worldY;
	public String name;
	public int type = TYPE_MOB;
	public BufferedImage image; // Imagenes estaticas
	public BufferedImage heartFull, heartHalf, heartBlank;
	public BufferedImage manaFull, manaBlank;
	public int direction = DIR_DOWN;
	public int speed;
	public int maxLife, life; // 2 de vida representa 1 corazon (heartFull) y 1 de vida representa medio corazon (heartHalf)
	public int maxMana, mana;
	public int ammo;
	public int level, exp, nextLevelExp;
	public int coin;
	public int strength, dexterity;
	public int attack, defense;
	public int attackValue, defenseValue;
	public boolean collision;
	public Rectangle tileArea = new Rectangle(0, 0, 0, 0), attackArea = new Rectangle(0, 0, 0, 0), bodyArea = new Rectangle(0, 0, 48, 48);
	public int bodyAreaDefaultX, bodyAreaDefaultY;
	public Projectile projectile;
	public Entity currentWeapon, currentShield;
	public String itemDescription;
	public int price;

	// Frames
	public BufferedImage movementDown1, movementDown2, movementUp1, movementUp2, movementLeft1, movementLeft2, movementRight1, movementRight2;
	public BufferedImage attackDown1, attackDown2, attackUp1, attackUp2, attackLeft1, attackLeft2, attackRight1, attackRight2;

	// States
	public boolean collisionOn; // Estado que depende de las colisiones con tiles, items y entidades
	public boolean invincible;
	public boolean attacking;
	public boolean alive = true;
	public boolean dead;
	public boolean hpBarOn;
	public int movementNum = 1, attackNum = 1;

	public Entity(Game game) {
		this.game = game;
		initIconsImages(icons, 16, 16);
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
			case DIR_DOWN:
				direction = DIR_UP;
				break;
			case DIR_UP:
				direction = DIR_DOWN;
				break;
			case DIR_LEFT:
				direction = DIR_RIGHT;
				break;
			case DIR_RIGHT:
				direction = DIR_LEFT;
				break;
		}
	}

	public void use(Entity entity) {
	}

	public void checkDrop() {
	}

	/**
	 * Dropea el item.
	 *
	 * @param droppedItem el item.
	 */
	public void dropItem(Item droppedItem) {
		for (int i = 0; i < game.items[1].length; i++) {
			if (game.items[game.currentMap][i] == null) {
				game.items[game.currentMap][i] = droppedItem;
				// La imagen Coin al ser de 32x32, tiene que ajustarse al centro de la imagen del Slime
				game.items[game.currentMap][i].worldX = worldX + (entity_slime.getWidth() / 2 - Assets.item_coin.getWidth() / 2);
				game.items[game.currentMap][i].worldY = worldY + entity_slime.getHeight();
				break;
			}
		}

	}

	public Color getParticleColor() {
		return null;
	}

	public int getParticleSize() {
		return 0;
	}

	public int getParticleSpeed() {
		return 0;
	}

	public int getParticleMaxLife() {
		return 0;
	}

	/**
	 * Genera 4 particulas en el objetivo.
	 *
	 * @param generator la entidad que va a generar las particulas.
	 * @param target    el objetivo en donde se van a generar las particulas.
	 */
	public void generateParticle(Entity generator, Entity target) {
		game.particles.add(new Particle(game, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, -1)); // Top left
		game.particles.add(new Particle(game, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, -1)); // Top right
		game.particles.add(new Particle(game, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), -2, 1)); // Down left
		game.particles.add(new Particle(game, target, generator.getParticleColor(), generator.getParticleSize(), generator.getParticleSpeed(), generator.getParticleMaxLife(), 2, 1)); // Down right
	}

	public void update() {

		setAction();

		collisionOn = false;
		game.cChecker.checkTile(this);
		game.cChecker.checkObject(this);
		game.cChecker.checkEntity(this, game.npcs);
		game.cChecker.checkEntity(this, game.mobs);
		game.cChecker.checkEntity(this, game.iTile);
		damagePlayer(game.cChecker.checkPlayer(this), attack);

		// Si no hay colision, la entidad se puede mover dependiendo de la direccion
		if (!collisionOn) {
			switch (direction) {
				case DIR_DOWN:
					worldY += speed;
					break;
				case DIR_UP:
					worldY -= speed;
					break;
				case DIR_LEFT:
					worldX -= speed;
					break;
				case DIR_RIGHT:
					worldX += speed;
					break;
			}
		}

		timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);
		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
		if (timer.projectileCounter < INTERVAL_PROJECTILE_ATTACK) timer.projectileCounter++;

	}

	public void draw(Graphics2D g2) {
		BufferedImage auxImage = null;
		int screenX = (worldX - game.player.worldX) + game.player.screenX;
		int screenY = (worldY - game.player.worldY) + game.player.screenY;
		if (worldX + tile_size > game.player.worldX - game.player.screenX &&
				worldX - tile_size < game.player.worldX + game.player.screenX &&
				worldY + tile_size > game.player.worldY - game.player.screenY &&
				worldY - tile_size < game.player.worldY + game.player.screenY) {
			switch (direction) {
				case DIR_DOWN:
					auxImage = movementNum == 1 || collisionOn ? movementDown1 : movementDown2;
					break;
				case DIR_UP:
					auxImage = movementNum == 1 || collisionOn ? movementUp1 : movementUp2;
					break;
				case DIR_LEFT:
					auxImage = movementNum == 1 || collisionOn ? movementLeft1 : movementLeft2;
					break;
				case DIR_RIGHT:
					auxImage = movementNum == 1 || collisionOn ? movementRight1 : movementRight2;
					break;
			}

			// Si la barra de hp esta activada
			if (type == TYPE_MOB && hpBarOn) {

				double oneScale = (double) tile_size / maxLife;
				double hpBarValue = oneScale * life;

				/* En caso de que el valor de la barra de vida calculada sea menor a 0, le asigna 0 para que no se
				 * dibuje como valor negativo hacia la izquierda. */
				if (hpBarValue < 0) hpBarValue = 0;

				/*
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX - 1, screenY + tile_size + 4, tile_size + 2, 7);

				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY + tile_size + 5, (int) hpBarValue, 5);
				*/
				timer.timeHpBar(this, INTERVAL_HP_BAR);
			}
			if (invincible) {
				// Sin esto, la barra desaparece despues de 4 segundos, incluso si el player sigue atacando al mob
				timer.hpBarCounter = 0;
				if (!(this instanceof InteractiveTile)) Utils.changeAlpha(g2, 0.4f);
			}
			if (dead) timer.timeDeadAnimation(this, INTERVAL_DEAD_ANIMATION, g2);

			g2.drawImage(auxImage, screenX, screenY, null);
			g2.drawImage(image, screenX, screenY, null); // TODO Es eficiente esto?
			g2.setColor(Color.green);
			g2.drawRect(bodyArea.x + screenX, bodyArea.y + screenY, bodyArea.width, bodyArea.height);
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
			game.playSound(sound_receive_damage);
			// Resta la defensa del player al ataque del mob para calcular el daño justo
			int damage = Math.max(attack - game.player.defense, 0);
			game.player.life -= damage;
			game.player.invincible = true;
		}
	}

	public void initIconsImages(SpriteSheet image, int width, int height) {
		BufferedImage[] subimages = SpriteSheet.getIconsSubimages(image, width, height);
		heartFull = Utils.scaleImage(subimages[0], tile_size, tile_size);
		heartHalf = Utils.scaleImage(subimages[1], tile_size, tile_size);
		heartBlank = Utils.scaleImage(subimages[2], tile_size, tile_size);
		manaFull = Utils.scaleImage(subimages[3], tile_size, tile_size);
		manaBlank = Utils.scaleImage(subimages[4], tile_size, tile_size);
	}

	/**
	 * Inicializa las subimagenes de movimiento del SpriteSheet y escala cada una.
	 *
	 * @param image  el SpriteSheet.
	 * @param width  el ancho de la subimagen.
	 * @param height el alto de la subimagen.
	 */
	public void initMovementImages(SpriteSheet image, int width, int height, int scale) {
		BufferedImage[] subimages = SpriteSheet.getMovementSubimages(image, width, height);
		if (subimages.length > 2) {
			movementDown1 = Utils.scaleImage(subimages[0], scale, scale);
			movementDown2 = Utils.scaleImage(subimages[1], scale, scale);
			movementUp1 = Utils.scaleImage(subimages[2], scale, scale);
			movementUp2 = Utils.scaleImage(subimages[3], scale, scale);
			movementLeft1 = Utils.scaleImage(subimages[4], scale, scale);
			movementLeft2 = Utils.scaleImage(subimages[5], scale, scale);
			movementRight1 = Utils.scaleImage(subimages[6], scale, scale);
			movementRight2 = Utils.scaleImage(subimages[7], scale, scale);
		} else if (subimages.length == 2) { // Slime
			movementDown1 = Utils.scaleImage(subimages[0], scale, scale);
			movementDown2 = Utils.scaleImage(subimages[1], scale, scale);
			movementUp1 = Utils.scaleImage(subimages[0], scale, scale);
			movementUp2 = Utils.scaleImage(subimages[1], scale, scale);
			movementLeft1 = Utils.scaleImage(subimages[0], scale, scale);
			movementLeft2 = Utils.scaleImage(subimages[1], scale, scale);
			movementRight1 = Utils.scaleImage(subimages[0], scale, scale);
			movementRight2 = Utils.scaleImage(subimages[1], scale, scale);
		} else { // Rock
			movementDown1 = Utils.scaleImage(subimages[0], scale, scale);
			movementDown2 = Utils.scaleImage(subimages[0], scale, scale);
			movementUp1 = Utils.scaleImage(subimages[0], scale, scale);
			movementUp2 = Utils.scaleImage(subimages[0], scale, scale);
			movementLeft1 = Utils.scaleImage(subimages[0], scale, scale);
			movementLeft2 = Utils.scaleImage(subimages[0], scale, scale);
			movementRight1 = Utils.scaleImage(subimages[0], scale, scale);
			movementRight2 = Utils.scaleImage(subimages[0], scale, scale);
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
		attackDown1 = Utils.scaleImage(subimages[0], tile_size, tile_size * 2);
		attackDown2 = Utils.scaleImage(subimages[1], tile_size, tile_size * 2);
		attackUp1 = Utils.scaleImage(subimages[2], tile_size, tile_size * 2);
		attackUp2 = Utils.scaleImage(subimages[3], tile_size, tile_size * 2);
		attackLeft1 = Utils.scaleImage(subimages[4], tile_size * 2, tile_size);
		attackLeft2 = Utils.scaleImage(subimages[5], tile_size * 2, tile_size);
		attackRight1 = Utils.scaleImage(subimages[6], tile_size * 2, tile_size);
		attackRight2 = Utils.scaleImage(subimages[7], tile_size * 2, tile_size);
	}

}
