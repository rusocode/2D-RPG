package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.craivet.Game;
import com.craivet.gfx.Assets;
import com.craivet.input.KeyHandler;
import com.craivet.object.*;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

/**
 * El player permanece fijo en el centro de la pantalla dando la sensacion de movimiento aunque no se "mueva".
 */

public class Player extends Entity {

	private final KeyHandler key;
	public final int screenX, screenY;
	public boolean attackCanceled;

	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;

	public Player(Game game, KeyHandler key) {
		super(game);

		this.key = key;
		// Posiciona al player en el centro de la pantalla
		screenX = game.screenWidth / 2 - (game.tileSize / 2);
		screenY = game.screenHeight / 2 - (game.tileSize / 2);

		setDefaultValues();
	}

	private void setDefaultValues() {
		type = TYPE_PLAYER;
		name = "Player";
		direction = "down";
		speed = PLAYER_SPEED;
		maxLife = 6;
		life = maxLife;
		worldX = game.tileSize * 23;
		worldY = game.tileSize * 21;
		level = 1;
		exp = 0;
		nextLevelExp = 5;
		coin = 0;

		strength = 1; // Mas fuerza, mas daño
		dexterity = 1; // Mas destreza, menos daño
		currentWeapon = new SwordNormal(game);
		currentShield = new ShieldWood(game);
		attack = getAttack();
		defense = getDefense();

		attackArea = currentWeapon.attackArea;

		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

		projectile = new Fireball(game);

		initMovementImages(Assets.player_movement, ENTITY_WIDTH, ENTITY_HEIGHT);
		initAttackImages(Assets.player_attack_sword, ENTITY_WIDTH, ENTITY_HEIGHT);

		setItems();
	}

	public void update() {

		if (attacking) attack();

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
			damagePlayer(game.cChecker.checkEntity(this, game.mobs));
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

			checkAttack();

			game.keyH.enter = false;

			timer.timeMovement(this, 10);

		} else movementNum = 1; // Vuelve al sprite inicial (movimiento natural)

		// Si el proyectil anterior no sigue vivo y si ya pasaron 80 frames desde su lanzamiento
		if (key.shot && !projectile.alive && shotAvailableCounter == 80) {
			projectile.set(worldX, worldY, direction, true, this);
			game.projectiles.add(projectile);
			game.playSound(Assets.burning);
			shotAvailableCounter = 0;
		}

