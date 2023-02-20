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

	public Item currentWeapon;
	public Item currentShield;

	public ArrayList<Item> inventory = new ArrayList<>();

	public Player(Game game, KeyHandler key) {
		super(game);

		this.key = key;
		// Posiciona al player en el centro de la pantalla
		screenX = SCREEN_WIDTH / 2 - (TILE_SIZE / 2);
		screenY = SCREEN_HEIGHT / 2 - (TILE_SIZE / 2);

		setDefaultValues();
	}

	private void setDefaultValues() {
		type = TYPE_PLAYER;
		name = "Player";
		direction = "down";
		speed = PLAYER_SPEED;
		maxLife = 6;
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		ammo = 5;
		worldX = TILE_SIZE * 23;
		worldY = TILE_SIZE * 21;
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

		// Los limites del bodyArea (cuerpo del player) son diferentes a los limites de la imagen escalada
		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

		projectile = new Fireball(game);

		initMovementImages(Assets.player_movement, ENTITY_WIDTH, ENTITY_HEIGHT);
		initAttackImages(currentWeapon.type == TYPE_SWORD ? Assets.player_attack_sword : Assets.player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

		setItems();
	}

	public void update() {

		if (attacking) attackWithSword();

		// Evita que el player se mueva cuando no se presiono ninguna tecla
		if (key.s || key.w || key.a || key.d || key.enter || key.pickup) {

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
			game.cChecker.checkEntity(this, game.iTile);
			game.eHandler.checkEvent();

			// Si no hay colision y si no presiono la tecla enter, el player se puede mover dependiendo de la direccion
			if (!collisionOn && !key.enter && !key.pickup) {
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

			key.enter = false;
			key.pickup = false;

			timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);

		} else {
			naturalStopWalkingCounter++;
			if(naturalStopWalkingCounter == 10) {
				movementNum = 1;
				naturalStopWalkingCounter = 0;
			}
		}

		attackWithProjectile();

		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
		if (projectileCounter < INTERVAL_PROJECTILE) projectileCounter++;
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
					tempScreenY -= TILE_SIZE;
					image = attackNum == 1 ? attackUp1 : attackUp2;
				}
				break;
			case "left":
				if (!attacking) image = movementNum == 1 || collisionOn ? movementLeft1 : movementLeft2;
				if (attacking) {
					tempScreenX -= TILE_SIZE;
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
		// g2.setColor(Color.yellow);
		// g2.drawRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
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
	private void attackWithSword() {
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
					worldY += TILE_SIZE;
				case "up":
					worldY -= attackArea.height;
					break;
				case "left":
					worldX -= attackArea.width;
					break;
				case "right":
					worldX += TILE_SIZE;
					break;
			}

			// attackArea se convierte en bodyArea
			bodyArea.width = attackArea.width;
			bodyArea.height = attackArea.height;

			// Verifica la colision con el mob con la posicion X/Y y bodyArea actualizados, osea el area de ataque
			int mobIndex = game.cChecker.checkEntity(this, game.mobs);
			damageMob(mobIndex, attack);

			int iTileIndex = game.cChecker.checkEntity(this, game.iTile);
			damageInteractiveTile(iTileIndex);

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

	private void attackWithProjectile() {
		/* Si presiono la tecla f, y si el proyectil anterior no sigue vivo, y si ya pasaron 80 frames desde su
		 * lanzamiento y si el proyectil tiene recursos (mana, ammo, etc.) */
		if (key.shot && !projectile.alive && projectileCounter == INTERVAL_PROJECTILE && projectile.haveResource(this)) {
			projectile.set(worldX, worldY, direction, true, this);
			projectile.subtractResource(this);
			game.projectiles.add(projectile);
			game.playSound(Assets.burning);
			projectileCounter = 0;
		}
	}

	/**
	 * Recoge un objeto.
	 *
	 * <p>Si el inventario no esta lleno, lo agrega y lo elimina del mundo.
	 *
	 * @param objIndex indice del objeto.
	 */
	private void pickUpObject(int objIndex) {
		if (objIndex != -1 && key.pickup) {
			if (game.objs[objIndex].type == TYPE_PICKUP_ONLY) {
				game.objs[objIndex].use(this);
				game.objs[objIndex] = null;
			} else if (inventory.size() != MAX_INVENTORY_SIZE) {
				inventory.add(game.objs[objIndex]);
				game.playSound(Assets.power_up);
				game.ui.addMessage("Got a " + game.objs[objIndex].name + "!");
				game.objs[objIndex] = null;
			} else game.ui.addMessage("You cannot carry any more!");
		} else if (key.pickup) game.ui.addMessage("Nothing here");
	}

	/**
	 * Interactua con el npc.
	 *
	 * @param npcIndex indice del npc.
	 */
	private void interactNPC(int npcIndex) {
		if (npcIndex != -1 && key.enter) {
			attackCanceled = true; // No puede atacar si interactua con un npc
			game.gameState = DIALOGUE_STATE;
			game.npcs[npcIndex].speak();
		}
	}

	/**
	 * Daña al mob.
	 *
	 * @param mobIndex indice del mob.
	 * @param attack   el tipo de ataque (sword o fireball).
	 */
	public void damageMob(int mobIndex, int attack) {
		if (mobIndex != -1 && !game.mobs[mobIndex].invincible) {
			// Resta la defensa del mob al ataque del player para calcular el daño justo
			int damage = attack - game.mobs[mobIndex].defense;
			if (damage < 0) damage = 0; // TODO No tendria que ser 1 si es 0 o menor a 0?
			game.mobs[mobIndex].life -= damage;
			game.ui.addMessage(damage + " damage!");
			if (game.mobs[mobIndex].life > 0) {
				game.playSound(Assets.hit_monster);
			}

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

	/**
	 * El player recibe daño si colisiona con un mob.
	 *
	 * @param mobIndex indice del mob.
	 */
	private void damagePlayer(int mobIndex) {
		if (mobIndex != -1 && !invincible && !game.mobs[mobIndex].dead) {
			game.playSound(Assets.receive_damage);
			// Resta la defensa del player al ataque del mob para calcular el daño justo
			int damage = game.mobs[mobIndex].attack - defense;
			if (damage < 0) damage = 0;
			life -= damage;
			invincible = true;
		}
	}

	/**
	 * Daña al tile interactivo.
	 *
	 * @param iTileIndex indice del tile interactivo.
	 */
	private void damageInteractiveTile(int iTileIndex) {
		if (iTileIndex != -1 && game.iTile[iTileIndex].destructible && game.iTile[iTileIndex].isCorrectItem(this) && !game.iTile[iTileIndex].invincible) {
			game.playSound(Assets.cuttree);
			game.iTile[iTileIndex].life--;
			game.iTile[iTileIndex].invincible = true;

			// Generate particle
			generateParticle(game.iTile[iTileIndex], game.iTile[iTileIndex]);

			if (game.iTile[iTileIndex].life == 0) game.iTile[iTileIndex] = game.iTile[iTileIndex].getDestroyedForm();
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
			game.gameState = DIALOGUE_STATE;
			game.ui.currentDialogue = "You are level " + level + "!";
		}
	}

	/**
	 * Selecciona el item del array de inventario utilizando el indice del slot del inventario UI.
	 */
	public void selectItem() {
		int itemIndex = game.ui.getItemIndexOnSlot();
		if (itemIndex < inventory.size()) {
			Item selectedItem = inventory.get(itemIndex);
			if (selectedItem instanceof SwordNormal || selectedItem instanceof Axe) {
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
