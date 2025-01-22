package com.punkipunk.entity.combat;

import com.punkipunk.Direction;
import com.punkipunk.entity.Entity;

/**
 * <p>
 * Sistema que gestiona la hitbox de ataque para las entidades.
 * <p>
 * Se encarga de guardar y restaurar estados de hitbox, configurar hitbox temporales durante ataques y manejar transformaciones de
 * hitbox segun la direccion de la entidad. Proporciona un mecanismo seguro para modificar temporalmente las areas de colision
 * durante los ataques.
 */

public class HitboxSystem {

    /**
     * Ejecuta una accion usando una hitbox temporal.
     * <p>
     * Guarda el estado original del hitbox, aplica la configuracion temporal, ejecuta la accion y restaura el estado original
     * incluso si hay errores.
     *
     * @param entity entidad a la que se aplica la hitbox temporal
     * @param action accion a ejecutar mientras la hitbox temporal esta activa
     */
    public void withTemporaryHitbox(Entity entity, Runnable action) {
        HitboxState originalState = saveState(entity);
        configureAttackHitbox(entity);
        try {
            action.run();
        } finally {
            restoreState(entity, originalState);
        }
    }

    /**
     * Guarda el estado actual de la hitbox y posicion de la entidad.
     *
     * @param entity entidad cuyo estado se va a guardar
     * @return estado guardado de la hitbox
     */
    private HitboxState saveState(Entity entity) {
        return new HitboxState(entity.position.x, entity.position.y, (int) entity.hitbox.getWidth(), (int) entity.hitbox.getHeight());
    }

    /**
     * Configura la hitbox de ataque para la entidad.
     * <p>
     * Obtiene y aplica la configuracion basada en la direccion.
     *
     * @param entity entidad para la que se configura la hitbox
     */
    private void configureAttackHitbox(Entity entity) {
        AttackHitboxConfig config = getHitboxConfig(entity.direction);
        applyHitboxConfig(entity, config);
    }

    /**
     * Determina la configuracion del hitbox segun la direccion.
     * <p>
     * Las coordenadas se calculan desde la esquina superior izquierda del hitbox del jugador.
     * <p>
     * TODO Las coordenadas de cada hitbox deberian comenzar a partir de la imagen
     * FIXME Eliminar valores hardcodeados
     *
     * @param direction direccion que determina la configuracion
     * @return configuracion de hitbox para la direccion especificada
     */
    private AttackHitboxConfig getHitboxConfig(Direction direction) {
        return switch (direction) {
            case DOWN -> new AttackHitboxConfig(0, 4, 4, 44);
            case UP -> new AttackHitboxConfig(13, -43, 4, 42);
            case LEFT -> new AttackHitboxConfig(-20, 3, 19, 4);
            case RIGHT -> new AttackHitboxConfig(13, 5, 18, 4);
            case ANY -> new AttackHitboxConfig(0, 0, 0, 0); // FIXME Revisar
        };
    }

    /**
     * Aplica una configuracion de hitbox a la entidad.
     * <p>
     * Actualiza posicion, dimensiones y area de ataque.
     *
     * @param entity entidad a la que se aplica la configuracion
     * @param config configuracion de hitbox a aplicar
     */
    private void applyHitboxConfig(Entity entity, AttackHitboxConfig config) {
        entity.attackbox.setX(config.x());
        entity.attackbox.setY(config.y());
        entity.attackbox.setWidth(config.width());
        entity.attackbox.setHeight(config.height());

        /* Acumula la posicion del frame de ataque con la posicion del player para comprobar la colision con las coordenadas
         * ajustadas del frame de ataque. */
        entity.position.x += (int) entity.attackbox.getX();
        entity.position.y += (int) entity.attackbox.getY();
        // Convierte el hitbox (ancho y alto) en el attackbox para comprobar la colision solo con el attackbox
        entity.hitbox.setWidth(entity.attackbox.getWidth());
        entity.hitbox.setHeight(entity.attackbox.getHeight());
    }

    /**
     * Restaura el estado original de la hitbox y posicion.
     *
     * @param entity entidad cuyo estado se restaura
     * @param state  estado original a restaurar
     */
    private void restoreState(Entity entity, HitboxState state) {
        entity.position.x = state.x();
        entity.position.y = state.y();
        entity.hitbox.setWidth(state.width());
        entity.hitbox.setHeight(state.height());
    }

    /**
     * Representa el estado de un hitbox (cuadro de colision) con sus propiedades de posicion y dimensiones.
     * <p>
     * Este record es inmutable y utiliza el patron de datos para encapsular:
     * <ul>
     * <li>Coordenadas (x,y) que definen la posicion
     * <li>Dimensiones (width,height) que definen el tamaño
     * </ul>
     *
     * @param x      coordenada x de la esquina superior izquierda
     * @param y      coordenada y de la esquina superior izquierda
     * @param width  ancho del hitbox en pixeles
     * @param height alto del hitbox en pixeles
     */
    private record HitboxState(int x, int y, int width, int height) {
    }

    /**
     * Configuracion inmutable para el hitbox de ataque de una entidad.
     * <p>
     * Encapsula las dimensiones y posicion del area de ataque:
     * <ul>
     * <li>Posicion (x,y) relativa a la entidad
     * <li>Dimensiones (width,height) del area de ataque
     * </ul>
     *
     * @param x      offset horizontal desde la entidad
     * @param y      offset vertical desde la entidad
     * @param width  ancho del area de ataque
     * @param height alto del area de ataque
     */
    private record AttackHitboxConfig(double x, double y, double width, double height) {
    }

}
