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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.craivet.utils.Constants.*;

public class EntityManager {

	private final Game game;
	public AssetSetter aSetter;
	public EventHandler event;

	public World world;
	public Collider collider;
	// public AssetSetter aSetter = new AssetSetter(this);
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

	public int map;
	public int gameState;

	public EntityManager(Game game, KeyManager key) {
		this.game = game;
		event = new EventHandler(game, this);
		world = new World(game, key);
		player = new Player(this, game, key);
		collider = new Collider(this);
		config = new Config(game);
		aSetter = new AssetSetter(this);
		aStar = new AStar(game, this);
		setAssets();
	}

	public void update() {
		if (game.gameState == PLAY_STATE) {
			player.update();
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[map][i] != null) npcs[map][i].update();
			for (int i = 0; i < mobs[1].length; i++) {
				if (mobs[map][i] != null) {
					/* Cuando muere el mob, primero establece el estado dead a true evitando que siga moviendose. Luego
					 * genera la animacion de muerte y al finalizarla, establece alive en false para que no genere
					 * movimiento y elimine el objeto. */
					if (mobs[map][i].alive && !mobs[map][i].dead) mobs[map][i].update();
					if (!mobs[map][i].alive) {
						mobs[map][i].checkDrop();
						mobs[map][i] = null;
					}
				}
			}
			for (int i = 0; i < projectiles[1].length; i++) {
				if (projectiles[map][i] != null) {
					if (projectiles[map][i].alive) projectiles[map][i].update();
					if (!projectiles[map][i].alive) projectiles[map][i] = null;
				}
			}
			for (int i = 0; i < particles.size(); i++) {
				if (particles.get(i) != null) {
					if (particles.get(i).alive) particles.get(i).update();
					if (!particles.get(i).alive) particles.remove(i);
				}
			}
			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[map][i] != null) iTile[map][i].update();
		}
	}

	public void render(Graphics2D g2) {
		if (game.gameState == TITLE_STATE) {
			g2.setColor(Color.black);
			g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			game.ui.draw(g2);
		} else {

			for (int i = 0; i < iTile[1].length; i++)
				if (iTile[map][i] != null) iTile[map][i].render(g2);

			// Agrega las entidades a la lista de entidades
			entities.add(player);
			for (int i = 0; i < items[1].length; i++)
				if (items[map][i] != null) itemList.add(items[map][i]);
			for (int i = 0; i < npcs[1].length; i++)
				if (npcs[map][i] != null) entities.add(npcs[map][i]);
			for (int i = 0; i < mobs[1].length; i++)
				if (mobs[map][i] != null) entities.add(mobs[map][i]);
			for (int i = 0; i < projectiles[1].length; i++)
				if (projectiles[map][i] != null) entities.add(projectiles[map][i]);
			for (Entity particle : particles)
				if (particle != null) entities.add(particle);

			/* Ordena la lista de entidades dependiendo de la posicion Y. Es decir, si el player esta por encima del npc
			 * entonces este se dibuja por debajo. */
			entities.sort(Comparator.comparingInt(o -> o.worldY));

			// TODO Se podria usar esta forma mas entendible para comparar las entidades
			/*
			 * private Comparator<Entity> spriteSorter = new Comparator<Entity>() {
			 * public int compare(Entity e0, Entity e1) {
			 * if (e1.y < e0.y) return +1;
			 * if (e1.y > e0.y) return -1;
			 * return 0;
			 * }
			 * };
			 * */

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
