package com.craivet.entity;

import com.craivet.Game;
import com.craivet.World;

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

	public Particle(Game game, World world, Entity generator, Color color, int size, int speed, int maxLife, int xd, int yd) {
		super(game, world);
		// El generador seria por ejemplo el arbol seco o un proyectil
		this.color = color;
		this.size = size;
		this.speed = speed;
		this.maxHp = maxLife;
		this.xd = xd;
		this.yd = yd;

		hp = maxLife;
		int offset = (tile_size / 2) - (size - 2);
		x = generator.x + offset;
		y = generator.y + offset;

	}

	public void update() {
		hp--;

		/* Si la vida de la particula es la mitad de su vida maxima, entonces la posicion y aumenta en 1, generando asi
		 * un efecto de gravedad. */
		if (hp < maxHp / 3) {
			yd++;
			size--;
		}

		x += xd * speed;
		y += yd * speed;

		if (hp == 0) flags.alive = false;

	}

	public void render(Graphics2D g2) {
		int screenX = x - world.player.x + world.player.screenX;
		int screenY = y - world.player.y + world.player.screenY;
		g2.setColor(color);
		g2.fillRect(screenX, screenY, size, size); // Dibuja un rectangulo como una particula
	}

}
