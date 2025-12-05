package com.punkipunk.entity.combat;

import com.punkipunk.core.IGame;
import com.punkipunk.entity.player.Player;
import com.punkipunk.physics.CollisionDetector;

/**
 * <p>
 * Sistema que gestiona la mecanica de ataque para las entidades del juego.
 * <p>
 * Este sistema maneja la temporizacion y fases del ataque, gestion del hitbox durante ataques y deteccion de colisiones durante
 * la ejecucion del ataque. La secuencia de ataque consta de una fase inicial con frames iniciales del ataque, una fase activa
 * donde el hitbox puede causar daño, y una fase de recuperacion que completa la animacion de ataque. Coordina todos los aspectos
 * necesarios para ejecutar ataques de manera coherente y precisa.
 */

public class AttackSystem {

    private final IGame game;
    /** Entidad que realiza el ataque */
    private final Player player;
    /** Detector de colisiones durante el ataque */
    private final CollisionDetector collisionDetector;
    /** Gestiona modificaciones temporales del hitbox durante el ataque */
    private final HitboxSystem hitboxSystem;
    /** Contador de la animacion de ataque */
    private int attackAnimationCounter;

    /**
     * Crea un nuevo sistema de ataque para la entidad especificada.
     *
     * @param game   instancia del juego que contiene los sistemas requeridos
     * @param player entidad que usara este sistema de ataque
     */
    public AttackSystem(IGame game, Player player) {
        this.game = game;
        this.player = player;
        this.collisionDetector = new CollisionDetector(game);
        this.hitboxSystem = new HitboxSystem();
    }

    public void update() {
        if (!player.flags.hitting) return;

        attackAnimationCounter++;
        // 15ms es el tiempo de animacion de ataque
        if (attackAnimationCounter <= 15) attack();
        else {
            attackAnimationCounter = 0;
            player.flags.hitting = false;
        }

    }

    /**
     * Ejecuta la logica principal del ataque.
     * <p>
     * Configura un hitbox temporal y verifica colisiones.
     */
    private void attack() {
        hitboxSystem.withTemporaryHitbox(player, () -> collisionDetector.detectCollisions(player));
    }

}
