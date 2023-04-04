package com.craivet.entity;

import com.craivet.*;
import com.craivet.ai.AStar;
import com.craivet.entity.item.Item;
import com.craivet.entity.mob.Mob;
import com.craivet.entity.npc.Npc;
import com.craivet.entity.projectile.Projectile;
import com.craivet.input.KeyManager;
import com.craivet.tile.InteractiveTile;
import com.craivet.tile.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.craivet.utils.Constants.*;

public class EntityManager {

	private final Game game;
	private final World world;

	public AssetSetter aSetter;
	public EventHandler event;
	public Collider collider;
	public Config config;
	public AStar aStar;

	// Entities
	public Player player;
	public Entity[][] items = new Item[MAX_MAP][20];
	public Entity[][] mobs = new Mob[MAX_MAP][20];
	public Entity[][] npcs = new Npc[MAX_MAP][10];
	public Entity[][] projectiles = new Projectile[MAX_MAP][20];
	private final List<Entity> entities = new ArrayList<>();
	public ArrayList<Entity> itemList = new ArrayList<>();
	public ArrayList<Entity> particles = new ArrayList<>();
	// Interactive tiles
	public InteractiveTile[][] iTile = new InteractiveTile[MAX_MAP][50];

	public EntityManager(Game game, World world) {
		this.game = game;
		this.world = world;
		event = new EventHandler(game,world, this);
		collider = new Collider(world, this);
		config = new Config(game);
		aSetter = new AssetSetter(game, world, this);
		aStar = new AStar(game, world, this);
		player = new Player(game, world, this);
		setAssets();
	}

	public void update() {
		if (game.gameState == PLAY_STATE) {
			player.update();
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[world.map][i] != null) npcs[world.map][i].update();
			for (int i = 0; i < mobs[1].length; i++) {
				if (mobs[world.map][i] != null) {
					/* Cuando muere el mob, primero establece el estado dead a true evitando que siga moviendose. Luego
					 * genera la animacion de muerte y al finalizarla, establece alive en false para que no genere
					 * movimiento y elimine el objeto. */
					if (mobs[world.map][i].alive && !mobs[world.map][i].dead) mobs[world.map][i].update();
					if (!mobs[world.map][i].alive) {
						mobs[world.map][i].checkDrop();
						mobs[world.map][i] = null;
					}
				}
			}
			for (int i = 0; i < projectiles[1].length; i++) {
				if (projectiles[world.map][i] != null) {
					if (projectiles[world.map][i].alive) projectiles[world.map][i].update();
					if (!projectiles[world.map][i].alive) projectiles[world.map][i] = null;
				}
			}
			for (int i = 0; i < particles.size(); i++) {
				if (particles.get(i) != null) {
					if (particles.get(i).alive) particles.get(i).update();
					if (!particles.get(i).alive) particles.remove(i);
				}
			}
			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[world.map][i] != null) iTile[world.map][i].update();
		}
	}

	public void render(Graphics2D g2) {
		if (game.gameState == TITLE_STATE) {
			g2.setColor(Color.black);
			g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			game.ui.draw(g2);
		} else {

			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[world.map][i] != null) iTile[world.map][i].render(g2);

			// Agrega las entidades a la lista de entidades
			entities.add(player);
			for (int i = 0; i < items[1].length; i++)
				if (items[world.map][i] != null) itemList.add(items[world.map][i]);
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[world.map][i] != null) entities.add(npcs[world.map][i]);
			for (int i = 0; i < mobs[1].length; i++)
				if (mobs[world.map][i] != null) entities.add(mobs[world.map][i]);
			for (int i = 0; i < projectiles[1].length; i++)
				if (projectiles[world.map][i] != null) entities.add(projectiles[world.map][i]);
			for (Entity particle : particles)
				if (particle != null) entities.add(particle);

			/* Ordena la lista de entidades dependiendo de la posicion Y. Es decir, si el player esta por encima del npc
			 * entonces este se dibuja por debajo. */
			entities.sort(Comparator.comparingInt(o -> o.worldY));

			for (Entity item : itemList) item.render(g2);
			for (Entity entity : entities) entity.render(g2);

			entities.clear();
			itemList.clear();

			game.ui.draw(g2);
		}
	}

	private void setAssets() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMOB();
		aSetter.setInteractiveTile();
	}

}
