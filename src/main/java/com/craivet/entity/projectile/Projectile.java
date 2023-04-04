package com.craivet.entity.projectile;

import com.craivet.entity.Entity;
import com.craivet.entity.EntityManager;
import com.craivet.entity.Player;

import static com.craivet.utils.Constants.*;

public class Projectile extends Entity {

	private Entity entity;
	protected int useCost;

	public Projectile(EntityManager game) {
		super(game);
	}

	public boolean haveResource(Entity entity) {
		return false;
	}

	public void subtractResource(Entity entity) {
	}

	public void set(int worldX, int worldY, int direction, boolean alive, Entity entity) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.alive = alive;
		this.entity = entity;
		this.life = this.maxLife; // Resetea la vida al valor maximo cada vez que lanza un proyectil
	}

	/**
	 * Actualiza la posicion del proyectil si no colisiona con un mob o si no se termina la vida de este. En caso
	 * contrario, deja de vivir.
	 */
	@Override
	public void update() {

		// Si el player lanza un proyectil
		if (entity instanceof Player) {
			int mobIndex = entityManager.collider.checkEntity(this, entityManager.mobs);
			/* Cuando el proyectil colisiona con un mob, establece el estado collisionOn en true. Por lo tanto, cuando
			 * se vuelva a dibujar el proyectil, este se va a mantener en el frame de movimiento 1 ya que en el operador
			 * ternario, la condicion se mantiene en true y nunca cambia a false para poder mostrar el frame de
			 * movimiento 2. La siguiente linea soluciona este problema. */
			collisionOn = false;
			if (mobIndex != -1 && !entityManager.mobs[entityManager.map][mobIndex].invincible) {
				entityManager.player.damageMob(mobIndex, attack, knockBackPower, direction);
				// En este caso, el generador de particulas es la bola de fuego cuando el player la lanza contra un mob
				generateParticle(entity.projectile, entityManager.mobs[entityManager.map][mobIndex]);
				alive = false;
			}
		}

		// Si el mob lanza un proyectil
		if (!(entity instanceof Player)) {
			boolean contact = entityManager.collider.checkPlayer(this);
			if (contact && !entityManager.player.invincible) {
				damagePlayer(true, attack);
				generateParticle(entity.projectile, entityManager.player);
				alive = false;
			}
		}

		if (life-- <= 0) alive = false;

		if (alive) {
			switch (direction) {
				case DOWN:
					worldY += speed;
					break;
				case UP:
					worldY -= speed;
					break;
				case LEFT:
					worldX -= speed;
					break;
				case RIGHT:
					worldX += speed;
					break;
			}
			timer.timeMovement(this, INTERVAL_PROJECTILE_ANIMATION);
		}
	}

}
