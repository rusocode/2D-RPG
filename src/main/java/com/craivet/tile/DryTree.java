package com.craivet.tile;

import com.craivet.Game;
import com.craivet.entity.Player;
import com.craivet.gfx.Assets;
import com.craivet.utils.Utils;

import static com.craivet.utils.Constants.*;

public class DryTree extends InteractiveTile {

	public DryTree(Game game, int col, int row) {
		super(game);


		this.worldX = TILE_SIZE * col;
		this.worldY = TILE_SIZE * row;

		movementDown1 = Utils.scaleImage(Assets.drytree, TILE_SIZE, TILE_SIZE);
		destructible = true;
		life = 3;

	}

	public boolean isCorrectItem(Player player) {
		return player.currentWeapon.type == TYPE_AXE;
	}

	/**
	 * Reemplaza el arbol seco por el tronco en la misma posicion cuando es destruido.
	 *
	 * @return el tronco en la misma posicion que el arbol seco.
	 */
	public InteractiveTile getDestroyedForm() {
		return new Trunk(game, worldX / TILE_SIZE, worldY / TILE_SIZE);
	}

}
