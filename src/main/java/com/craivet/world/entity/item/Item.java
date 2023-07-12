package com.craivet.world.entity.item;

import com.craivet.Game;
import com.craivet.world.entity.Entity;
import com.craivet.world.World;

import static com.craivet.util.Global.*;
import static com.craivet.util.Global.tile_size;

public class Item extends Entity {

    protected int value;

    public Item(Game game, World world, int x, int y) {
        super(game, world, x, y);
    }

    /**
     * Usa el item.
     *
     * @param entity entidad que utiliza el item.
     * @return true si puede usarlo o false.
     */
    public boolean use(Entity entity) {
        return false;
    }

    public void setLoot(Entity loot) {
    }

    /**
     * Interactua con el item.
     */
    public void interact() {
    }

    /**
     * Detecta si el item especificado se encuentra en la posicion adyacente a la entidad.
     *
     * @param entity entidad.
     * @param items  lista de items.
     * @param name   nombre del item.
     * @return el indice del item especificado a la posicion adyacente de la entidad o -1 si no existe.
     */
    protected int detect(Entity entity, Entity[][] items, String name) {
        // Verifica el item adyacente a la entidad
        int nextX = entity.getLeftHitbox();
        int nextY = entity.getTopHitbox();

        switch (entity.direction) {
            case DOWN -> nextY = entity.getBottomHitbox() + entity.speed;
            case UP -> nextY = entity.getTopHitbox() - entity.speed;
            case LEFT -> nextX = entity.getLeftHitbox() - entity.speed;
            case RIGHT -> nextX = entity.getRightHitbox() + entity.speed;
        }

        int row = nextY / tile_size;
        int col = nextX / tile_size;

        // Si el item iterado es igual a la posicion adyacente de la entidad
        for (int i = 0; i < items[1].length; i++) {
            if (items[world.map][i] != null)
                if (items[world.map][i].getRow() == row && items[world.map][i].getCol() == col && items[world.map][i].name.equals(name))
                    return i;
        }

        return -1;
    }

}
