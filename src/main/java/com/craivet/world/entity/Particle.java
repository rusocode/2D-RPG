package com.craivet.world.entity;

import com.craivet.Game;
import com.craivet.world.World;

import java.awt.*;

import static com.craivet.utils.Global.*;

/**
 * Las particulas son rectangulos pequeños dibujados con el metodo fillRect(). Esto es opcional, pero tambien se puede
 * agregar imagenes para este caso.
 */

public class Particle extends Entity {

	private final Color color;
	private int size;
	private final int xd;
	private int yd;

	public Particle(Game game, World world, Entity generator, Color color, int size, int speed, int maxHp, int xd, int yd) {
		super(game, world);
		// El generador seria por ejemplo el arbol seco o un proyectil
		this.color = color;
		this.size = size;
		stats.speed = speed;
		stats.maxHp = maxHp;
		this.xd = xd;
		this.yd = yd;

		stats.hp = maxHp;
		int offset = (tile / 2) - (size - 2);
		pos.x = generator.pos.x + offset;
		pos.y = generator.pos.y + offset;

	}

	public void update() {
		stats.hp--;

		/* Si la vida de la particula es la mitad de su vida maxima, entonces la posicion y aumenta en 1, generando asi
		 * un efecto de gravedad. */
		if (stats.hp < stats.maxHp / 3) {
			yd++;
			size--;
		}

		pos.x += xd * stats.speed;
		pos.y += yd * stats.speed;

		if (stats.hp == 0) flags.alive = false;

	}

	public void render(Graphics2D g2) {
		int screenX = pos.x - world.player.pos.x + world.player.stats.screenX;
		int screenY = pos.y - world.player.pos.y + world.player.stats.screenY;
		g2.setColor(color);
		g2.fillRect(screenX, screenY, size, size); // Dibuja un rectangulo como una particula
	}

}
