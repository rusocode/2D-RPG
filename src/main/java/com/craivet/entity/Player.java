package com.craivet.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.craivet.Game;
import com.craivet.entity.item.*;
import com.craivet.entity.mob.Mob;
import com.craivet.input.KeyHandler;
import com.craivet.tile.InteractiveTile;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;
import static com.craivet.gfx.Assets.*;

/**
 * El player permanece fijo en el centro de la pantalla dando la sensacion de movimiento aunque no se "mueva".
 *
 * <p>Quiero aclarar que con el tema de las colisiones entre dos entidades, en el caso del bodyArea, solo se genera
 * colision cuando el limite del player por ejemplo, SUPERA el limite del slime. Pero en el caso del attackArea, solo
 * se genera colision cuando los limites de ambos SE TOCAN.
 */

public class Player extends Entity {

	private final KeyHandler key;
	public final int screenX, screenY;
	public boolean attackCanceled;

	public Player(Game game, KeyHandler key) {
		super(game);
		// Posiciona el player en el centro de la pantalla
		screenX = SCREEN_WIDTH / 2 - (tile_size / 2);
		screenY = SCREEN_HEIGHT / 2 - (tile_size / 2);
		// Posiciona el player en el mundo, 23,21 es el centro
		worldX = 23 * tile_size;
		worldY = 21 * tile_size;
		this.key = key;
		initDefaultValues();
	}

	public void initDefaultValues() {
		type = TYPE_PLAYER;
		name = "Player";
		speed = 3;
		maxLife = 6;
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		ammo = 5;
		level = 1;
		exp = 0;
		nextLevelExp = 5;
		coin = 500;

		invincible = false;

		strength = 1; // Mas fuerza, mas daño
		dexterity = 1; // Mas destreza, menos daño recibido
		currentWeapon = new SwordNormal(game);
		currentShield = new ShieldWood(game);
		attack = getAttack();
		defense = getDefense();

		tileArea.x = 10;
		tileArea.y = 16;
		tileArea.width = 30;
		tileArea.height = 32;

		// attackArea = currentWeapon.attackArea;

		bodyArea.x = 8;
		bodyArea.y = 16;
		bodyArea.width = 32;
		bodyArea.height = 32;
		bodyAreaDefaultX = bodyArea.x;
		bodyAreaDefaultY = bodyArea.y;

		projectile = new Fireball(game);

		initMovementImages(entity_player_movement, ENTITY_WIDTH, ENTITY_HEIGHT, tile_size);
		initAttackImages(currentWeapon.type == TYPE_SWORD ? entity_player_attack_sword : entity_player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);

		setItems();
	}

	public void setDefaultPosition() {
		worldX = tile_size * 23;
		worldY = tile_size * 21;
		direction = DIR_DOWN;
	}

	public void restoreLifeAndMana() {
		life = maxLife;
		mana = maxMana;
		invincible = false;
	}

	public void update() {

		if (attacking) attackWithSword();

		if (checkKeys()) {

			getDirection();
			checkCollisions();
			updatePosition();
			checkAttack();

			// Resetea las teclas de accion
			key.enter = false;
			key.l = false;

			// Temporiza la animacion de movimiento solo cuando se presionan las teclas de movimiento
			if (checkMovementKeys()) timer.timeMovement(this, INTERVAL_MOVEMENT_ANIMATION);

		} else timer.timeNaturalStopWalking(this, 10);

		shootProjectile();

		// Aplica el timer solo si el player es invencible
		if (invincible) timer.timeInvincible(this, INTERVAL_INVINCIBLE);
		if (timer.projectileCounter < INTERVAL_PROJECTILE_ATTACK) timer.projectileCounter++;
		if (timer.attackCounter < INTERVAL_SWORD_ATTACK) timer.attackCounter++;

		if (life > maxLife) life = maxLife;
		if (mana > maxMana) mana = maxMana;
		if (life <= 0) die();
	}