		if (invincible) timer.timeInvincible(this, 60);
		if (shotAvailableCounter < 80) shotAvailableCounter++;

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
					// Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
					tempScreenY -= game.tileSize;
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
				if (attacking) image = attackNum == 1 ? attackRight1 : attackRight2;
				break;
		}

		if (invincible) Utils.changeAlpha(g2, 0.3f);
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		Utils.changeAlpha(g2, 1);

	}

	/**
	 * Verifica si puede atacar. No puede atacar si interactua con un npc o bebe agua.
	 */
	private void checkAttack() {
		// Si presiono enter y el ataque no esta cancelado
		if (key.enter && !attackCanceled) {
			game.playSound(Assets.swing_weapon_wav);
			attacking = true;
			attackCounter = 0;
		}
		attackCanceled = false; // Para que pueda volver a atacar despues de interactuar con un npc o beber agua
	}

	/**
	 * Ataca al mob si el frame de ataque colisiona con el.
	 *
	 * <p>De 0 a 5 milisegundos se muestra el primer frame de ataque. De 6 a 25 milisegundos se muestra el segundo frame
	 * de ataque. Despues de 25 milisegundos vuelve al frame de movimiento.
	 *
	 * <p>En el segundo frame de ataque, la posicion X/Y se ajusta para el area de ataque y verifica si colisiona con un
	 * mob.
	 */
	private void attack() {
		attackCounter++;
		if (attackCounter <= 5) attackNum = 1; // (0-5 frame de ataque 1)
		if (attackCounter > 5 && attackCounter <= 25) { // (6-25 frame de ataque 2)
			attackNum = 2;

			// Guarda la posicion actual de worldX, worldY y bodyArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int bodyAreaWidth = bodyArea.width;
			int bodyAreaHeight = bodyArea.height;

			// Ajusta la posicion del player X/Y para el area de ataque
			switch (direction) {
				case "down":
					worldY += game.tileSize;
				case "up":
					worldY -= attackArea.height;
					break;
				case "left":
					worldX -= attackArea.width;
					break;
				case "right":
					worldX += game.tileSize;
					break;
			}

			// attackArea se convierte en bodyArea
			bodyArea.width = attackArea.width;
			bodyArea.height = attackArea.height;

			// Verifica la colision con el mob con la posicion X/Y y bodyArea actualizados, osea el area de ataque
			int mobIndex = game.cChecker.checkEntity(this, game.mobs);
			damageMob(mobIndex, attack);

			// Despues de verificar la colision, resetea los datos originales
			worldX = currentWorldX;
			worldY = currentWorldY;
			bodyArea.width = bodyAreaWidth;
			bodyArea.height = bodyAreaHeight;
		}
		if (attackCounter > 25) {
			attackNum = 1;
			attackCounter = 0;
			attacking = false;
		}
	}

	/**
	 * Recoge un objeto.
	 *
	 * <p>Si el inventario no esta lleno, lo agrega al inventario y lo elimina del mundo.
	 *
	 * @param objIndex indice del objeto.
	 */
	private void pickUpObject(int objIndex) {
		if (objIndex != -1) {
			String text;
			if (inventory.size() != maxInventorySize) {
				inventory.add(game.objs[objIndex]);
				game.playSound(Assets.coin);
				text = "Got a " + game.objs[objIndex].name + "!";
			} else text = "You cannot carry any more!";
			game.ui.addMessage(text);
			game.objs[objIndex] = null;
		}
	}

	/**
	 * Interactua con el npc.
	 *
	 * @param npcIndex indice del npc.
	 */
	private void interactNPC(int npcIndex) {
		if (game.keyH.enter) {
			if (npcIndex != -1) {
				attackCanceled = true; // No puede atacar si interactua con un npc
				game.gameState = game.dialogueState;
				game.npcs[npcIndex].speak();
			}
		}
	}

	/**
	 * Daña al mob.
	 *
	 * @param mobIndex indice del mob.
	 * @param attack   el tipo de ataque (sword o fireball).
	 */
	public void damageMob(int mobIndex, int attack) {
		if (mobIndex != -1) {
			if (!game.mobs[mobIndex].invincible) {

				// Resta la defensa del mob al ataque del player para calcular el daño justo
				int damage = attack - game.mobs[mobIndex].defense;
				if (damage < 0) damage = 0;
				game.mobs[mobIndex].life -= damage;
				game.ui.addMessage(damage + " damage!");
				if (game.mobs[mobIndex].life > 0) game.playSound(Assets.hit_monster);

				game.mobs[mobIndex].invincible = true;
				game.mobs[mobIndex].hpBarOn = true; // Activa la barra
				game.mobs[mobIndex].damageReaction();

				if (game.mobs[mobIndex].life <= 0) {
					game.playSound(Assets.mob_death);
					game.mobs[mobIndex].dead = true;
					game.ui.addMessage("Killed the " + game.mobs[mobIndex].name + "!");
					game.ui.addMessage("Exp + " + game.mobs[mobIndex].exp);
					exp += game.mobs[mobIndex].exp;
					checkLevelUp();
				}
			}
		}
	}

	/**
	 * El player recibe daño si colisiona con un mob.
	 *
	 * @param mobIndex indice del mob.
	 */
	private void damagePlayer(int mobIndex) {
		if (mobIndex != -1) {
			if (!invincible && !game.mobs[mobIndex].dead) {
				game.playSound(Assets.receive_damage);
				// Resta la defensa del player al ataque del mob para calcular el daño justo
				int damage = game.mobs[mobIndex].attack - defense;
				if (damage < 0) damage = 0;
				life -= damage;
				invincible = true;
			}
		}
	}

	/**
	 * Verifica si subio de nivel.
	 */
	private void checkLevelUp() {
		if (exp >= nextLevelExp) {
			level++;
			nextLevelExp *= 2;
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();

			game.playSound(Assets.level_up);
			game.gameState = game.dialogueState;
			game.ui.currentDialogue = "You are level " + level + "!";
		}
	}

	/**
	 * Selecciona el item del array de inventario utilizando el indice del slot del inventario UI.
	 */
	public void selectItem() {
		int itemIndex = game.ui.getItemIndexOnSlot();
		if (itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			if (selectedItem instanceof SwordNormal || selectedItem instanceof Axe) { // selectedItem.type == typeSword || selectedItem.type == typeAxe
				currentWeapon = selectedItem;
				attack = getAttack();
				initAttackImages(currentWeapon.type == TYPE_SWORD ? Assets.player_attack_sword : Assets.player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);
			}
			if (selectedItem.type == TYPE_SHIELD) {
				currentShield = selectedItem;
				defense = getDefense();
			}
			if (selectedItem.type == TYPE_CONSUMABLE) {
				selectedItem.use(this);
				inventory.remove(itemIndex);
			}
		}
	}

	private int getAttack() {
		return strength * currentWeapon.attackValue;
	}

	private int getDefense() {
		return dexterity * currentShield.defenseValue;
	}

	private void setItems() {
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new Key(game));
	}

}
