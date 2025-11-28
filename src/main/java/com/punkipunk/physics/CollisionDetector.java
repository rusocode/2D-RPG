package com.punkipunk.physics;

import com.punkipunk.core.Game;
import com.punkipunk.core.IGame;
import com.punkipunk.entity.Entity;
import com.punkipunk.entity.player.Player;

/**
 * Maneja la deteccion de colisiones con entidades durante ataques.
 */

public class CollisionDetector {

    private final IGame game;

    public CollisionDetector(IGame game) {
        this.game = game;
    }

    /**
     * Detecta todas las colisiones posibles para una entidad.
     */
    public void detectCollisions(Entity entity) {
        detectMobCollisions(entity);
        detectInteractiveCollisions(entity);
        detectSpellCollisions(entity);
    }

    private void detectMobCollisions(Entity entity) {
        game.getGameSystem().collisionChecker.checkMob(entity)
                .ifPresent(mob -> ((Player) entity).hitMob(
                        mob,
                        entity,
                        ((Player) entity).weapon.stats.knockback,
                        entity.stats.attack
                ));
    }

    private void detectInteractiveCollisions(Entity entity) {
        // Si hay un valor presente (usando Optional), realiza la accion indicada con el valor presente
        game.getGameSystem().collisionChecker.checkInteractive(entity).ifPresent(interactive -> ((Player) entity).hitInteractive(interactive));
    }

    private void detectSpellCollisions(Entity entity) {
        game.getGameSystem().collisionChecker.checkSpell(entity).ifPresent(spell -> ((Player) entity).hitSpell(spell));
    }

}
