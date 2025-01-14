package com.punkipunk.entity.player;

import com.punkipunk.core.Game;
import com.punkipunk.entity.mob.Mob;
import com.punkipunk.entity.mob.MobType;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

/**
 * <p>
 * Personaje ficticio que sirve para representar la escena del boss. Es decir, seria el jugador que permanece inmovil en la
 * entrada del jefe hasta que termina la fase 3 y comienza la fase 4.
 */

public class PlayerDummy extends Mob {

    public static final String NAME = "Dummy";

    public PlayerDummy(Game game, World world) {
        super(game, world);
        stats.name = NAME;
        sheet.loadPlayerMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameScale());
    }

    @Override
    protected MobType getType() {
        return MobType.PLAYER_DUMMY;
    }

}