	/**
	 * Ataca al mob si el frame de ataque colisiona con el.
	 *
	 * <p>De 0 a 5 milisegundos se muestra el primer frame de ataque. De 6 a 25 milisegundos se muestra el segundo frame
	 * de ataque. Despues de 25 milisegundos vuelve al frame de movimiento.
	 *
	 * <p>En el segundo frame de ataque, la posicion x/y se ajusta para el area de ataque y verifica si colisiona con un
	 * mob o tile interactivo.
	 */
	private void attackWithSword() {
		timer.attackAnimationCounter++;
		if (timer.attackAnimationCounter <= 5) attackNum = 1; // (de 0-5 ms frame de ataque 1)
		if (timer.attackAnimationCounter > 5 && timer.attackAnimationCounter <= 25) { // (de 6-25 ms frame de ataque 2)
			attackNum = 2;

			// Guarda la posicion actual de worldX, worldY y el tamaño del bodyArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int bodyAreaWidth = bodyArea.width;
			int bodyAreaHeight = bodyArea.height;

			/* Ajusta el area de ataque para cada direccion. Para las direcciones down y up el tamaño va a ser el mismo,
			 * pero el tamaño para las direcciones left y right va a variar. Tambien la posicion varia para cada
			 * direccion. */
			switch (direction) {
				case DIR_DOWN:
					attackArea.x = 9;
					attackArea.y = 5;
					attackArea.width = 10;
					attackArea.height = 27;
					worldX += attackArea.x;
					worldY += attackArea.y + attackArea.height;
					break;
				case DIR_UP:
					attackArea.x = 15;
					attackArea.y = 4;
					attackArea.width = 10;
					attackArea.height = 28;
					worldX += attackArea.x;
					worldY -= bodyArea.y + attackArea.height; // TODO Por que se resta bodyArea.y y no attackArea.y?
					break;
				case DIR_LEFT:
					attackArea.x = 0;
					attackArea.y = 10;
					attackArea.width = 25;
					attackArea.height = 10;
					worldX -= bodyArea.x + attackArea.x + attackArea.width;
					worldY += attackArea.y;
					break;
				case DIR_RIGHT:
					attackArea.x = 16;
					attackArea.y = 10;
					attackArea.width = 24;
					attackArea.height = 10;
					worldX += attackArea.x + attackArea.width;
					worldY += attackArea.y;
					break;
			}

			// Convierte el area del cuerpo en el area de ataque para verificar la colision solo con el area de ataque
			bodyArea.width = attackArea.width;
			bodyArea.height = attackArea.height;

			// Verifica la colision con el mob usando la posicion y tamaño del bodyArea actualizados, osea el area de ataque
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
		if (timer.attackAnimationCounter > 25) {
			attackNum = 1;
			timer.attackAnimationCounter = 0;
			attacking = false;
		}
	}

	private void drawRects(Graphics2D g2) {
		// Imagen
		g2.setColor(Color.magenta);
		g2.drawRect(screenX, screenY, tile_size, tile_size);
		// Cuerpo
		g2.setColor(Color.yellow);
		g2.drawRect(screenX + bodyArea.x, screenY + bodyArea.y, bodyArea.width, bodyArea.height);
		// Area de ataque
		if (attacking) {
			g2.setColor(Color.red);
			switch (direction) {
				case DIR_DOWN:
					g2.drawRect(screenX + bodyArea.x + attackArea.x, screenY + bodyArea.y + attackArea.y + attackArea.height, attackArea.width, attackArea.height);
					break;
				case DIR_UP:
					g2.drawRect(screenX + bodyArea.x + attackArea.x, screenY - attackArea.height, attackArea.width, attackArea.height);
					break;
				case DIR_LEFT:
					g2.drawRect(screenX + attackArea.x - attackArea.width, screenY + bodyArea.y + attackArea.y, attackArea.width, attackArea.height);
					break;
				case DIR_RIGHT:
					g2.drawRect(screenX + bodyArea.x + attackArea.x + attackArea.width, screenY + bodyArea.y + attackArea.y, attackArea.width, attackArea.height);
					break;
			}
		}
	}

	public void draw(Graphics2D g2) {
		BufferedImage frame = null;
		int tempScreenX = screenX, tempScreenY = screenY;
		switch (direction) {
			case DIR_DOWN:
				if (!attacking) frame = movementNum == 1 || collisionOn ? movementDown1 : movementDown2;
				if (attacking) frame = attackNum == 1 ? attackDown1 : attackDown2;
				break;
			case DIR_UP:
				if (!attacking) frame = movementNum == 1 || collisionOn ? movementUp1 : movementUp2;
				if (attacking) {
					// Soluciona el bug para las imagenes de ataque up y left, ya que la posicion 0,0 de estas imagenes son tiles transparentes
					tempScreenY -= tile_size;
					frame = attackNum == 1 ? attackUp1 : attackUp2;
				}
				break;
			case DIR_LEFT:
				if (!attacking) frame = movementNum == 1 || collisionOn ? movementLeft1 : movementLeft2;
				if (attacking) {
					tempScreenX -= tile_size;
					frame = attackNum == 1 ? attackLeft1 : attackLeft2;
				}
				break;
			case DIR_RIGHT:
				if (!attacking) frame = movementNum == 1 || collisionOn ? movementRight1 : movementRight2;
				if (attacking) frame = attackNum == 1 ? attackRight1 : attackRight2;
				break;
		}

		if (invincible) Utils.changeAlpha(g2, 0.3f);
		g2.drawImage(frame, tempScreenX, tempScreenY, null);

		// drawRects(g2);

		Utils.changeAlpha(g2, 1);

	}

	/**
	 * Verifica si puede atacar. No puede atacar si interactua con un npc o bebe agua.
	 */
	private void checkAttack() {
		// Si presiono enter y el ataque no esta cancelado
		if (key.enter && !attackCanceled && timer.attackCounter == INTERVAL_SWORD_ATTACK) {
			if (currentWeapon.type == TYPE_SWORD) game.playSound(sound_swing_weapon);
			if (currentWeapon.type == TYPE_AXE) game.playSound(sound_swing_axe);
			attacking = true;
			timer.attackAnimationCounter = 0;
			timer.attackCounter = 0;
		}
		attackCanceled = false; // Para que pueda volver a atacar despues de interactuar con un npc o beber agua
	}

	private void shootProjectile() {
		if (key.f && !projectile.alive && timer.projectileCounter == INTERVAL_PROJECTILE_ATTACK && projectile.haveResource(this)) {
			game.playSound(sound_burning);
			projectile.set(worldX, worldY, direction, true, this);
			game.projectiles.add(projectile);
			projectile.subtractResource(this);
			timer.projectileCounter = 0;
		}
	}

	/**
	 * Recoge un item.
	 *
	 * @param itemIndex indice del item.
	 */
	private void pickUpItem(int itemIndex) {
		if (key.l) {
			if (itemIndex != -1) {
				Item item = game.items[game.currentMap][itemIndex];
				if (item.type == TYPE_PICKUP_ONLY) item.use(this);
				else if (inventory.size() != MAX_INVENTORY_SIZE) {
					inventory.add(item);
					game.playSound(sound_power_up);
					game.ui.addMessage("Got a " + item.name + "!");
				} else {
					game.ui.addMessage("You cannot carry any more!");
					return; // En caso de que el inventario este lleno, no elimina el item del mundo
				}
				game.items[game.currentMap][itemIndex] = null;
			} else game.ui.addMessage("Nothing here");
		}
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
			game.npcs[game.currentMap][npcIndex].speak();
		}
	}

