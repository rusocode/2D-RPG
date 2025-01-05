package com.punkipunk.entity.mob;

/**
 * Los mobs se clasifican en:
 * <ul>
 * <li>NPC: Es la entidad con la que el jugador puede interactuar, dialogar o comerciar, pero no pueden ser atacados.
 * <li>Hostiles: Atacan al jugador cuando lo detectan.
 * <li>Neutrales: Atacan al jugador si son provocados.
 * <li>Pacificos: Nunca atacan al jugador pero si pueden ser atacados.
 * </ul>
 */

public enum MobType {

    NPC, HOSTILE, NEUTRAL, PEACEFUL;

}
