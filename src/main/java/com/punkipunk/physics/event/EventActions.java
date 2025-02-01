package com.punkipunk.physics.event;

import com.punkipunk.core.Game;
import com.punkipunk.entity.Entity;
import com.punkipunk.input.keyboard.Key;
import com.punkipunk.io.Progress;
import com.punkipunk.states.State;
import com.punkipunk.world.MapID;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.tile;

/**
 * Maneja la implementacion especifica de las acciones.
 */

public class EventActions {

    private final Game game;
    private final World world;

    public EventActions(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    /**
     * Causa daño a una entidad cuando activa el evento.
     */
    public void hurt(Entity entity) {
        entity.stats.decreaseHp(1);
        /* Desactiva canTriggerEvent ya que la entidad esta dentro del area del evento, evitando asi que dañe a la entidad en el
         * mismo lugar todo el tiempo. */
        game.gameSystem.eventSystem.canTriggerEvent = false;
    }

    /**
     * Cura completamente a una entidad si presiona ENTER al activar el evento.
     * <p>
     * No hace falta desactivar canTriggerEvent ya que el evento se activa en el mismo lugar solo si presiono ENTER.
     */
    public void heal(Entity entity) {
        if (game.gameSystem.keyboard.isKeyPressed(Key.ENTER)) {
            // entity.dialogue.dialogues[1][0] = "You drink the water.\nYour life has been recovered.";
            // entity.dialogue.startDialogue(State.DIALOGUE, entity, 1);
            entity.stats.fullHp();
        }
    }

    /**
     * Activa la escena del boss si no ha sido derrotado.
     */
    public void bossScene() {
        if (!world.entitySystem.player.bossBattleOn && !Progress.bossDefeated) {
            State.setState(State.CUTSCENE);
            world.cutsceneSystem.n = world.cutsceneSystem.boss;
            world.entitySystem.player.bossBattleOn = true;
        }
    }

    /**
     * Teletransporta al player.
     * <p>
     * Al cambiar de posicion no hace falta desactivar canTriggerEvent ya que la entidad se aleja cuando se teletransporta.
     */
    public void teleport(MapID mapId, int col, int row) {
        // Si la zona del mapa actual es distinta a la zona que se teletransporta, entonces reproduce el sonido ambiente de la nueva zona
        if (!world.map.id.zone.equals(mapId.zone)) game.gameSystem.audio.playAmbient(mapId.zone.name().toLowerCase());
        world.map.id = mapId;
        world.entitySystem.player.position.x = (int) ((col * tile) + world.entitySystem.player.hitbox.getWidth() / 2);
        world.entitySystem.player.position.y = (int) ((row * tile) - world.entitySystem.player.hitbox.getHeight());
    }

}