	/**
	 * Daña al mob.
	 *
	 * @param mobIndex indice del mob.
	 * @param attack   el tipo de ataque (sword o fireball).
	 */
	public void damageMob(int mobIndex, int attack) {
		if (mobIndex != -1) { // TODO Lo cambio por >= 0 para evitar la doble negacion y comparacion -1?
			Mob mob = game.mobs[game.currentMap][mobIndex];
			if (!mob.invincible) {
				int damage = Math.max(attack - mob.defense, 0);
				mob.life -= damage;
				game.ui.addMessage(damage + " damage!");
				if (mob.life > 0) game.playSound(sound_hit_monster);

				mob.invincible = true;
				mob.hpBarOn = true;
				mob.damageReaction();

				if (mob.life <= 0) {
					game.playSound(sound_mob_death);
					mob.dead = true;
					game.ui.addMessage("Killed the " + mob.name + "!");
					game.ui.addMessage("Exp + " + mob.exp);
					exp += mob.exp;
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
		if (mobIndex >= 0) {
			Mob mob = game.mobs[game.currentMap][mobIndex];
			if (!invincible && !mob.dead) {
				game.playSound(sound_receive_damage);
				// En caso de que el ataque sea menor a la defensa, entonces no hace daño
				int damage = Math.max(mob.attack - defense, 0);
				life -= damage;
				invincible = true;
			}
		}
	}

	/**
	 * Daña al tile interactivo.
	 *
	 * @param iTileIndex indice del tile interactivo.
	 */
	private void damageInteractiveTile(int iTileIndex) {
		if (iTileIndex != -1) {
			InteractiveTile iTile = game.iTile[game.currentMap][iTileIndex];
			if (iTile.destructible && iTile.isCorrectItem(currentWeapon) && !iTile.invincible) {
				game.playSound(sound_cut_tree);

				iTile.life--;
				iTile.invincible = true;

				generateParticle(iTile, iTile);

				if (iTile.life == 0) game.iTile[game.currentMap][iTileIndex] = iTile.getDestroyedForm();
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

			game.playSound(sound_level_up);
			game.gameState = DIALOGUE_STATE;
			game.ui.currentDialogue = "You are level " + level + "!";
		}
	}

	/**
	 * Selecciona el item del array de inventario utilizando el indice del slot del inventario UI.
	 */
	public void selectItem() {
		int itemIndex = game.ui.getItemIndexOnSlot(game.ui.playerSlotCol, game.ui.playerSlotRow);
		if (itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			if (selectedItem instanceof SwordNormal || selectedItem instanceof Axe) {
				currentWeapon = selectedItem;
				attackArea = currentWeapon.attackArea;
				attack = getAttack();
				if (currentWeapon.type == TYPE_SWORD) game.playSound(sound_draw_sword);
				initAttackImages(currentWeapon.type == TYPE_SWORD ? entity_player_attack_sword : entity_player_attack_axe, ENTITY_WIDTH, ENTITY_HEIGHT);
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

	/**
	 * Obtiene la direccion dependiendo de la tecla seleccionada.
	 */
	private void getDirection() {
		if (key.s) direction = DIR_DOWN;
		else if (key.w) direction = DIR_UP;
		else if (key.a) direction = DIR_LEFT;
		else if (key.d) direction = DIR_RIGHT;
	}

	/**
	 * Verifica las colisiones con tiles, objetos, npcs, mobs, tiles interactivos y eventos.
	 */
	private void checkCollisions() {
		collisionOn = false;
		game.cChecker.checkTile(this);
		pickUpItem(game.cChecker.checkObject(this));
		interactNPC(game.cChecker.checkEntity(this, game.npcs));
		damagePlayer(game.cChecker.checkEntity(this, game.mobs));
		game.cChecker.checkEntity(this, game.iTile);
		game.eHandler.checkEvent();
	}

	/**
	 * Actualiza la posicion del player.
	 */
	private void updatePosition() {
		/* Si no hay colision y si no presiono ninguna telca de accion, el player se puede mover dependiendo de la
		 * direccion */
		if (!collisionOn && !key.enter && !key.l) {
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
	}

	private boolean checkKeys() {
		return checkMovementKeys() || checkAccionKeys();
	}

	private boolean checkMovementKeys() {
		return key.s || key.w || key.a || key.d;
	}

	private boolean checkAccionKeys() {
		return key.enter || key.l;
	}

	private void die() {
		game.gameState = GAME_OVER_STATE;
		game.playSound(sound_player_die);
		game.ui.commandNum = -1;
		game.music.stop();
		attacking = false;
	}

	private int getAttack() {
		return strength * currentWeapon.attackValue;
	}

	private int getDefense() {
		return dexterity * currentShield.defenseValue;
	}

	public void setItems() {
		inventory.clear();
		inventory.add(currentWeapon);
		inventory.add(new Axe(game));
		inventory.add(currentShield);
		inventory.add(new PotionRed(game));
		inventory.add(new PotionRed(game));
		inventory.add(new PotionRed(game));
	}

}
