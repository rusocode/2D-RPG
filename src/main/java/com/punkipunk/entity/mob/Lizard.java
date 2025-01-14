package com.punkipunk.entity.mob;

import com.punkipunk.Dialogue;
import com.punkipunk.core.Game;
import com.punkipunk.entity.item.Gold;
import com.punkipunk.entity.item.IronDoor;
import com.punkipunk.gfx.SpriteSheet;
import com.punkipunk.io.Progress;
import com.punkipunk.utils.Utils;
import com.punkipunk.world.World;

import static com.punkipunk.utils.Global.INTERVAL_DIRECTION;

/**
 * Es el unico mob que va a tener animacion de ataque.
 */

public class Lizard extends Mob {

    public static final String NAME = "Lizard";

    public Lizard(Game game, World world, int... pos) {
        super(game, world, pos);
        dialogue = new Dialogue(game);
        attackbox.setWidth(mobData.attackboxWidth());
        attackbox.setHeight(mobData.attackboxHeight());
        sheet.loadMovementFrames(new SpriteSheet(Utils.loadTexture(mobData.spriteSheetPath())), mobData.frameWidth(), mobData.frameHeight(), mobData.frameScale());
        initDialogue();
    }

    @Override
    public void doActions() {
        // TODO Haz que siga al player cuando este atascado en un tile
        // Si la distancia del jugador con respecto al mob es menor a 10 tiles
        if (getTileDistance(game.system.world.entities.player) < 10) moveTowardPlayer(game.system.world.entities.player, 30);
        else timer.timeDirection(this, INTERVAL_DIRECTION);
        // if (!flags.hitting) isPlayerWithinAttackRange(60, tile * 6, tile * 4, 60); // TODO No se utiliza ya que por ahora el boss no tiene un sprite de ataque
    }

    private void initDialogue() {
        dialogue.dialogues[0][0] = "No one can steal my treasure.";
        dialogue.dialogues[0][1] = "You will die here.";
        dialogue.dialogues[0][2] = "WELCOME TO YOUR DOOM!";
    }

    @Override
    public void damageReaction() {
        // timer.directionCounter = 0;
    }

    @Override
    public void checkDrop() {
        drop(new Gold(game, world, mobData.gold()));
        Progress.bossDefeated = true;
        // Elimina la IronDoor
        world.entities.getItems(world.map.num).stream()
                .filter(item -> item instanceof IronDoor)
                .findFirst()
                .ifPresent(door -> world.entities.removeItem(world.map.num, door));
    }

    @Override
    protected MobType getType() {
        return MobType.LIZARD;
    }

}
