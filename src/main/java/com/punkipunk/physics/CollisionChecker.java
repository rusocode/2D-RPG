package com.punkipunk.physics;

import com.punkipunk.Direction;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.interactive.Interactive;
import com.punkipunk.entity.item.Item;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.player.Player;
import com.punkipunk.entity.spells.Spell;
import com.punkipunk.world.World;

import java.util.List;
import java.util.Optional;

import static com.punkipunk.utils.Global.tile;

/**
 * <p>
 * La colision entre dos entidades se genera cuando uno de los limites del hitbox pasa 1 pixel del hitbox de la otra entidad. Pero
 * en el caso del attackbox, solo se genera una colision cuando los limites de ambos se tocan.
 */

public class CollisionChecker {

    private final World world;

    public CollisionChecker(World world) {
        this.world = world;
    }

    /**
     * Comprueba si la entidad colisiono con un tile solido.
     */
    public void checkTile(Entity entity) {

        int entityBottomWorldY = (int) (entity.position.y + entity.hitbox.getY() + entity.hitbox.getHeight());
        int entityTopWorldY = (int) (entity.position.y + entity.hitbox.getY());
        int entityLeftWorldX = (int) (entity.position.x + entity.hitbox.getX());
        int entityRightWorldX = (int) (entity.position.x + entity.hitbox.getX() + entity.hitbox.getWidth());

        int entityBottomRow = entityBottomWorldY / tile;
        int entityTopRow = entityTopWorldY / tile;
        int entityLeftCol = entityLeftWorldX / tile;
        int entityRightCol = entityRightWorldX / tile;

        // En caso de que la entidad colisione en medio de dos tiles solidos
        int tile1, tile2;

        // Obtiene la direccion del atacante si la entidad es empujada para evitar que la verificacion de colision se vuelva inexacta
        Direction direction = entity.flags.knockback ? entity.direction.knockbackDirection : entity.direction;

        switch (direction) {
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.id.ordinal()][entityBottomRow][entityLeftCol];
                tile2 = world.map.tileIndex[world.map.id.ordinal()][entityBottomRow][entityRightCol];
                if (world.map.tile[tile1].solid || world.map.tile[tile2].solid) entity.flags.colliding = true;
            }
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.id.ordinal()][entityTopRow][entityLeftCol];
                tile2 = world.map.tileIndex[world.map.id.ordinal()][entityTopRow][entityRightCol];
                if (world.map.tile[tile1].solid || world.map.tile[tile2].solid) entity.flags.colliding = true;
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.id.ordinal()][entityTopRow][entityLeftCol];
                tile2 = world.map.tileIndex[world.map.id.ordinal()][entityBottomRow][entityLeftCol];
                if (world.map.tile[tile1].solid || world.map.tile[tile2].solid) entity.flags.colliding = true;
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.stats.speed) / tile;
                tile1 = world.map.tileIndex[world.map.id.ordinal()][entityTopRow][entityRightCol];
                tile2 = world.map.tileIndex[world.map.id.ordinal()][entityBottomRow][entityRightCol];
                if (world.map.tile[tile1].solid || world.map.tile[tile2].solid) entity.flags.colliding = true;
            }
        }

    }

    /**
     * Comprueba si la entidad colisiono con un item.
     */
    public Optional<Item> checkItem(Entity entity) {
        List<Item> items = world.entitySystem.getItems(world.map.id);
        Direction direction = entity.flags.knockback ? entity.direction.knockbackDirection : entity.direction;
        return items.stream()
                .filter(item -> checkCollision(entity, item, direction))
                .peek(item -> {
                    if (item.solid) entity.flags.colliding = true;
                })
                .findFirst();
    }

    /**
     * Comprueba si la entidad colisiono con un mob.
     */
    public Optional<Mob> checkMob(Entity entity) {
        List<Mob> mobs = world.entitySystem.getMobs(world.map.id);
        Direction direction = entity.flags.knockback ? entity.direction.knockbackDirection : entity.direction;
        return mobs.stream()
                .filter(mob -> mob != entity) // Evita colision consigo mismo
                .filter(mob -> checkCollision(entity, mob, direction))
                .peek(mob -> {
                    entity.flags.colliding = true;
                    entity.flags.collidingOnMob = true;
                })
                .findFirst();
    }

    /**
     * Comprueba si la entidad colisiono con un interactivo.
     */
    public Optional<Interactive> checkInteractive(Entity entity) {
        List<Interactive> interactives = world.entitySystem.getInteractives(world.map.id);
        Direction direction = entity.flags.knockback ? entity.direction.knockbackDirection : entity.direction;
        return interactives.stream()
                .filter(interactive -> checkCollision(entity, interactive, direction))
                .peek(interactive -> entity.flags.colliding = true)
                .findFirst();
    }

    /**
     * Comprueba si la entidad colisiono con un spell.
     */
    public Optional<Spell> checkSpell(Entity entity) {
        List<Spell> spells = world.entitySystem.getSpells(world.map.id);
        Direction direction = entity.flags.knockback ? entity.direction.knockbackDirection : entity.direction;
        return spells.stream()
                .filter(spell -> spell != entity.spell) // ?
                .filter(spell -> checkCollision(entity, spell, direction))
                .findFirst();
    }

    /**
     * Comprueba si la entidad colisiono con el player.
     */
    public boolean checkPlayer(Entity entity) {
        Player player = world.entitySystem.player;
        // No comprueba si es el propio player
        if (entity == player) return false;
        return checkCollision(entity, player, entity.direction);
    }

    /**
     * Comprueba la colision entre dos entidades.
     */
    private boolean checkCollision(Entity entity1, Entity entity2, Direction direction) {

        // Guarda las posiciones originales
        // TODO Creo que estas variables reemplazan a hitboxDefaultX y hitboxDefaultY
        double originalX1 = entity1.hitbox.getX();
        double originalY1 = entity1.hitbox.getY();
        double originalX2 = entity2.hitbox.getX();
        double originalY2 = entity2.hitbox.getY();

        // Ajusta las posiciones para la deteccion
        entity1.hitbox.setX(originalX1 + entity1.position.x);
        entity1.hitbox.setY(originalY1 + entity1.position.y);
        entity2.hitbox.setX(originalX2 + entity2.position.x);
        entity2.hitbox.setY(originalY2 + entity2.position.y);

        // Ajusta por direccion y velocidad
        switch (direction) {
            case DOWN -> entity1.hitbox.setY(entity1.hitbox.getY() + entity1.stats.speed);
            case UP -> entity1.hitbox.setY(entity1.hitbox.getY() - entity1.stats.speed);
            case LEFT -> entity1.hitbox.setX(entity1.hitbox.getX() - entity1.stats.speed);
            case RIGHT -> entity1.hitbox.setX(entity1.hitbox.getX() + entity1.stats.speed);
        }

        // Verifica la colision
        boolean collision = entity1.hitbox.intersects(entity2.hitbox.getBoundsInParent());

        // Restaura las posiciones originales
        entity1.hitbox.setX(originalX1);
        entity1.hitbox.setY(originalY1);
        entity2.hitbox.setX(originalX2);
        entity2.hitbox.setY(originalY2);

        return collision;
    }

}
